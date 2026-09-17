const Allocations = (() => {
    let allAllocations = [];

    async function loadAllocations() {
        const tbody = document.getElementById('allocations-table-body');
        tbody.innerHTML = '<tr><td colspan="8" class="text-center">Loading allocations...</td></tr>';
        try {
            const res = await API.get('/api/allocations');
            if (res.success && res.data) {
                allAllocations = res.data;
                renderAllocations();
            }
        } catch (e) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center" style="color:var(--danger);">Failed to load allocations</td></tr>';
        }
    }

    function renderAllocations() {
        const tbody = document.getElementById('allocations-table-body');
        if (allAllocations.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center" style="color:var(--text-muted);">No active allocations.</td></tr>';
            return;
        }

        const isManagerOrAdmin = Auth.hasRole('ROLE_ADMIN', 'ROLE_MANAGER');

        tbody.innerHTML = allAllocations.map(a => `
            <tr>
                <td>
                    <div style="font-weight:600;">${a.resourceName}</div>
                    <span style="font-size:0.75rem;color:var(--text-muted);">${a.resourceType}</span>
                </td>
                <td>${a.userName}</td>
                <td>${a.taskTitle || '<span style="color:var(--text-muted)">General Assignment</span>'}</td>
                <td><strong>${a.allocatedQuantity}</strong></td>
                <td>${a.startDate}</td>
                <td>${a.endDate}</td>
                <td><span class="badge badge-${a.status === 'ACTIVE' ? 'available' : 'todo'}">${a.status}</span></td>
                <td>
                    <div style="display:flex;gap:6px;">
                        ${a.status === 'ACTIVE' ? `
                            <button class="action-btn" onclick="Allocations.returnAllocation(${a.id})">Return / Check-In</button>
                        ` : '<span style="font-size:0.75rem;color:var(--text-muted);">Resolved</span>'}
                        ${isManagerOrAdmin && a.status === 'ACTIVE' ? `
                            <button class="action-btn action-btn-danger" onclick="Allocations.cancelAllocation(${a.id})">Cancel</button>
                        ` : ''}
                    </div>
                </td>
            </tr>
        `).join('');
    }

    function openAllocateModal(preselectedResourceId = null) {
        const resSelect = document.getElementById('alloc-resource-id');
        const availableResources = Resources.getAllResources().filter(r => r.availableCapacity > 0);

        resSelect.innerHTML = '<option value="">Select an available resource</option>' +
            availableResources.map(r => `<option value="${r.id}" ${preselectedResourceId === r.id ? 'selected' : ''}>${r.name} (${r.availableCapacity} available)</option>`).join('');

        const today = new Date().toISOString().split('T')[0];
        const nextMonth = new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString().split('T')[0];

        document.getElementById('alloc-start-date').value = today;
        document.getElementById('alloc-end-date').value = nextMonth;
        document.getElementById('alloc-quantity').value = '1';
        document.getElementById('alloc-notes').value = '';

        document.getElementById('allocation-modal').classList.remove('hidden');
    }

    async function saveAllocation(e) {
        e.preventDefault();
        const payload = {
            resourceId: parseInt(document.getElementById('alloc-resource-id').value),
            userId: parseInt(document.getElementById('alloc-user-id').value),
            taskId: document.getElementById('alloc-task-id').value ? parseInt(document.getElementById('alloc-task-id').value) : null,
            quantity: parseInt(document.getElementById('alloc-quantity').value),
            startDate: document.getElementById('alloc-start-date').value,
            endDate: document.getElementById('alloc-end-date').value,
            notes: document.getElementById('alloc-notes').value
        };

        try {
            await API.post('/api/allocations', payload);
            app.showToast('Resource allocated successfully', 'success');
            app.closeModals();
            loadAllocations();
            Resources.loadResources();
            Dashboard.refresh();
        } catch (err) {
            app.showToast(err.message, 'error');
        }
    }

    async function returnAllocation(id) {
        try {
            await API.post('/api/allocations/' + id + '/return');
            app.showToast('Resource checked-in / returned successfully', 'success');
            loadAllocations();
            Resources.loadResources();
            Dashboard.refresh();
        } catch (e) {
            app.showToast(e.message, 'error');
        }
    }

    async function cancelAllocation(id) {
        if (!confirm('Cancel this allocation and restore resource capacity?')) return;
        try {
            await API.post('/api/allocations/' + id + '/cancel');
            app.showToast('Allocation cancelled', 'success');
            loadAllocations();
            Resources.loadResources();
            Dashboard.refresh();
        } catch (e) {
            app.showToast(e.message, 'error');
        }
    }

    return {
        loadAllocations,
        renderAllocations,
        openAllocateModal,
        saveAllocation,
        returnAllocation,
        cancelAllocation
    };
})();