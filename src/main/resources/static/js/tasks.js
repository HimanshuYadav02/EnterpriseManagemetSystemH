const Tasks = (() => {
    let allTasks = [];

    async function loadTasks() {
        const tbody = document.getElementById('tasks-table-body');
        tbody.innerHTML = '<tr><td colspan="8" class="text-center">Loading tasks...</td></tr>';
        try {
            const res = await API.get('/api/tasks');
            if (res.success && res.data) {
                allTasks = res.data;
                renderTasks();
            }
        } catch (e) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center" style="color:var(--danger);">Failed to load tasks</td></tr>';
        }
    }

    function renderTasks() {
        const tbody = document.getElementById('tasks-table-body');
        const search = (document.getElementById('task-search-input').value || '').toLowerCase();
        const statusFilter = document.getElementById('task-status-filter').value;
        const priorityFilter = document.getElementById('task-priority-filter').value;

        const filtered = allTasks.filter(t => {
            const matchesSearch = t.title.toLowerCase().includes(search) || 
                                  (t.assignedToName && t.assignedToName.toLowerCase().includes(search));
            const matchesStatus = statusFilter === 'ALL' || t.status === statusFilter;
            const matchesPriority = priorityFilter === 'ALL' || t.priority === priorityFilter;
            return matchesSearch && matchesStatus && matchesPriority;
        });

        if (filtered.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center" style="color:var(--text-muted);">No tasks found matching criteria.</td></tr>';
            return;
        }

        const isManagerOrAdmin = Auth.hasRole('ROLE_ADMIN', 'ROLE_MANAGER');

        tbody.innerHTML = filtered.map(t => `
            <tr>
                <td>
                    <div style="font-weight:600;">${t.title}</div>
                    <div style="font-size:0.75rem;color:var(--text-muted);">${t.description ? t.description.substring(0, 60) + '...' : ''}</div>
                </td>
                <td><span class="badge badge-${t.priority.toLowerCase()}">${t.priority}</span></td>
                <td>
                    <select class="quick-status-select" onchange="Tasks.updateStatus(${t.id}, this.value)">
                        <option value="TODO" ${t.status === 'TODO' ? 'selected' : ''}>TODO</option>
                        <option value="IN_PROGRESS" ${t.status === 'IN_PROGRESS' ? 'selected' : ''}>IN PROGRESS</option>
                        <option value="IN_REVIEW" ${t.status === 'IN_REVIEW' ? 'selected' : ''}>IN REVIEW</option>
                        <option value="COMPLETED" ${t.status === 'COMPLETED' ? 'selected' : ''}>COMPLETED</option>
                    </select>
                </td>
                <td>${t.assignedToName || '<span style="color:var(--text-muted)">Unassigned</span>'}</td>
                <td>${t.departmentName || '-'}</td>
                <td>${t.dueDate || '-'}</td>
                <td>${t.estimatedHours || 0}h / ${t.actualHours || 0}h</td>
                <td>
                    <div style="display:flex;gap:6px;">
                        ${isManagerOrAdmin ? `
                            <button class="action-btn" onclick="Tasks.editTask(${t.id})">Edit</button>
                            <button class="action-btn action-btn-danger" onclick="Tasks.deleteTask(${t.id})">Delete</button>
                        ` : '<span style="font-size:0.75rem;color:var(--text-muted)">Assigned</span>'}
                    </div>
                </td>
            </tr>
        `).join('');
    }

    async function updateStatus(taskId, newStatus) {
        try {
            const res = await API.patch('/api/tasks/' + taskId + '/status', { status: newStatus });
            if (res.success) {
                app.showToast('Task status updated to ' + newStatus, 'success');
                loadTasks();
                Dashboard.refresh();
            }
        } catch (e) {
            app.showToast(e.message, 'error');
            loadTasks();
        }
    }

    function openCreateModal() {
        document.getElementById('task-modal-title').textContent = 'Create New Task';
        document.getElementById('task-id').value = '';
        document.getElementById('task-title').value = '';
        document.getElementById('task-desc').value = '';
        document.getElementById('task-priority').value = 'MEDIUM';
        document.getElementById('task-due-date').value = '';
        document.getElementById('task-estimated-hours').value = '8';
        document.getElementById('task-assignee').value = '';
        document.getElementById('task-modal').classList.remove('hidden');
    }

    function editTask(taskId) {
        const task = allTasks.find(t => t.id === taskId);
        if (!task) return;

        document.getElementById('task-modal-title').textContent = 'Edit Task';
        document.getElementById('task-id').value = task.id;
        document.getElementById('task-title').value = task.title;
        document.getElementById('task-desc').value = task.description || '';
        document.getElementById('task-priority').value = task.priority;
        document.getElementById('task-due-date').value = task.dueDate || '';
        document.getElementById('task-estimated-hours').value = task.estimatedHours || 0;
        document.getElementById('task-assignee').value = task.assignedToId || '';
        document.getElementById('task-modal').classList.remove('hidden');
    }

    async function saveTask(e) {
        e.preventDefault();
        const id = document.getElementById('task-id').value;
        const payload = {
            title: document.getElementById('task-title').value,
            description: document.getElementById('task-desc').value,
            priority: document.getElementById('task-priority').value,
            dueDate: document.getElementById('task-due-date').value || null,
            estimatedHours: parseFloat(document.getElementById('task-estimated-hours').value) || 0.0,
            assignedToUserId: document.getElementById('task-assignee').value ? parseInt(document.getElementById('task-assignee').value) : null
        };

        try {
            if (id) {
                await API.put('/api/tasks/' + id, payload);
                app.showToast('Task updated successfully', 'success');
            } else {
                await API.post('/api/tasks', payload);
                app.showToast('Task created successfully', 'success');
            }
            app.closeModals();
            loadTasks();
            Dashboard.refresh();
        } catch (err) {
            app.showToast(err.message, 'error');
        }
    }

    async function deleteTask(taskId) {
        if (!confirm('Are you sure you want to delete this task?')) return;
        try {
            await API.delete('/api/tasks/' + taskId);
            app.showToast('Task deleted successfully', 'success');
            loadTasks();
            Dashboard.refresh();
        } catch (e) {
            app.showToast(e.message, 'error');
        }
    }

    return {
        loadTasks,
        renderTasks,
        updateStatus,
        openCreateModal,
        editTask,
        saveTask,
        deleteTask
    };
})();