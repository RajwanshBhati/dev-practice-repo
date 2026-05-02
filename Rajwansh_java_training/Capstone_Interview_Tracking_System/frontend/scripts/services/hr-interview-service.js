import {
  HR_CANDIDATE_API,
  getHrCandidateHeaders,
} from "../config/hr-interview-api.js";

export async function getAllCandidates() {
  const response = await fetch(HR_CANDIDATE_API.candidates, {
    method: "GET",
    headers: getHrCandidateHeaders(),
  });

  if (!response.ok) {
    throw new Error("Failed to load candidates");
  }

  return await response.json();
}

export async function getAllPanels() {
  const response = await fetch(HR_CANDIDATE_API.panels, {
    method: "GET",
    headers: getHrCandidateHeaders(),
  });

  if (!response.ok) {
    throw new Error("Failed to load panels");
  }

  return await response.json();
}

export async function changeCandidateStatus(payload) {
  const response = await fetch(HR_CANDIDATE_API.updateCandidateStatus, {
    method: "POST",
    headers: getHrCandidateHeaders(),
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    const errorText = await response.text();
    let message = "Failed to update status";

    try {
      const errorJson = JSON.parse(errorText);
      message = errorJson.message || errorJson.error || message;
    } catch {
      message = errorText || message;
    }

    message = message.replace("Something went wrong:", "").trim();

    throw new Error(message);
  }
}

export async function createInterviewSchedule(payload) {
  const response = await fetch(HR_CANDIDATE_API.scheduleInterview, {
    method: "POST",
    headers: getHrCandidateHeaders(),
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    const errorText = await response.text();
    let message = "Failed to schedule interview";

    try {
      const errorJson = JSON.parse(errorText);
      message = errorJson.message || errorJson.error || message;
    } catch {
      message = errorText || message;
    }

    message = message.replace("Something went wrong:", "").trim();

    throw new Error(message);
  }
}

export async function onboardCandidate(payload) {
  const response = await fetch(HR_CANDIDATE_API.onboardCandidate, {
    method: "POST",
    headers: getHrCandidateHeaders(),
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || "Failed to onboard candidate");
  }

  return await response.text();
}
