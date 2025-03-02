/* eslint-disable no-implicit-globals */
/**
 * Checks if the provided string represents an integer.
 * @param str - The string to check.
 * @returns `true` if the string represents an integer, otherwise `false`.
 */
export function isInteger(str: string | null): boolean {
    if (str === null) return true;
    const num = Number(str);
    return Number.isInteger(num);
}

/**
 * Checks if the provided string represents a double (floating-point number).
 * @param str - The string to check.
 * @returns `true` if the string represents a double, otherwise `false`.
 */
export function isDouble(str: string | null): boolean {
    if (str === null) return true;
    const num = Number(str);
    return !Number.isNaN(num);
}

/**
 * Checks if the provided number is the maximum safe integer in JavaScript.
 * @param str - The number to check.
 * @param limit - The number to limit.
 * @returns `true` if the number is equal to `Number.MAX_SAFE_INTEGER`, otherwise `false`.
 */
export function isMaxValue(str: string | null, limit: number): boolean {
    if (str === null) return true;
    const num = Number(str);
    return num <= limit;
}

/**
 * Checks if the provided number is the maximum safe integer in JavaScript.
 * @param str - The number to check.
 * @param limit - The number to limit.
 * @returns `true` if the number is equal to `Number.MAX_SAFE_INTEGER`, otherwise `false`.
 */
export function isMinValue(str: string | null, limit: number): boolean {
    if (str === null) return true;
    const num = Number(str);
    return num >= limit;
}

/**
 * Checks if the provided string is a valid date in the format dd.mm.yyyy.
 * @param str - The string to check.
 * @returns true if the string matches the dd.mm.yyyy format and is a valid date, otherwise false.
 */
export function isValidDate(str: string | null): boolean {
    if (!str) return true;
    const regex = /^\d{2}\.\d{2}\.\d{4}$/;
    if (!regex.test(str)) return false;

    const [day, month, year] = str.split('.').map(Number);
    const date = new Date(year, month - 1, day); // Month is 0-indexed in JavaScript Date

    return date.getFullYear() === year && date.getMonth() === month - 1 && date.getDate() === day;
}
