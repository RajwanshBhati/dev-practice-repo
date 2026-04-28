const API_BASE = "http://localhost:8080/api";

function authHeaders() {
  const token = localStorage.getItem("accessToken");
  return {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  };
}

export async function fetchAllJDs() {
  const res = await fetch(`${API_BASE}/hr/jd`, {
    method: "GET",
    headers: authHeaders(),
  });

  if (!res.ok) {
    throw new Error(`Failed to load JDs. Status: ${res.status}`);
  }

  return res.json();
}

export async function searchJDs(title, status, jobType) {
  const params = new URLSearchParams();
  if (title) params.append("title", title);
  if (status) params.append("status", status);
  if (jobType) params.append("jobType", jobType);
  const res = await fetch(`${API_BASE}/hr/jd/search?${params}`, {
    headers: authHeaders(),
  });
  return res.json();
}

export async function createJD(payload) {
  const res = await fetch(`${API_BASE}/hr/jd`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify(payload),
  });
  return res.json();
}

export async function updateJD(id, payload) {
  const res = await fetch(`${API_BASE}/hr/jd/${id}`, {
    method: "PUT",
    headers: authHeaders(),
    body: JSON.stringify(payload),
  });
  return res.json();
}

export async function updateJDStatus(id, status) {
  const res = await fetch(`${API_BASE}/hr/jd/${id}/status?status=${status}`, {
    method: "PATCH",
    headers: authHeaders(),
  });
  return res.json();
}

export async function deleteJD(id) {
  const res = await fetch(`${API_BASE}/hr/jd/${id}`, {
    method: "DELETE",
    headers: authHeaders(),
  });
  return res.json();
}

export async function logoutAPI() {
  const token = localStorage.getItem("accessToken");

  try {
    await fetch(`${API_BASE}/auth/logout`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });
  } catch (err) {
  } finally {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("name");
    localStorage.removeItem("email");
    localStorage.removeItem("role");
    window.location.href = "/login.html";
  }
}
