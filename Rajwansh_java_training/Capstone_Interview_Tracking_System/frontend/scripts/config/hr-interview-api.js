/**
 * API endpoints used by HR interview workflow.
 */
export const HR_INTERVIEW_API = {
  candidates: "http://localhost:8080/api/hr/candidates",
  panels: "http://localhost:8080/api/v1/panel/all",
  scheduleInterview: "http://localhost:8080/api/interview/schedule",
  updateCandidateStatus: "http://localhost:8080/api/interview/status",
};

/**
 * Returns authorization headers for protected HR APIs.
 *
 * @returns {Object} headers with JWT token
 */
export function getHrAuthHeaders() {
  return {
    "Content-Type": "application/json",
    Authorization: "Bearer " + localStorage.getItem("accessToken"),
  };
}
