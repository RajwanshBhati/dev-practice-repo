import {
  fetchPanelInterviews,
  submitFeedbackAPI,
} from "../services/panel-service.js";
import { renderPanelTable } from "../ui/panel-ui.js";

let currentInterviewId = null;

export async function loadPanelDashboard() {
  const data = await fetchPanelInterviews();

  window.openFeedback = (id) => {
    currentInterviewId = id;
    document.getElementById("feedback-modal").style.display = "block";
  };

  renderPanelTable(data, window.openFeedback);
}

export function closeFeedback() {
  document.getElementById("feedback-modal").style.display = "none";
}

export async function submitFeedback() {
  const payload = {
    interviewId: currentInterviewId,
    comments: document.getElementById("feedback-comments").value,
    strengths: document.getElementById("feedback-strength").value,
    weaknesses: document.getElementById("feedback-weakness").value,
    rating: document.getElementById("feedback-rating").value,
    decision: document.getElementById("feedback-status").value,
  };
  await submitFeedbackAPI(payload);

  alert("Feedback submitted");

  closeFeedback();
  loadPanelDashboard();
}
