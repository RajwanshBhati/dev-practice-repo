import {
  changeCandidateStatus,
  createInterviewSchedule,
  getAllPanels,
} from "../services/hr-interview-service.js";

import {
  getCandidateList,
  applyCandidateFilters,
  showCandidateMessage,
} from "../ui/hr-interview-ui.js";

let scheduleEventsAttached = false;

/**
 * Handles interview scheduling for L1 and L2.
 */
async function handleSchedule(candidateId, stage, reloadCandidates) {
  if (!candidateId || Number.isNaN(Number(candidateId))) {
    showCandidateMessage(
      "Candidate id missing. Check Candidate object in console.",
    );
    return;
  }
  const panels = await getAllPanels();

  if (!panels.length) {
    showCandidateMessage("No panel members available");
    return;
  }

  const modal = document.getElementById("schedule-modal");
  const candidateInput = document.getElementById("schedule-candidate-id");
  const stageInput = document.getElementById("schedule-stage");
  const panelSelect = document.getElementById("panel-select");

  candidateInput.value = candidateId;
  stageInput.value = stage;

  panelSelect.innerHTML = "";

  panels.forEach((panel) => {
    console.log("Panel object:", panel);

    const panelId = panel.id || panel.panelId || panel.userId || panel.panel_id;
    const panelName = panel.fullName || panel.name || "Panel";
    const panelEmail = panel.email || panel.emailId || panel.userEmail || "-";

    if (!panelId) {
      return;
    }

    const option = document.createElement("option");

    option.value = panelId;
    option.textContent = `${panelName} (${panelEmail})`;

    panelSelect.appendChild(option);
  });
  modal.style.display = "flex";

  attachScheduleModalEvents(reloadCandidates);
}

/**
 * Attaches all action button handlers for HR candidate table.
 */
export function attachCandidateActions(reloadCandidates) {
  const tableBody = document.getElementById("hr-candidate-table-body");

  tableBody.addEventListener("click", async (event) => {
    const candidateId = event.target.dataset.id;

    if (!candidateId) {
      return;
    }

    if (event.target.classList.contains("reject-btn")) {
      await handleReject(candidateId, reloadCandidates);
    }

    if (event.target.classList.contains("l1-btn")) {
      await handleSchedule(candidateId, "L1_TECHNICAL", reloadCandidates);
    }

    if (event.target.classList.contains("l2-btn")) {
      await handleSchedule(candidateId, "L2_TECHNICAL", reloadCandidates);
    }

    if (event.target.classList.contains("hr-btn")) {
      await handleStageUpdate(candidateId, "HR_ROUND", reloadCandidates);
    }

    if (event.target.classList.contains("select-btn")) {
      await handleFinalSelect(candidateId, reloadCandidates);
    }
  });
}

/**
 * Rejects a candidate.
 */
async function handleReject(candidateId, reloadCandidates) {
  const confirmed = confirm("Are you sure you want to reject this candidate?");

  if (!confirmed) {
    return;
  }

  await changeCandidateStatus({
    candidateId: Number(candidateId),
    decision: "REJECTED",
    remarks: "Rejected by HR",
  });

  showCandidateMessage("Candidate rejected");
  reloadCandidates();
}

/**
 * Moves candidate to HR round.
 */
async function handleStageUpdate(candidateId, stage, reloadCandidates) {
  await changeCandidateStatus({
    candidateId: Number(candidateId),
    stage: stage,
    remarks: "Moved to HR round",
  });

  showCandidateMessage("Candidate moved to HR round");
  reloadCandidates();
}

/**
 * Final selection of candidate.
 */
async function handleFinalSelect(candidateId, reloadCandidates) {
  const confirmed = confirm("Mark this candidate as selected?");

  if (!confirmed) {
    return;
  }

  await changeCandidateStatus({
    candidateId: Number(candidateId),
    decision: "SELECTED",
    remarks: "Selected by HR",
  });

  showCandidateMessage("Candidate selected");
  reloadCandidates();
}

/**
 * Builds readable panel list
 */
function buildPanelListText(panels) {
  console.log("Panels from backend:", panels);

  let text = "Available Panels:\n";

  panels.forEach((panel) => {
    const panelId = panel.id || panel.panelId;
    text += `${panelId} - ${panel.fullName} (${panel.email})\n`;
  });

  return text;
}

function attachScheduleModalEvents(reloadCandidates) {
  if (scheduleEventsAttached) {
    return;
  }

  scheduleEventsAttached = true;

  document
    .getElementById("schedule-modal-close")
    .addEventListener("click", closeScheduleModal);

  document
    .getElementById("schedule-cancel-btn")
    .addEventListener("click", closeScheduleModal);

  document
    .getElementById("schedule-submit-btn")
    .addEventListener("click", async () => {
      await submitScheduleForm(reloadCandidates);
    });
}

async function submitScheduleForm(reloadCandidates) {
  const candidateId = document.getElementById("schedule-candidate-id").value;
  const stage = document.getElementById("schedule-stage").value;
  const date = document.getElementById("interview-date").value;
  const time = document.getElementById("interview-time").value;
  const focusAreas = document.getElementById("focus-areas").value;
  const panelSelect = document.getElementById("panel-select");

  const panelIds = Array.from(panelSelect.selectedOptions)
    .map((option) => Number(option.value))
    .filter((id) => Number.isInteger(id) && id > 0);

  if (!date || !time) {
    showCandidateMessage("Please select interview date and time");
    return;
  }

  if (panelIds.length < 1 || panelIds.length > 2) {
    showCandidateMessage("Please select minimum 1 and maximum 2 panels");
    return;
  }
  console.log("Selected panel ids:", panelIds);
  await createInterviewSchedule({
    candidateId: Number(candidateId),
    stage: stage,
    panelIds: panelIds,
    interviewTime: `${date}T${time}`,
    focusAreas: focusAreas,
  });

  closeScheduleModal();
  showCandidateMessage("Interview scheduled successfully");
  reloadCandidates();
}

function closeScheduleModal() {
  document.getElementById("schedule-modal").style.display = "none";
  document.getElementById("interview-date").value = "";
  document.getElementById("interview-time").value = "";
  document.getElementById("focus-areas").value = "";
  document.getElementById("panel-select").innerHTML = "";
}
