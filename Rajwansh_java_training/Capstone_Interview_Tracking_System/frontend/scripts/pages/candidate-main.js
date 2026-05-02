import {
  fetchCandidateProgress,
  fetchCandidateInterviews,
} from "../services/candidate-register-service.js";

import {
  renderCandidateProgress,
  renderCurrentStatus,
  renderFinalStatus,
  renderInterviewDetails,
  setCandidateLoader,
  showCandidateError,
} from "../ui/candidate-ui.js";

/**
 * Loads candidate dashboard data.
 */
async function loadCandidateDashboard() {
  try {
    setCandidateLoader(true);

    const statusData = await fetchCandidateProgress();

    const candidateId =
      statusData.id || statusData.candidateId || statusData.candidateProfileId;

    const status = statusData.status || "PROFILING";

    const section = document.getElementById("stageSection");
    if (section && status !== "NOT_APPLIED") {
      section.style.display = "block";
    }

    renderCurrentStatus(status);
    renderCandidateProgress(status);
    renderFinalStatus(status);

    if (candidateId) {
      const interviews = await fetchCandidateInterviews(candidateId);
      renderInterviewDetails(interviews);
    } else {
      renderInterviewDetails([]);
    }
  } catch (error) {
    showCandidateError("Unable to load candidate dashboard");
  } finally {
    setCandidateLoader(false);
  }
}

document.addEventListener("DOMContentLoaded", loadCandidateDashboard);
