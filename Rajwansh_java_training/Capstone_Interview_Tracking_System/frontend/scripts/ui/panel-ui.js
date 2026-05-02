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

function getJobTitle(i) {
  return (
    i.jobTitle ||
    i.jdTitle ||
    i.position ||
    i.appliedPosition ||
    i.jobDescriptionTitle ||
    "-"
  );
}
function canSubmitFeedback(interviewDate, interviewTime) {
  if (!interviewDate || !interviewTime) return false;

  const interviewDateTime = new Date(`${interviewDate}T${interviewTime}`);
  return new Date() >= interviewDateTime;
}
export function renderPanelTable(data = [], filter = "all") {
  const tbody = document.getElementById("panel-table-body");
  if (!tbody) return;

  tbody.innerHTML = "";

  const rows = Array.isArray(data) ? data : [];

  const filteredRows = rows.filter((item) => {
    if (filter === "submitted") return item.feedbackSubmitted;
    if (filter === "pending") return !item.feedbackSubmitted;
    return true;
  });

  if (filteredRows.length === 0) {
    let title = "No interview assigned";
    let subtitle =
      "Assigned interviews will appear here after HR schedules them.";

    if (filter === "submitted") {
      title = "No submitted feedback";
      subtitle = "Submitted feedback records will appear here.";
    }

    if (filter === "pending") {
      title = "No pending feedback";
      subtitle = "Pending interviews will appear here.";
    }

    tbody.innerHTML = `
      <tr>
        <td colspan="10">
          <div class="panel-empty-state">
            <div class="empty-icon">📋</div>
            <h3>${title}</h3>
            <p>${subtitle}</p>
          </div>
        </td>
      </tr>
    `;
    return;
  }

  filteredRows.forEach((i) => {
    const resumeUrl = buildResumeUrl(i.resumeUrl);

    const resumeHtml = resumeUrl
      ? `<a href="${escapeHtml(resumeUrl)}" target="_blank" rel="noopener noreferrer" class="resume-link">View Resume</a>`
      : `<span class="muted-text">No Resume</span>`;

    tbody.innerHTML += `
      <tr>
        <td>${escapeHtml(i.candidateName)}</td>
        <td>${escapeHtml(i.candidateEmail)}</td>
        <td>${escapeHtml(i.candidateMobileNumber)}</td>
        <td>${escapeHtml(getJobTitle(i))}</td>
        <td><span class="stage-chip">${escapeHtml(i.stage)}</span></td>
        <td>${escapeHtml(i.interviewDate)}</td>
        <td>${escapeHtml(i.interviewTime)}</td>
        <td>${escapeHtml(i.focusArea || i.focusAreas)}</td>
        <td>${resumeHtml}</td>
        <td>
          ${
            i.feedbackSubmitted
              ? `<span class="submitted-chip">Submitted</span>`
              : canSubmitFeedback(i.interviewDate, i.interviewTime)
                ? `<button type="button" class="btn-feedback" onclick="window.openFeedback(${Number(i.interviewId)})">Give Feedback</button>`
                : `<button type="button" class="btn-feedback" disabled title="Feedback can be submitted after interview starts">Not Started</button>`
          }
        </td>
      </tr>
    `;
  });
}
