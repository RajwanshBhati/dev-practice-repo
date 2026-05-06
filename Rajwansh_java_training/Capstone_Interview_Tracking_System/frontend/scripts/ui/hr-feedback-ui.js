export function renderHRFeedback(data) {
  const container = document.getElementById("hr-feedback-content");

  if (!data || data.length === 0) {
    container.innerHTML = "<p>No feedback available</p>";
    return;
  }

  container.innerHTML = "";

  data.forEach((f) => {
    container.innerHTML += `
      <div style="border:1px solid #ccc; padding:10px; margin-bottom:10px;">
        <h4>Panel: ${f.panelName}</h4>
        <p><strong>Email:</strong> ${f.panelEmail}</p>
        <p><strong>Stage:</strong> ${f.stage}</p>
        <p><strong>Comments:</strong> ${f.comments}</p>
        <p><strong>Strengths:</strong> ${f.strengths}</p>
        <p><strong>Weaknesses:</strong> ${f.weaknesses}</p>
        <p><strong>Rating:</strong> ${f.rating}</p>
        <p><strong>Decision:</strong> ${f.decision}</p>
      </div>
    `;
  });
}
