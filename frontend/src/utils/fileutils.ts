import type {POIDTO} from "../../typescript-client";
export function downloadPOIArrayToUserDevice(pois: Array<POIDTO>, fileName: string = 'Points of Interest.json') {
    const fileURL = window.URL.createObjectURL(new File([JSON.stringify(pois)], fileName));
    const a = document.createElement('a');
    a.href = fileURL;
    a.download = fileName;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(fileURL);
}