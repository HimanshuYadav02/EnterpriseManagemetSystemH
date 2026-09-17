const Dashboard = (() => {
    async function loadStats() {
        try {
            const res = await API.get('/api/dashboard/stats');
            if (res.success && res.data) {
                const s = res.data;
                document.getElementById('stat-total-tasks').textContent = s.totalTasks;
                document.getElementById('stat-completed-rate').textContent = s.taskCompletionRate + '% Completed (' + s.completedTasks + ')';
                document.getElementById('stat-in-progress-tasks').textContent = s.inProgressTasks;
                document.getElementById('stat-todo-tasks').textContent = s.todoTasks + '  ' + s.inReviewTasks + ' in review';
                document.getElementById('stat-overdue-tasks').textContent = s.overdueTasks;
                document.getElementById('stat-total-resources').textContent = s.totalResources;
                document.getElementById('stat-utilization-rate').textContent = s.resourceUtilizationRate + '% Utilization (' + s.activeAllocations + ' active)';
            }
        } catch (e) {
            console.error('Failed to load dashboard stats', e);
        }
    }

    async function loadRecentTasks() {
        const container = document.getElementById('dashboard-recent-tasks');
        try {
            const res = await API.get('/api/tasks');
            if (res.success && res.data) {
                const tasks = res.data.slice(0, 5);
                if (tasks.length === 0) {
                    container.innerHTML = '<p class="text-center" style="color:var(--text-muted);padding:10px;">No tasks assigned yet.</p>';
                    return;
                }

                let html = '<div style="display:flex;flex-direction:column;gap:10px;">';
                tasks.forEach(t => {
                    html += `
                        <div style="display:flex;align-items:center;justify-content:space-between;padding:10px;background:#182234;border-radius:8px;">
                            <div>
                                <div style="font-weight:600;font-size:0.9rem;">${t.title}</div>
                                <div style="font-size:0.75rem;color:var(--text-muted);">Assigned to: ${t.assignedToName || 'Unassigned'} • Due: ${t.dueDate || 'No date'}</div>
                            </div>
                            <div style="display:flex;gap:6px;">
                                <span class="badge badge-${t.priority.toLowerCase()}">${t.priority}</span>
                                <span class="badge badge-${t.status.toLowerCase()}">${t.status}</span>
                            </div>
                        </div>
                    `;
                });
                html += '</div>';
                container.innerHTML = html;
            }
        } catch (e) {
            container.innerHTML = '<p style="color:var(--danger)">Error loading tasks</p>';
        }
    }

    async function loadResourceSummary() {
        const container = document.getElementById('dashboard-resource-summary');
        try {
            const res = await API.get('/api/resources');
            if (res.success && res.data) {
                const resources = res.data.slice(0, 5);
                if (resources.length === 0) {
                    container.innerHTML = '<p class="text-center" style="color:var(--text-muted);padding:10px;">No resources registered.</p>';
                    return;
                }

                let html = '<div style="display:flex;flex-direction:column;gap:10px;">';
                resources.forEach(r => {
                    const pct = Math.round((r.availableCapacity / r.totalCapacity) * 100);
                    html += `
                        <div style="padding:10px;background:#182234;border-radius:8px;">
                            <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:6px;">
                                <span style="font-weight:600;font-size:0.9rem;">${r.name}</span>
                                <span class="badge badge-${r.status.toLowerCase()}">${r.status}</span>
                            </div>
                            <div style="display:flex;justify-content:space-between;font-size:0.75rem;color:var(--text-muted);margin-bottom:4px;">
                                <span>Type: ${r.type}</span>
                                <span>${r.availableCapacity} / ${r.totalCapacity} Available</span>
                            </div>
                            <div style="width:100%;height:6px;background:#0f172a;border-radius:3px;overflow:hidden;">
                                <div style="width:${pct}%;height:100%;background:var(--success);"></div>
                            </div>
                        </div>
                    `;
                });
                html += '</div>';
                container.innerHTML = html;
            }
        } catch (e) {
            container.innerHTML = '<p style="color:var(--danger)">Error loading resources</p>';
        }
    }

    function refresh() {
        loadStats();
        loadRecentTasks();
        loadResourceSummary();
    }

    return { refresh };
})();