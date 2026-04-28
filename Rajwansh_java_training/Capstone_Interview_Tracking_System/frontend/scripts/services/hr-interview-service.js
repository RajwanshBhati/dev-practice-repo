import {
  HR_CANDIDATE_API,
  getHrCandidateHeaders,
} from "../config/hr-interview-api.js";

/**
 * Fetch all candidates applied for JDs.
 */
export async function getAllCandidates() {
  const response = await fetch(HR_CANDIDATE_API.candidates, {
    method: "GET",
    headers: getHrCandidateHeaders(),
  });

  if (!response.ok) {
    throw new Error("Failed to load candidates");
  }

  return response.json();
}

/**
 * Fetch all available panel members.
 */
export async function getAllPanels() {
  const response = await fetch(HR_CANDIDATE_API.panels, {
    method: "GET",
    headers: getHrCandidateHeaders(),
  });

  if (!response.ok) {
    throw new Error("Failed to load panels");
  }

  return response.json();
}

/**
 * Update candidate status (Reject / Stage change).
 */
export async function changeCandidateStatus(payload) {
  const response = await fetch(HR_CANDIDATE_API.updateCandidateStatus, {
    method: "POST",
    headers: getHrCandidateHeaders(),
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error("Failed to update status");
  }
}

/**
 * Schedule interview for candidate.
 */
export async function createInterviewSchedule(payload) {
  const response = await fetch(HR_CANDIDATE_API.scheduleInterview, {
    method: "POST",
    headers: getHrCandidateHeaders(),
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error("Failed to schedule interview");
  }
}
