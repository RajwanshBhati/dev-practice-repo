import { apiFetch } from "../utils/apiClient.js";

export function loginAPI(email, password) {
  return apiFetch("/auth/login", {
    method: "POST",
    body: JSON.stringify({ email, password }),
  });
}

export function activateAPI(payload) {
  return apiFetch("/auth/activate", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export function logoutAPI() {
  return apiFetch("/auth/logout", {
    method: "POST",
  });
}
