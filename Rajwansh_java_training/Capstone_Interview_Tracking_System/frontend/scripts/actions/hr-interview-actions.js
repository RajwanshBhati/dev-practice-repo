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
  const dateInput = document.getElementById("interview-date");

  if (dateInput) {
    dateInput.min = new Date().toISOString().split("T")[0];
  }

  attachScheduleModalEvents(reloadCandidates);
}

/**
 * Attaches all action button handlers for HR candidate table.
 */
let isActionBound = false;

export function attachCandidateActions(reloadCandidates) {
  const container = document.getElementById("candidate-container");

  if (!container || isActionBound) return;

  isActionBound = true;

  container.addEventListener("change", (e) => {
    const select = e.target.closest(".candidate-action-select");
    if (!select) return;

    const id = select.dataset.id;
    const action = select.value;

    if (action === "screening") {
      handleScreening(id, reloadCandidates);
    }

    if (action === "reject") {
      handleReject(id, reloadCandidates);
    }

    if (action === "l1") {
      handleSchedule(id, "L1_TECHNICAL", reloadCandidates);
    }

    if (action === "l2") {
      handleSchedule(id, "L2_TECHNICAL", reloadCandidates);
    }

    if (action === "hr") {
      handleSchedule(id, "HR_ROUND", reloadCandidates);
    }

    if (action === "select") {
      handleFinalSelect(id, reloadCandidates);
    }
    select.value = "";
  });
}
async function handleScreening(candidateId, reloadCandidates) {
  await changeCandidateStatus({
    candidateId: Number(candidateId),
    stage: "SCREENING",
    remarks: "Moved to screening",
  });

  showCandidateMessage("Candidate moved to screening");
  reloadCandidates();
}

async function handleReject(candidateId, reloadCandidates) {
  await changeCandidateStatus({
    candidateId: Number(candidateId),
    decision: "REJECTED",
    remarks: "Rejected by HR",
  });

  showCandidateMessage("Candidate rejected successfully");
  reloadCandidates();
}

async function handleFinalSelect(candidateId, reloadCandidates) {
  await changeCandidateStatus({
    candidateId: Number(candidateId),
    decision: "SELECTED",
    remarks: "Selected by HR",
  });

  showCandidateMessage("Candidate selected successfully");
  reloadCandidates();
}
/**
 * Builds readable panel list
 */
function buildPanelListText(panels) {
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
  const selectedDateTime = new Date(`${date}T${time}`);
  const currentDateTime = new Date();

  if (selectedDateTime < currentDateTime) {
    showCandidateMessage("Interview date and time cannot be in the past");
    return;
  }
  if (panelIds.length < 1 || panelIds.length > 2) {
    showCandidateMessage("Please select minimum 1 and maximum 2 panels");
    return;
  }
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

function closeHRFeedback() {
  document.getElementById("hr-feedback-modal").style.display = "none";
}
window.closeHRFeedback = closeHRFeedback;
