/**
 * API endpoints used in HR candidate workflow.
 */
export const HR_CANDIDATE_API = {
  candidates: "http://localhost:8080/api/hr/candidates",
  panels: "http://localhost:8080/api/v1/panel/list",
  scheduleInterview: "http://localhost:8080/api/interview/schedule",
  updateCandidateStatus: "http://localhost:8080/api/interview/status",
};

/**
 * Returns headers required for HR protected APIs.
 *
 * @returns {Object} request headers
 */
export function getHrCandidateHeaders() {
  return {
    "Content-Type": "application/json",
    Authorization: "Bearer " + localStorage.getItem("accessToken"),
  };
}
