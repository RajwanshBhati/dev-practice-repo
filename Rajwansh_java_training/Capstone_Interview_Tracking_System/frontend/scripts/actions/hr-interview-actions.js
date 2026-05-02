import {
  changeCandidateStatus,
  createInterviewSchedule,
  getAllPanels,
} from "../services/hr-interview-service.js";

import { showCandidateMessage } from "../ui/hr-interview-ui.js";
import { showToast } from "../utils/toast.js";
let scheduleEventsAttached = false;
let dropdownEventsAttached = false;
async function handleSchedule(candidateId, stage, reloadCandidates) {
  document.getElementById("schedule-candidate-id").value = candidateId;
  document.getElementById("schedule-stage").value = stage;

  const isHrRound = stage === "HR_ROUND";

  const panelBlock = document.getElementById("panel-selection-block");
  const panelList = document.getElementById("panel-checkbox-list");
  const toggle = document.getElementById("panel-dropdown-toggle");
  const menu = document.getElementById("panel-dropdown-menu");
  const focusLabel = document.getElementById("focus-area-label");

  if (panelList) panelList.innerHTML = "";
  if (toggle) toggle.textContent = "Select Panel Members";
  if (menu) menu.classList.add("hidden");

  if (focusLabel) {
    focusLabel.textContent = isHrRound
      ? "HR Comments / Discussion Points"
      : "Focus Areas";
  }

  if (panelBlock) {
    panelBlock.style.display = isHrRound ? "none" : "block";
  }

  if (!isHrRound) {
    const panels = await getAllPanels();

    if (!panels || panels.length === 0) {
      showCandidateMessage("No panel members available", "error");
      return;
    }

    if (!panelList || !toggle || !menu) {
      showCandidateMessage("Panel selector not found", "error");
      return;
    }

    panels.forEach((panel) => {
      const panelId = panel.id || panel.panelId || panel.userId;
      if (!panelId) return;

      const name = panel.fullName || panel.name || "Panel Member";
      const email = panel.email || panel.emailId || "-";

      const item = document.createElement("label");
      item.className = "panel-checkbox-item";

      item.innerHTML = `
        <input type="checkbox" value="${panelId}" />
        <div class="panel-checkbox-text">
          <div class="panel-name">${name}</div>
          <div class="panel-email">${email}</div>
        </div>
      `;

      panelList.appendChild(item);
    });

    attachPanelDropdownEvents();
  }

  const dateInput = document.getElementById("interview-date");
  dateInput.min = new Date().toISOString().split("T")[0];

  document.getElementById("schedule-modal").style.display = "flex";

  attachScheduleModalEvents(reloadCandidates);
}

export function attachCandidateActions(reloadCandidates) {
  const container = document.getElementById("candidate-container");
  if (!container) return;

  if (container.dataset.bound === "true") return;
  container.dataset.bound = "true";

  container.addEventListener("change", async (e) => {
    const select = e.target.closest(".candidate-action-select");
    if (!select) return;

    const id = select.dataset.id;
    const action = select.value;
    select.value = "";

    try {
      if (action === "screening") {
        await changeCandidateStatus({
          candidateId: Number(id),
          stage: "SCREENING",
          remarks: "Moved to screening",
        });
        showCandidateMessage("Candidate moved to screening");
        reloadCandidates();
      }

      if (action === "reject") {
        await changeCandidateStatus({
          candidateId: Number(id),
          decision: "REJECTED",
          remarks: "Rejected by HR",
        });
        showCandidateMessage("Candidate rejected");
        reloadCandidates();
      }

      if (action === "select") {
        await changeCandidateStatus({
          candidateId: Number(id),
          decision: "SELECTED",
          remarks: "Selected by HR",
        });
        showCandidateMessage("Candidate selected");
        reloadCandidates();
      }

      if (action === "l1") {
        await handleSchedule(id, "L1_TECHNICAL", reloadCandidates);
      }

      if (action === "l2") {
        await handleSchedule(id, "L2_TECHNICAL", reloadCandidates);
      }

      if (action === "hr") {
        await handleSchedule(id, "HR_ROUND", reloadCandidates);
      }
    } catch (err) {
      showCandidateMessage(err.message || "Action failed");
    }
  });
}

function attachScheduleModalEvents(reloadCandidates) {
  if (scheduleEventsAttached) return;
  scheduleEventsAttached = true;

  document
    .getElementById("schedule-modal-close")
    ?.addEventListener("click", closeScheduleModal);

  document
    .getElementById("schedule-cancel-btn")
    ?.addEventListener("click", closeScheduleModal);

  document
    .getElementById("schedule-submit-btn")
    ?.addEventListener("click", async () => {
      await submitScheduleForm(reloadCandidates);
    });
}

function attachPanelDropdownEvents() {
  if (dropdownEventsAttached) return;
  dropdownEventsAttached = true;

  const toggle = document.getElementById("panel-dropdown-toggle");
  const menu = document.getElementById("panel-dropdown-menu");
  const panelList = document.getElementById("panel-checkbox-list");

  if (!toggle || !menu || !panelList) return;

  toggle.addEventListener("click", (e) => {
    e.stopPropagation();
    menu.classList.toggle("hidden");
  });

  menu.addEventListener("click", (e) => {
    e.stopPropagation();
  });

  panelList.addEventListener("change", () => {
    const checked = Array.from(panelList.querySelectorAll("input:checked"));

    if (checked.length > 2) {
      checked[checked.length - 1].checked = false;
      showCandidateMessage("You can select maximum 2 panel members");
      return;
    }

    updatePanelDropdownText();
  });

  document.addEventListener("click", () => {
    menu.classList.add("hidden");
  });
}

function updatePanelDropdownText() {
  const toggle = document.getElementById("panel-dropdown-toggle");
  const panelList = document.getElementById("panel-checkbox-list");

  if (!toggle || !panelList) return;

  const selectedNames = Array.from(panelList.querySelectorAll("input:checked"))
    .map((input) => {
      return (
        input
          .closest(".panel-checkbox-item")
          ?.querySelector(".panel-name")
          ?.textContent.trim() || ""
      );
    })
    .filter(Boolean);

  toggle.textContent =
    selectedNames.length > 0
      ? selectedNames.join(", ")
      : "Select Panel Members";
}

async function submitScheduleForm(reloadCandidates) {
  const candidateId = document.getElementById("schedule-candidate-id").value;
  const stage = document.getElementById("schedule-stage").value;
  const date = document.getElementById("interview-date").value;
  const time = document.getElementById("interview-time").value;
  const focusAreas = document.getElementById("focus-areas").value.trim();

  const panelIds = Array.from(
    document.querySelectorAll("#panel-checkbox-list input:checked"),
  )
    .map((el) => Number(el.value))
    .filter((id) => id > 0);

  if (!date || !time) {
    showToast("Please select interview date and time", "error");
    return;
  }

  const isHrRound = stage === "HR_ROUND";

  if (!isHrRound && (panelIds.length < 1 || panelIds.length > 2)) {
    showToast("Please select minimum 1 and maximum 2 panel members", "error");
    return;
  }

  if (!focusAreas) {
    showToast(
      isHrRound ? "Please enter HR comments" : "Please enter focus areas",
      "error",
    );
    return;
  }

  const btn = document.getElementById("schedule-submit-btn");
  const oldText = btn?.textContent || "Schedule Interview";

  if (btn) {
    btn.disabled = true;
    btn.textContent = "Scheduling...";
  }
  try {
    await createInterviewSchedule({
      candidateId: Number(candidateId),
      stage,
      panelIds: isHrRound ? [] : panelIds,
      interviewTime: `${date}T${time}:00`,
      focusAreas,
    });
    closeScheduleModal();
    showCandidateMessage("Interview scheduled successfully");
    reloadCandidates();
  } catch (err) {
    showToast(err.message || "Interview schedule failed", "error");
  } finally {
    if (btn) {
      btn.disabled = false;
      btn.textContent = oldText;
    }
  }
}

function closeScheduleModal() {
  document.getElementById("schedule-modal").style.display = "none";
  document.getElementById("interview-date").value = "";
  document.getElementById("interview-time").value = "";
  document.getElementById("focus-areas").value = "";

  const panelList = document.getElementById("panel-checkbox-list");
  const toggle = document.getElementById("panel-dropdown-toggle");
  const menu = document.getElementById("panel-dropdown-menu");

  if (panelList) panelList.innerHTML = "";
  if (toggle) toggle.textContent = "Select Panel Members";
  if (menu) menu.classList.add("hidden");
}
