import { STORAGE_KEYS } from "../constants/storageKeys.js";

export function getToken() {
  return localStorage.getItem(STORAGE_KEYS.ACCESS_TOKEN);
}

export function getName() {
  const name = localStorage.getItem(STORAGE_KEYS.NAME);

  return name && name.trim() ? name.trim() : "Candidate";
}
