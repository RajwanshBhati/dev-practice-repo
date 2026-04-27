import { API } from "../config/candidate-api.js";

export async function applyCandidate(formData, token) {
  const res = await fetch(API.CANDIDATE.APPLY, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    body: formData,
  });
  return res.json();
}

export async function getCandidateStatus(token) {
  const res = await fetch(API.CANDIDATE.STATUS, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return res.json();
}

export async function logoutCandidate(token) {
  return fetch(API.AUTH.LOGOUT, {
    headers: { Authorization: `Bearer ${token}` },
  });
}
