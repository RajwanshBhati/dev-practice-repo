import { fetchHrCandidates } from "../services/hr-interview-service.js";
import { attachHrInterviewActions } from "../actions/hr-interview-actions.js";
import { renderCandidateTable, showMessage } from "../ui/hr-interview-ui.js";

/**
 * Loads candidate data and renders HR interview table.
 */
async function loadCandidates() {
  try {
    const candidates = await fetchHrCandidates();

    renderCandidateTable(candidates);
  } catch (error) {
    console.error("Candidate loading failed", error);
    showMessage("Unable to load candidates");
  }
}

/**
 * Starts HR interview workflow page.
 */
function initHrInterviewPage() {
  loadCandidates();
  attachHrInterviewActions(loadCandidates);
}

initHrInterviewPage();
