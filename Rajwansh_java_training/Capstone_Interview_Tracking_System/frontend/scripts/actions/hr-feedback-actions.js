import { fetchCandidateFeedback } from "../services/hr-feedback-service.js";
import { renderHRFeedback } from "../ui/hr-feedback-ui.js";

export async function handleFeedbackClick(candidateId) {
  const data = await fetchCandidateFeedback(candidateId);

  renderHRFeedback(data);

  document.getElementById("hr-feedback-modal").style.display = "block";
}

export function closeHRFeedback() {
  document.getElementById("hr-feedback-modal").style.display = "none";
}
