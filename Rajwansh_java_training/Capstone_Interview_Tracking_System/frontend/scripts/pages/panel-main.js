import {
  loadPanelDashboard,
  submitFeedback,
} from "../actions/panel-actions.js";

document.addEventListener("DOMContentLoaded", async () => {
  await loadPanelDashboard();

  const form = document.getElementById("feedback-form");

  if (form) {
    form.addEventListener("submit", async (e) => {
      e.preventDefault();
      await submitFeedback();
    });
  }
});
