const Resources = (() => {
    let allResources = [];

    async function loadResources() {
        const tbody = document.getElementById('resources-table-body');
        tbody.innerHTML = '<tr><td colspan="8" class="text-center">Loading resources...</td></tr>';
        try {
            const res = await API.get('/api/resources');
            if (res.success && res.data) {
                allResources = res.data;
                renderResources();
            }
        } catch (e) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center" style="color:var(--danger);">Failed to load resources</td></tr>';
        }
    }

    function renderResources() {
        const tbody = document.getElementById('resources-table-body');
        const search = (document.getElementById('resource-search-input').value || '').toLowerCase();
        const typeFilter = document.getElementById('resource-type-filter').value;

        const filtered = allResources.filter(r => {
            const matchesSearch = r.name.toLowerCase().includes(search) || 
                                  (r.serialNumber && r.serialNumber.toLowerCase().includes(search));
            const matchesType = typeFilter === 'ALL' || r.type === typeFilter;
            return matchesSearch && matchesType;
        });

        if (filtered.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center" style="color:var(--text-muted);">No enterprise resources found.</td></tr>';
            return;
        }

        const isManagerOrAdmin = Auth.hasRole('ROLE_ADMIN', 'ROLE_MANAGER');
        const isAdmin = Auth.hasRole('ROLE_ADMIN');

        tbody.innerHTML = filtered.map(r => `
            <tr>
                <td>
                    <div style="font-weight:600;">${r.name}</div>
                    <div style="font-size:0.75rem;color:var(--text-muted);">${r.description || ''}</div>
                </td>
                <td><span style="font-size:0.8rem;background:#182234;padding:2px 6px;border-radius:4px;">${r.type}</span></td>
                <td><code>${r.serialNumber || 'N/A'}</code></td>
                <td><span class="badge badge-${r.status.toLowerCase()}">${r.status}</span></td>
                <td>
                    <strong>${r.availableCapacity}</strong> / ${r.totalCapacity} units
                </td>
                <td>${r.location || '-'}</td>
                <td>${r.departmentName || '-'}</td>
                <td>
                    <div style="display:flex;gap:6px;">
                        ${isManagerOrAdmin && r.availableCapacity > 0 ? `
                            <button class="action-btn" onclick="Allocations.openAllocateModal(${r.id})">Allocate</button>
                        ` : ''}
                        ${isManagerOrAdmin ? `
                            <button class="action-btn" onclick="Resources.editResource(${r.id})">Edit</button>
                        ` : ''}
                        ${isAdmin ? `
                            <button class="action-btn action-btn-danger" onclick="Resources.deleteResource(${r.id})">Del</button>
                        ` : ''}
                    </div>
                </td>
            </tr>
        `).join('');
    }

    function openCreateModal() {
        document.getElementById('resource-modal-title').textContent = 'Register Enterprise Resource';
        document.getElementById('resource-id').value = '';
        document.getElementById('resource-name').value = '';
        document.getElementById('resource-type').value = 'EQUIPMENT';
        document.getElementById('resource-serial').value = '';
        document.getElementById('resource-capacity').value = '1';
        document.getElementById('resource-location').value = '';
        document.getElementById('resource-desc').value = '';
        document.getElementById('resource-modal').classList.remove('hidden');
    }

    function editResource(resourceId) {
        const res = allResources.find(r => r.id === resourceId);
        if (!res) return;

        document.getElementById('resource-modal-title').textContent = 'Edit Resource';
        document.getElementById('resource-id').value = res.id;
        document.getElementById('resource-name').value = res.name;
        document.getElementById('resource-type').value = res.type;
        document.getElementById('resource-serial').value = res.serialNumber || '';
        document.getElementById('resource-capacity').value = res.totalCapacity;
        document.getElementById('resource-location').value = res.location || '';
        document.getElementById('resource-desc').value = res.description || '';
        document.getElementById('resource-modal').classList.remove('hidden');
    }

    async function saveResource(e) {
        e.preventDefault();
        const id = document.getElementById('resource-id').value;
        const payload = {
            name: document.getElementById('resource-name').value,
            type: document.getElementById('resource-type').value,
            serialNumber: document.getElementById('resource-serial').value,
            totalCapacity: parseInt(document.getElementById('resource-capacity').value),
            location: document.getElementById('resource-location').value,
            description: document.getElementById('resource-desc').value
        };

        try {
            if (id) {
                await API.put('/api/resources/' + id, payload);
                app.showToast('Resource updated successfully', 'success');
            } else {
                await API.post('/api/resources', payload);
                app.showToast('Resource registered successfully', 'success');
            }
            app.closeModals();
            loadResources();
            Dashboard.refresh();
        } catch (err) {
            app.showToast(err.message, 'error');
        }
    }

    async function deleteResource(id) {
        if (!confirm('Are you sure you want to retire and remove this resource?')) return;
        try {
            await API.delete('/api/resources/' + id);
            app.showToast('Resource removed', 'success');
            loadResources();
            Dashboard.refresh();
        } catch (e) {
            app.showToast(e.message, 'error');
        }
    }

    function getAllResources() {
        return allResources;
    }

    return {
        loadResources,
        renderResources,
        openCreateModal,
        editResource,
        saveResource,
        deleteResource,
        getAllResources
    };
})();