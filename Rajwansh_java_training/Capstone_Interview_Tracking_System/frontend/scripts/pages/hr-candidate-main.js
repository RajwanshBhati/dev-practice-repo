import { getAllCandidates } from "../services/hr-interview-service.js";

import {
  applyCandidateFilters,
  renderCandidateTable,
  setCandidateList,
  showCandidateLoader,
  showCandidateMessage,
} from "../ui/hr-interview-ui.js";

import { attachCandidateActions } from "../actions/hr-interview-actions.js";

/**
 * Loads candidates and refreshes HR candidate section.
 */
async function loadHrCandidates() {
  try {
    showCandidateLoader();

    const candidates = await getAllCandidates();

    setCandidateList(candidates);
    renderCandidateTable(candidates);
  } catch (error) {
    console.error("Candidate loading failed", error);
    showCandidateMessage("Unable to load candidates");
  }
}

/**
 * Connects filter inputs with candidate table.
 */
function attachCandidateFilters() {
  const searchInput = document.getElementById("candidate-search-input");
  const stageFilter = document.getElementById("candidate-stage-filter");

  searchInput.addEventListener("input", applyCandidateFilters);
  stageFilter.addEventListener("change", applyCandidateFilters);
}

/**
 * Starts HR candidate workflow.
 */
export function initHrCandidateSection() {
  loadHrCandidates();
  attachCandidateFilters();
  attachCandidateActions(loadHrCandidates);
}
