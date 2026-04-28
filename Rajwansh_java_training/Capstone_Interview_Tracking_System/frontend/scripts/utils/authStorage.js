import { STORAGE_KEYS } from "../constants/storageKeys.js";

export function storeAuthData(data) {
  if (!data || typeof data !== "object") {
    return;
  }

  const { accessToken, refreshToken, role, name, email } = data;

  if (accessToken) {
    localStorage.setItem(STORAGE_KEYS.ACCESS_TOKEN, accessToken);
  }

  if (refreshToken) {
    localStorage.setItem(STORAGE_KEYS.REFRESH_TOKEN, refreshToken);
  }

  if (role) {
    localStorage.setItem(STORAGE_KEYS.ROLE, role);
  }

  if (name && name.trim()) {
    localStorage.setItem(STORAGE_KEYS.NAME, name.trim());
  }

  if (email) {
    localStorage.setItem(STORAGE_KEYS.EMAIL, email);
  }
}

export function getToken() {
  return localStorage.getItem(STORAGE_KEYS.ACCESS_TOKEN);
}

export function getRefreshToken() {
  return localStorage.getItem(STORAGE_KEYS.REFRESH_TOKEN);
}

export function getAuthRole() {
  return localStorage.getItem(STORAGE_KEYS.ROLE);
}

export function getName() {
  return localStorage.getItem(STORAGE_KEYS.NAME)?.trim() || "Candidate";
}

export function getEmail() {
  return localStorage.getItem(STORAGE_KEYS.EMAIL);
}

export function isLoggedIn() {
  const token = localStorage.getItem(STORAGE_KEYS.ACCESS_TOKEN);

  if (
    !token ||
    token === "null" ||
    token === "undefined" ||
    token.trim() === ""
  ) {
    return false;
  }

  return true;
}
export function clearAuthData() {
  localStorage.removeItem(STORAGE_KEYS.ACCESS_TOKEN);
  localStorage.removeItem(STORAGE_KEYS.REFRESH_TOKEN);
  localStorage.removeItem(STORAGE_KEYS.ROLE);
  localStorage.removeItem(STORAGE_KEYS.NAME);
  localStorage.removeItem(STORAGE_KEYS.EMAIL);
}
