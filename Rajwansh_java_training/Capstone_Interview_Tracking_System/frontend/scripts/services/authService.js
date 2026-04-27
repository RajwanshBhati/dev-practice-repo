import API_BASE from "../config/api.js";

// This service will handle all authentication related API calls
export async function loginAPI(email, password) {
  const response = await fetch(`${API_BASE}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  });

  return await response.json();
}

// This can be used for account activation after registration
export async function activateAPI(payload) {
  const response = await fetch(`${API_BASE}/auth/activate`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });

  return await response.json();
}
