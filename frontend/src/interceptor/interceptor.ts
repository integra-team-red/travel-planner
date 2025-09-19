const { fetch: originalFetch } = window;

window.fetch = async (...args) => {
    const resource = args[0];
    let config = args[1];

    // request interceptor
    const token = localStorage.getItem("jwt");
    if (token) {
        config = {
            ...config,
            headers: {
                ...config?.headers,
                Authorization: `Bearer ${token}`,
            }
        };
    }

    const response = await originalFetch(resource, config);

    // response interceptor
    if (response.status === 401) {
        localStorage.removeItem("jwt");
        window.location.href = "/login";
    }

    return response;
};
