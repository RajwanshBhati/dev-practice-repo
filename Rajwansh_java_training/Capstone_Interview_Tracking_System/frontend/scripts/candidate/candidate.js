import { API } from "../config/candidate-api.js";

export async function registerCandidate(payload) {
  const res = await fetch(API.CANDIDATE.REGISTER, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
  const text = await res.text();

  try {
    return JSON.parse(text);
  } catch {
    return { success: res.ok, message: text };
  }
}

export async function loginCandidate(payload) {
  const res = await fetch(API.CANDIDATE.LOGIN, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
  return res.json();
}

export async function applyCandidate(formData, token) {
  const res = await fetch(API.CANDIDATE.APPLY, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    body: formData,
  });

  const text = await res.text();

  let data;

  try {
    data = JSON.parse(text);
  } catch {
    data = { message: text };
  }

  if (!res.ok) {
    throw new Error(
      data?.message ||
        data?.error ||
        data?.statusMessage ||
        "Application submission failed",
    );
  }

  return data;
}
export async function getCandidateStatus(token) {
  const res = await fetch(API.CANDIDATE.STATUS, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
      Accept: "application/json",
    },
  });

  const text = await res.text();

  try {
    return JSON.parse(text);
  } catch {
    return { success: false, message: text };
  }
}

export async function logoutCandidate(token) {
  await fetch(API.CANDIDATE.LOGOUT, {
    method: "POST",
    headers: { Authorization: `Bearer ${token}` },
  });
}
