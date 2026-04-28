const API_BASE = "http://localhost:8080/api";

function authHeaders() {
  return {
    "Content-Type": "application/json",
    Authorization: "Bearer " + localStorage.getItem("accessToken"),
  };
}

/**
 * Fetches logged-in candidate current profile/status.
 */
export async function fetchCandidateProgress() {
  const response = await fetch(`${API_BASE}/candidates/status`, {
    method: "GET",
    headers: authHeaders(),
  });

  if (!response.ok) {
    throw new Error("Failed to load candidate status");
  }

  return response.json();
}

/**
 * Fetches scheduled interviews for a candidate.
 */
export async function fetchCandidateInterviews(candidateId) {
  const response = await fetch(
    `${API_BASE}/interview/candidate/${candidateId}`,
    {
      method: "GET",
      headers: authHeaders(),
    },
  );

  if (!response.ok) {
    throw new Error("Failed to load candidate interviews");
  }

  return response.json();
}
