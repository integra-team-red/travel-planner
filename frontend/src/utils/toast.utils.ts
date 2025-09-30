import {type ToastServiceMethods} from "primevue";
type ToastSeverity = 'success' | 'info' | 'warn' | 'error';

export function showToast(severity: ToastSeverity, summary: string, toast: ToastServiceMethods) {
    toast.add({
        severity,
        summary: summary,
        life: 5000
    });
}