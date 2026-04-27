import {
  HR_INTERVIEW_API,
  getHrAuthHeaders,
} from "../config/hr-interview-api.js";

/**
 * Fetches all candidates for HR dashboard.
 */
export async function fetchHrCandidates() {
  const response = await fetch(HR_INTERVIEW_API.candidates, {
    method: "GET",
    headers: getHrAuthHeaders(),
  });

  if (!response.ok) {
    throw new Error("Unable to load candidates");
  }

  return response.json();
}

/**
 * Fetches panel members for interview assignment.
 *
 * @returns {Promise<Array>} panel list
 */
export async function fetchPanelMembers() {
  const response = await fetch(HR_INTERVIEW_API.panels, {
    method: "GET",
    headers: getHrAuthHeaders(),
  });

  if (!response.ok) {
    throw new Error("Unable to load panel members");
  }

  return response.json();
}

/**
 * Sends interview schedule details to backend.
 */
export async function scheduleInterview(payload) {
  const response = await fetch(HR_INTERVIEW_API.scheduleInterview, {
    method: "POST",
    headers: getHrAuthHeaders(),
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    throw new Error("Unable to schedule interview");
  }
}

/**
 * Updates candidate stage or final decision.
 */
export async function updateCandidateStatus(payload) {
  const response = await fetch(HR_INTERVIEW_API.updateCandidateStatus, {
    method: "POST",
    headers: getHrAuthHeaders(),
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    throw new Error("Unable to update candidate status");
  }
}
