export let isLoading: boolean = false;

export function showLoading() {
    isLoading = true;
}

export function hideLoading() {
    isLoading = false;
}