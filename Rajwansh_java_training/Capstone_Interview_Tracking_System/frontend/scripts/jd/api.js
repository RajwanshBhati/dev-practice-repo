const API_BASE = "http://localhost:8080/api";

function authHeaders() {
  const token = localStorage.getItem("accessToken");

  // Base headers
  const headers = {
    "Content-Type": "application/json",
  };

  // Check if token exists and is not invalid
  if (token && token !== "undefined" && token !== "null") {
    headers.Authorization = `Bearer ${token}`;
  }

  return headers;
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

  const text = await res.text();
  const data = text ? JSON.parse(text) : null;

  if (!res.ok) {
    throw new Error(data?.message || "Update failed");
  }

  return data;
}

export async function updateJDStatus(id, status) {
  const res = await fetch(
    `${API_BASE}/hr/jd/${id}/status?status=${encodeURIComponent(status)}`,
    {
      method: "PATCH",
      headers: authHeaders(),
    },
  );

  const text = await res.text();
  const data = text ? JSON.parse(text) : null;

  if (!res.ok) {
    throw new Error(data?.message || "Status update failed");
  }

  return data;
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
