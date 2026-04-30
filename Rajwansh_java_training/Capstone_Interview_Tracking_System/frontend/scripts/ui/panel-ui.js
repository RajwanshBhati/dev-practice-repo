const API_ORIGIN = "http://localhost:8080";

function escapeHtml(value) {
  return String(value ?? "-")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function buildResumeUrl(resumeUrl) {
  if (!resumeUrl) return "";
  return resumeUrl.startsWith("http") ? resumeUrl : `${API_ORIGIN}${resumeUrl}`;
}

export function renderPanelTable(data = []) {
  const tbody = document.getElementById("panel-table-body");
  if (!tbody) return;

  tbody.innerHTML = "";

  if (!Array.isArray(data) || data.length === 0) {
    tbody.innerHTML = `
      <tr>
        <td colspan="9" class="panel-empty-state">
          <div>No interview assigned yet.</div>
          <small>Assigned interviews will appear here after HR schedules them.</small>
        </td>
      </tr>
    `;
    return;
  }

  data.forEach((i) => {
    const resumeUrl = buildResumeUrl(i.resumeUrl);

    const resumeHtml = resumeUrl
      ? `<a href="${escapeHtml(resumeUrl)}" target="_blank" rel="noopener noreferrer" class="resume-link">View Resume</a>`
      : `<span class="muted-text">No Resume</span>`;

    tbody.innerHTML += `
      <tr>
        <td>${escapeHtml(i.candidateName)}</td>
        <td>${escapeHtml(i.candidateEmail)}</td>
        <td>${escapeHtml(i.candidateMobileNumber)}</td>
        <td><span class="stage-chip">${escapeHtml(i.stage)}</span></td>
        <td>${escapeHtml(i.interviewDate)}</td>
        <td>${escapeHtml(i.interviewTime)}</td>
        <td>${escapeHtml(i.focusArea)}</td>
        <td>${resumeHtml}</td>
        <td>
          ${
            i.feedbackSubmitted
              ? `<span class="submitted-chip">Submitted</span>`
              : `<button type="button" class="btn-feedback" onclick="window.openFeedback(${Number(i.interviewId)})">Give Feedback</button>`
          }
        </td>
      </tr>
    `;
  });
}
