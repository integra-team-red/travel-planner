import type {ConfirmationOptions} from "primevue/confirmationoptions";

export function promptConfirm(
    confirmService: {
        require: (option: ConfirmationOptions) => void;
        close: () => void;
    },
    acceptButtonCallback: () => void,
    rejectButtonCallback: () => void = () => {},
    header: string = 'Confirmation needed',
    message: string = 'Are you sure you want to continue this operation?',
    acceptButtonMessage: string = 'Yes',
    rejectButtonMessage: string = 'Cancel'
) {
    confirmService.require({
        message: message,
        header: header,
        icon: 'pi pi-exclamation-triangle',
        rejectProps: {
            label: rejectButtonMessage,
            severity: 'danger',
        },
        acceptProps: {
            label: acceptButtonMessage,
            severity: 'info'
        },
        accept: acceptButtonCallback,
        reject: rejectButtonCallback
    });
}