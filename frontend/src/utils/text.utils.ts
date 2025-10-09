export function toCapitalCaseFromAllCaps(value: string): string {
    return value
        .toLowerCase()
        .split('_')
        .map(word => word.charAt(0).toUpperCase() + word.slice(1))
        .join(' ');
}

export function toCamelCase(value: string): string {
    return value.replace(/^\w|[A-Z]|\b\w/g, function (word, index) {
        return index == 0 ? word.toLowerCase() : word.toUpperCase();
    }).replace(/\s+/g, '');
}