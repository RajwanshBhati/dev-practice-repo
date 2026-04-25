import API_BASE from "../config/api.js";

function authHeaders() {
  const token = localStorage.getItem("accessToken");
  return {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  };
}

export const getJDs = () =>
  fetch(`${API_BASE}/hr/jd`, { headers: authHeaders() });

export const searchJDs = (params) =>
  fetch(`${API_BASE}/hr/jd/search?${params}`, { headers: authHeaders() });

export const createJD = (payload) =>
  fetch(`${API_BASE}/hr/jd`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify(payload),
  });

export const updateJD = (id, payload) =>
  fetch(`${API_BASE}/hr/jd/${id}`, {
    method: "PUT",
    headers: authHeaders(),
    body: JSON.stringify(payload),
  });

export const deleteJD = (id) =>
  fetch(`${API_BASE}/hr/jd/${id}`, {
    method: "DELETE",
    headers: authHeaders(),
  });

export const updateStatus = (id, status) =>
  fetch(`${API_BASE}/hr/jd/${id}/status?status=${status}`, {
    method: "PATCH",
    headers: authHeaders(),
  });
