const API = (() => {
    const BASE_URL = '';

    function getAuthToken() {
        return localStorage.getItem('jwt_token');
    }

    async function request(endpoint, options = {}) {
        const url = BASE_URL + endpoint;
        const headers = {
            'Content-Type': 'application/json',
            ...(options.headers || {})
        };

        const token = getAuthToken();
        if (token) {
            headers['Authorization'] = 'Bearer ' + token;
        }

        const config = {
            ...options,
            headers
        };

        try {
            const response = await fetch(url, config);

            if (response.status === 401) {
                console.warn('Session expired or unauthorized. Logging out.');
                Auth.logout();
                throw new Error('Session expired. Please log in again.');
            }

            const data = await response.json().catch(() => null);

            if (!response.ok) {
                const message = (data && data.message) || ('Request failed with status ' + response.status);
                throw new Error(message);
            }

            return data;
        } catch (error) {
            console.error('API Error (' + endpoint + '):', error);
            throw error;
        }
    }

    return {
        get: (endpoint) => request(endpoint, { method: 'GET' }),
        post: (endpoint, body) => request(endpoint, { method: 'POST', body: JSON.stringify(body) }),
        put: (endpoint, body) => request(endpoint, { method: 'PUT', body: JSON.stringify(body) }),
        patch: (endpoint, body) => request(endpoint, { method: 'PATCH', body: JSON.stringify(body) }),
        delete: (endpoint) => request(endpoint, { method: 'DELETE' })
    };
})();