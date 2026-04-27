import {
  fetchPanelMembers,
  scheduleInterview,
  updateCandidateStatus,
} from "../services/hr-interview-service.js";

import { showMessage } from "../ui/hr-interview-ui.js";

/**
 * Adds click actions on HR candidate table buttons.
 *
 * @param {Function} reloadCandidates function to refresh candidate table
 */
export function attachHrInterviewActions(reloadCandidates) {
  const tableBody = document.getElementById("candidate-table-body");

  tableBody.addEventListener("click", async (event) => {
    const candidateId = event.target.dataset.id;

    if (!candidateId) {
      return;
    }

    if (event.target.classList.contains("btn-reject")) {
      await rejectCandidate(candidateId, reloadCandidates);
    }

    if (event.target.classList.contains("btn-l1")) {
      await openInterviewSchedule(
        candidateId,
        "L1_TECHNICAL",
        reloadCandidates,
      );
    }

    if (event.target.classList.contains("btn-l2")) {
      await openInterviewSchedule(
        candidateId,
        "L2_TECHNICAL",
        reloadCandidates,
      );
    }
  });
}

/**
 * Rejects candidate from HR dashboard.
 *
 * @param {string} candidateId candidate id
 * @param {Function} reloadCandidates function to refresh candidate table
 */
async function rejectCandidate(candidateId, reloadCandidates) {
  const confirmed = confirm("Reject this candidate?");

  if (!confirmed) {
    return;
  }

  await updateCandidateStatus({
    candidateId: Number(candidateId),
    decision: "REJECTED",
    remarks: "Rejected by HR",
  });

  showMessage("Candidate rejected successfully");
  reloadCandidates();
}

/**
 * Opens simple scheduling prompt and sends schedule request.
 *
 * @param {string} candidateId candidate id
 * @param {string} stage interview stage
 * @param {Function} reloadCandidates function to refresh candidate table
 */
async function openInterviewSchedule(candidateId, stage, reloadCandidates) {
  const panels = await fetchPanelMembers();

  if (!panels.length) {
    showMessage("No panel members found");
    return;
  }

  const panelMessage = buildPanelPromptMessage(panels);

  const selectedPanelInput = prompt(
    panelMessage + "\nEnter panel IDs comma separated. Example: 1,2",
  );

  if (!selectedPanelInput) {
    return;
  }

  const panelIds = selectedPanelInput
    .split(",")
    .map((id) => Number(id.trim()))
    .filter((id) => !Number.isNaN(id));

  if (panelIds.length < 1 || panelIds.length > 2) {
    showMessage("Please select minimum 1 and maximum 2 panels");
    return;
  }

  const interviewDateTime = prompt(
    "Enter interview date and time. Example: 2026-04-28T11:30",
  );

  if (!interviewDateTime) {
    return;
  }

  const focusAreas = prompt("Enter focus areas for panel") || "";

  await scheduleInterview({
    candidateId: Number(candidateId),
    stage: stage,
    panelIds: panelIds,
    interviewTime: interviewDateTime,
    focusAreas: focusAreas,
  });

  showMessage("Interview scheduled successfully");
  reloadCandidates();
}

/**
 * Builds readable panel list for prompt.
 *
 * @param {Array} panels panel members
 * @returns {string} prompt text
 */
function buildPanelPromptMessage(panels) {
  let message = "Available Panels:\n";

  panels.forEach((panel) => {
    message += `${panel.id} - ${panel.fullName} (${panel.email})\n`;
  });

  return message;
}
