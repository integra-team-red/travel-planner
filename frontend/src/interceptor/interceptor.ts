const originalFetch = window.fetch

window.fetch = async (...args) => {
    const response = await originalFetch(...args)
    if (response.status === 401 || response.status === 403) {
        localStorage.removeItem("jwt");
        window.location.href = "/login"
    }
    return response
}
