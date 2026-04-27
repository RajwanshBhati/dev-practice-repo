import {
  loadPanelDashboard,
  submitFeedback,
} from "../actions/panel-actions.js";

document.addEventListener("DOMContentLoaded", () => {
  loadPanelDashboard();

  document
    .getElementById("submit-feedback-btn")
    .addEventListener("click", submitFeedback);
});
