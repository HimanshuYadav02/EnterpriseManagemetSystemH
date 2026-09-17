const Auth = (() => {
    let currentUser = null;

    function init() {
        const savedUser = localStorage.getItem('current_user');
        const token = localStorage.getItem('jwt_token');
        if (savedUser && token) {
            try {
                currentUser = JSON.parse(savedUser);
            } catch (e) {
                logout();
            }
        }
    }

    function isAuthenticated() {
        return !!currentUser && !!localStorage.getItem('jwt_token');
    }

    function getUser() {
        return currentUser;
    }

    function hasRole(...roles) {
        if (!currentUser || !currentUser.role) return false;
        return roles.includes(currentUser.role);
    }

    async function login(username, password) {
        const res = await API.post('/api/auth/login', { username, password });
        if (res.success && res.data) {
            currentUser = res.data;
            localStorage.setItem('jwt_token', res.data.token);
            localStorage.setItem('current_user', JSON.stringify(res.data));
            return res.data;
        }
        throw new Error(res.message || 'Login failed');
    }

    async function register(userData) {
        return await API.post('/api/auth/register', userData);
    }

    function logout() {
        currentUser = null;
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('current_user');
        window.location.reload();
    }

    return {
        init,
        isAuthenticated,
        getUser,
        hasRole,
        login,
        register,
        logout
    };
})();