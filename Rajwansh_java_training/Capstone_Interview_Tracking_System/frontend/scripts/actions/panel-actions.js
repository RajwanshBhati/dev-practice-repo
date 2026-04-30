import {
  fetchPanelInterviews,
  submitFeedbackAPI,
} from "../services/panel-service.js";
import { renderPanelTable } from "../ui/panel-ui.js";
import { showToast } from "../utils/toast.js";

let currentInterviewId = null;

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

    window.openFeedback = (id) => {
      currentInterviewId = id;
      setFeedbackModalVisible(true);
    };

    renderPanelTable(data);
  } catch (err) {
    renderPanelTable([]);
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

  if (!comments || !strengths || !weaknesses || !rating || !decision) {
    showToast("Please fill all feedback fields", "error");
    return;
  }

  const payload = {
    interviewId: currentInterviewId,
    comments,
    strengths,
    weaknesses,
    rating: Number(rating),
    decision,
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
