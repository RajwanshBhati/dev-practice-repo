import {
  fetchPanelInterviews,
  submitFeedbackAPI,
} from "../services/panel-service.js";
import { renderPanelTable } from "../ui/panel-ui.js";
import { showToast } from "../utils/toast.js";

let currentInterviewId = null;
let panelInterviewData = [];

export function getPanelInterviewData() {
  return panelInterviewData;
}

function setFeedbackModalVisible(isVisible) {
  const modal = document.getElementById("feedback-modal");
  if (!modal) return;

  modal.style.display = isVisible ? "flex" : "none";
}

function resetFeedbackForm() {
  document.getElementById("feedback-form")?.reset();
  currentInterviewId = null;
}

export async function loadPanelDashboard() {
  try {
    const data = await fetchPanelInterviews();
    panelInterviewData = Array.isArray(data)
      ? [...data].sort((a, b) => {
          const aTime = new Date(
            `${a.interviewDate}T${a.interviewTime || "00:00"}`,
          ).getTime();
          const bTime = new Date(
            `${b.interviewDate}T${b.interviewTime || "00:00"}`,
          ).getTime();
          return bTime - aTime;
        })
      : [];

    window.openFeedback = (id) => {
      currentInterviewId = id;
      setFeedbackModalVisible(true);
    };

    renderPanelTable(panelInterviewData, "all");
  } catch (err) {
    panelInterviewData = [];
    renderPanelTable([], "all");
    showToast(err.message || "Failed to load interviews", "error");
  }
}

export function closeFeedback() {
  setFeedbackModalVisible(false);
  resetFeedbackForm();
}

export async function submitFeedback() {
  if (!currentInterviewId) {
    showToast("Interview id missing", "error");
    return;
  }

  const comments = document.getElementById("feedback-comments")?.value.trim();
  const strengths = document.getElementById("feedback-strength")?.value.trim();
  const weaknesses = document.getElementById("feedback-weakness")?.value.trim();
  const rating = document.getElementById("feedback-rating")?.value;
  const decision = document.getElementById("feedback-status")?.value;
  const areasCovered = document
    .getElementById("feedback-areas-covered")
    ?.value.trim();

  if (
    !comments ||
    !strengths ||
    !weaknesses ||
    !rating ||
    !decision ||
    !areasCovered
  ) {
    showToast("Please fill all feedback fields", "error");
    return;
  }

  const payload = {
    interviewId: Number(currentInterviewId),
    rating: Number(rating),
    decision,
    strengths,
    weaknesses,
    comments,
    areasCovered,
  };

  try {
    await submitFeedbackAPI(payload);

    showToast("Feedback submitted", "success");
    closeFeedback();
    await loadPanelDashboard();
  } catch (err) {
    showToast(err.message || "Feedback failed", "error");
  }
}
window.submitFeedback = submitFeedback;
window.closeFeedback = closeFeedback;
