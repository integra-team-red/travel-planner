import type {CityDTO} from "../../typescript-client";
import type {POIDTO} from "../../typescript-client";

export function downloadCityArrayToUserDevice(cities: Array<CityDTO>, fileName: string = 'Cities.json') {
    const fileURL = window.URL.createObjectURL(new File([JSON.stringify(cities)], fileName));
    const a = document.createElement('a');
    a.href = fileURL;
    a.download = fileName;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(fileURL);
}

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