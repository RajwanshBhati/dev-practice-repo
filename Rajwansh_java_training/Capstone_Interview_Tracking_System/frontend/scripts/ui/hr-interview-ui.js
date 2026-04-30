/**
 * Holds candidate data currently loaded on HR screen.
 */
let candidateList = [];

/**
 * Stores candidate list for filtering and rendering.
 */
export function setCandidateList(candidates) {
  candidateList = candidates || [];
}

/**
 * Returns all candidates from local state.
 */
export function getCandidateList() {
  return candidateList;
}

/**
 * Renders complete candidate table for HR.
 */
export function renderCandidateTable(candidates) {
  const container = document.getElementById("candidate-container");
  const emptyState = document.getElementById("candidate-empty-state");
  const loader = document.getElementById("candidate-table-loader");

  loader.style.display = "none";
  container.innerHTML = "";

  if (!candidates || candidates.length === 0) {
    emptyState.style.display = "block";
    return;
  }

  emptyState.style.display = "none";

  container.innerHTML = candidates
    .map((candidate) => {
      const candidateId =
        candidate.id || candidate.candidateId || candidate.candidateUserId;

      return `
  <div class="candidate-card">
    <div class="candidate-header">
      <div class="candidate-avatar">
        ${candidate.name?.charAt(0)?.toUpperCase() || "C"}
      </div>

      <div>
        <h3>${candidate.name || "-"}</h3>
        <p>${candidate.email || "-"}</p>
        <p>${candidate.mobileNumber || "-"}</p>
      </div>
    </div>

    <div class="candidate-detail-grid">
      <div><span>Job</span><strong>${candidate.jdTitle || candidate.jobTitle || "-"}</strong></div>
      <div><span>Company</span><strong>${candidate.currentCompany || "-"}</strong></div>
      <div><span>Location</span><strong>${candidate.preferredLocation || "-"}</strong></div>
      <div><span>Source</span><strong>${candidate.source || "-"}</strong></div>
      <div><span>Current CTC</span><strong>${candidate.currentCtc ?? "-"} LPA</strong></div>
      <div><span>Expected CTC</span><strong>${candidate.expectedCtc ?? "-"} LPA</strong></div>
    </div>

    <div class="experience-box">
      <div>
        <span>Total Experience</span>
        <strong>${candidate.totalExperience ?? "-"} yrs</strong>
      </div>
      <div>
        <span>Relevant Experience</span>
        <strong>${candidate.relevantExperience ?? "-"} yrs</strong>
      </div>
    </div>

    <div class="candidate-footer">
      <span class="status ${String(candidate.status || "").toLowerCase()}">
        ${candidate.status || "-"}
      </span>

      ${renderResumeLink(candidate.resumeUrl)}

      <select class="candidate-action-select" data-id="${candidateId}">
        <option value="">Choose Action</option>
        <option value="screening">Screening</option>
        <option value="l1">Schedule L1</option>
        <option value="l2">Schedule L2</option>
        <option value="hr">Schedule HR</option>
        <option value="reject">Reject</option>
        <option value="select">Select</option>
      </select>
    </div>
  </div>
`;
    })
    .join("");
}
/**
 * Filters candidates using search text and stage filter.
 */
export function applyCandidateFilters() {
  const searchInput = document.getElementById("candidate-search-input");
  const stageFilter = document.getElementById("candidate-stage-filter");

  const searchText = (searchInput.value || "").toLowerCase();
  const selectedStage = stageFilter.value;

  const filteredCandidates = candidateList.filter((candidate) => {
    const mergedText = [
      candidate.name,
      candidate.email,
      candidate.mobileNumber,
      candidate.currentCompany,
      candidate.jdTitle,
      candidate.jobTitle,
      candidate.preferredLocation,
      candidate.status,
    ]
      .join(" ")
      .toLowerCase();

    const searchMatched = mergedText.includes(searchText);
    const stageMatched = !selectedStage || candidate.status === selectedStage;

    return searchMatched && stageMatched;
  });

  renderCandidateTable(filteredCandidates);
}

/**
 * Shows loader while candidate data is loading.
 */
export function showCandidateLoader() {
  const loader = document.getElementById("candidate-table-loader");
  const emptyState = document.getElementById("candidate-empty-state");

  loader.style.display = "block";
  emptyState.style.display = "none";
}

/**
 * Shows normal alert message.
 */
export function showCandidateMessage(message) {
  alert(message);
}

/**
 * Builds readable experience text.
 */
function formatExperience(candidate) {
  const total = candidate.totalExperience || "-";
  const relevant = candidate.relevantExperience || "-";

  return `${total} total / ${relevant} relevant`;
}

/**
 * Builds readable CTC text.
 */
function formatCtc(candidate) {
  const current = candidate.currentCtc ?? "-";
  const expected = candidate.expectedCtc ?? "-";

  return `
    <div class="ctc-box">
      <div><strong>Currect CTC:</strong> ${current}</div>
      <div><strong>Expected CTC:</strong> ${expected}</div>
    </div>
  `;
}

/**
 * Renders resume link if candidate uploaded resume.
 */
function renderResumeLink(resumeUrl) {
  if (!resumeUrl) {
    return "-";
  }

  const finalUrl = resumeUrl.startsWith("http")
    ? resumeUrl
    : `http://localhost:8080${resumeUrl}`;

  return `<a href="${finalUrl}" target="_blank" class="resume-link">View Resume</a>`;
}
