const app = (() => {
    let usersList = [];

    async function init() {
        Auth.init();
        setupEventListeners();

        if (Auth.isAuthenticated()) {
            showAppView();
        } else {
            showAuthView();
        }
    }

    function setupEventListeners() {
        document.getElementById('login-form').addEventListener('submit', handleLogin);
        document.getElementById('register-form').addEventListener('submit', handleRegister);
        document.getElementById('show-register-link').addEventListener('click', (e) => {
            e.preventDefault();
            document.getElementById('login-form').classList.add('hidden');
            document.getElementById('register-form').classList.remove('hidden');
        });
        document.getElementById('show-login-link').addEventListener('click', (e) => {
            e.preventDefault();
            document.getElementById('register-form').classList.add('hidden');
            document.getElementById('login-form').classList.remove('hidden');
        });

        document.getElementById('logout-btn').addEventListener('click', () => Auth.logout());

        document.querySelectorAll('.nav-item').forEach(item => {
            item.addEventListener('click', (e) => {
                e.preventDefault();
                const view = item.getAttribute('data-view');
                navigateTo(view);
            });
        });

        document.getElementById('task-search-input').addEventListener('input', () => Tasks.renderTasks());
        document.getElementById('task-status-filter').addEventListener('change', () => Tasks.renderTasks());
        document.getElementById('task-priority-filter').addEventListener('change', () => Tasks.renderTasks());

        document.getElementById('resource-search-input').addEventListener('input', () => Resources.renderResources());
        document.getElementById('resource-type-filter').addEventListener('change', () => Resources.renderResources());

        document.getElementById('open-create-task-modal-btn').addEventListener('click', () => Tasks.openCreateModal());
        document.getElementById('quick-create-task-btn').addEventListener('click', () => Tasks.openCreateModal());
        document.getElementById('open-create-resource-modal-btn').addEventListener('click', () => Resources.openCreateModal());
        document.getElementById('quick-create-res-btn').addEventListener('click', () => Resources.openCreateModal());
        document.getElementById('open-create-allocation-modal-btn').addEventListener('click', () => Allocations.openAllocateModal());

        document.getElementById('task-form').addEventListener('submit', Tasks.saveTask);
        document.getElementById('resource-form').addEventListener('submit', Resources.saveResource);
        document.getElementById('allocation-form').addEventListener('submit', Allocations.saveAllocation);
    }

    async function handleLogin(e) {
        e.preventDefault();
        const u = document.getElementById('login-username').value;
        const p = document.getElementById('login-password').value;
        try {
            await Auth.login(u, p);
            showToast('Welcome back, ' + u + '!', 'success');
            showAppView();
        } catch (err) {
            showToast(err.message, 'error');
        }
    }

    async function handleRegister(e) {
        e.preventDefault();
        const payload = {
            fullName: document.getElementById('reg-fullname').value,
            username: document.getElementById('reg-username').value,
            email: document.getElementById('reg-email').value,
            password: document.getElementById('reg-password').value,
            role: document.getElementById('reg-role').value,
            departmentId: parseInt(document.getElementById('reg-dept').value)
        };
        try {
            await Auth.register(payload);
            showToast('Account created! Please sign in with your credentials.', 'success');
            document.getElementById('register-form').classList.add('hidden');
            document.getElementById('login-form').classList.remove('hidden');
            document.getElementById('login-username').value = payload.username;
        } catch (err) {
            showToast(err.message, 'error');
        }
    }

    function showAuthView() {
        document.getElementById('auth-view').classList.remove('hidden');
        document.getElementById('app-view').classList.add('hidden');
    }

    async function showAppView() {
        document.getElementById('auth-view').classList.add('hidden');
        document.getElementById('app-view').classList.remove('hidden');

        const user = Auth.getUser();
        if (user) {
            document.getElementById('sidebar-user-name').textContent = user.fullName || user.username;
            document.getElementById('sidebar-user-avatar').textContent = (user.fullName || user.username).charAt(0).toUpperCase();
            document.getElementById('sidebar-user-role').textContent = user.role.replace('ROLE_', '');
        }

        const isManagerOrAdmin = Auth.hasRole('ROLE_ADMIN', 'ROLE_MANAGER');
        document.querySelectorAll('.manager-action').forEach(el => {
            el.style.display = isManagerOrAdmin ? 'inline-flex' : 'none';
        });
        if (isManagerOrAdmin) {
            document.getElementById('quick-create-task-btn').style.display = 'inline-flex';
            document.getElementById('quick-create-res-btn').style.display = 'inline-flex';
        }

        await loadDropdowns();
        navigateTo('dashboard');
    }

    async function loadDropdowns() {
        try {
            const uRes = await API.get('/api/users');
            if (uRes.success && uRes.data) {
                usersList = uRes.data;
                const taskAssigneeSelect = document.getElementById('task-assignee');
                const allocUserSelect = document.getElementById('alloc-user-id');

                taskAssigneeSelect.innerHTML = '<option value="">Unassigned</option>' +
                    usersList.map(u => `<option value="${u.id}">${u.fullName} (${u.role.replace('ROLE_', '')})</option>`).join('');

                allocUserSelect.innerHTML = '<option value="">Select team member</option>' +
                    usersList.map(u => `<option value="${u.id}">${u.fullName} (${u.departmentName || 'General'})</option>`).join('');

                renderUsersTable();
            }

            const tRes = await API.get('/api/tasks');
            if (tRes.success && tRes.data) {
                const allocTaskSelect = document.getElementById('alloc-task-id');
                allocTaskSelect.innerHTML = '<option value="">None / General Use</option>' +
                    tRes.data.map(t => `<option value="${t.id}">${t.title}</option>`).join('');
            }
        } catch (e) {
            console.error('Failed to populate dropdowns', e);
        }
    }

    function renderUsersTable() {
        const tbody = document.getElementById('users-table-body');
        if (!usersList || usersList.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="text-center">No directory data</td></tr>';
            return;
        }

        tbody.innerHTML = usersList.map(u => `
            <tr>
                <td><strong>${u.fullName}</strong></td>
                <td><code>${u.username}</code></td>
                <td>${u.email}</td>
                <td><span class="badge badge-in_progress">${u.role.replace('ROLE_', '')}</span></td>
                <td>${u.departmentName || 'General'}</td>
            </tr>
        `).join('');
    }

    function navigateTo(viewName) {
        document.querySelectorAll('.nav-item').forEach(item => {
            if (item.getAttribute('data-view') === viewName) {
                item.classList.add('active');
            } else {
                item.classList.remove('active');
            }
        });

        document.querySelectorAll('.content-view').forEach(view => {
            view.classList.remove('active');
        });

        const target = document.getElementById('view-' + viewName);
        if (target) {
            target.classList.add('active');
        }

        const titles = {
            dashboard: { title: 'Dashboard Overview', sub: 'Real-time enterprise metrics and task distribution' },
            tasks: { title: 'Task Lifecycle Management', sub: 'Track, prioritize, and assign enterprise tasks' },
            resources: { title: 'Enterprise Resource Inventory', sub: 'Manage hardware, licenses, servers, and capacity' },
            allocations: { title: 'Resource Allocations & Bookings', sub: 'Active checkouts, reservations, and returns' },
            users: { title: 'Organization Directory', sub: 'Team members, departments, and roles' }
        };

        if (titles[viewName]) {
            document.getElementById('header-view-title').textContent = titles[viewName].title;
            document.getElementById('header-view-subtitle').textContent = titles[viewName].sub;
        }

        if (viewName === 'dashboard') Dashboard.refresh();
        if (viewName === 'tasks') Tasks.loadTasks();
        if (viewName === 'resources') Resources.loadResources();
        if (viewName === 'allocations') Allocations.loadAllocations();
        if (viewName === 'users') renderUsersTable();
    }

    function closeModals() {
        document.querySelectorAll('.modal-backdrop').forEach(m => m.classList.add('hidden'));
    }

    function showToast(message, type = 'info') {
        const container = document.getElementById('toast-container');
        const toast = document.createElement('div');
        toast.className = 'toast toast-' + type;
        toast.textContent = message;
        container.appendChild(toast);

        setTimeout(() => {
            toast.style.opacity = '0';
            toast.style.transform = 'translateX(100%)';
            toast.style.transition = 'all 0.3s';
            setTimeout(() => toast.remove(), 300);
        }, 4000);
    }

    document.addEventListener('DOMContentLoaded', init);

    return {
        navigateTo,
        closeModals,
        showToast
    };
})();