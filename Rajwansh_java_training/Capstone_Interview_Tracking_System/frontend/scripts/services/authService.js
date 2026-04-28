import { apiFetch } from "../utils/apiClient.js";

// Login API
export function loginAPI(email, password) {
  return apiFetch("/auth/login", {
    method: "POST",
    body: JSON.stringify({ email, password }),
  });
}

// Activate API
export function activateAPI(payload) {
  return apiFetch("/auth/activate", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

// Logout API
export function logoutAPI() {
  return apiFetch("/auth/logout", {
    method: "POST",
  });
}
