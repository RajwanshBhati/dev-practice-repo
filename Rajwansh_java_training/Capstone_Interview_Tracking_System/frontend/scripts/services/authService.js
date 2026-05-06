import { apiFetch } from "../utils/apiClient.js";
import { encodePasswordBase64 } from "../utils/password.js";

export function loginAPI(email, password) {
  const encodedPassword = encodePasswordBase64(password);

  return apiFetch("/auth/login", {
    method: "POST",
    body: JSON.stringify({
      email,
      password: encodedPassword,
    }),
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
