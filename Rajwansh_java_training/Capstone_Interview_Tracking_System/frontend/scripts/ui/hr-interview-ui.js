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
  const tableBody = document.getElementById("hr-candidate-table-body");
  const emptyState = document.getElementById("candidate-empty-state");
  const loader = document.getElementById("candidate-table-loader");

  loader.style.display = "none";
  tableBody.innerHTML = "";

  if (!candidates || candidates.length === 0) {
    emptyState.style.display = "block";
    return;
  }

  emptyState.style.display = "none";

  candidates.forEach((candidate) => {
    console.log("Candidate object:", candidate);
    const candidateId =
      candidate.id ||
      candidate.candidateId ||
      candidate.candidate_id ||
      candidate.candidateUserId ||
      candidate.candidateProfileId ||
      candidate.profileId;

    const row = document.createElement("tr");

    row.innerHTML = `
    <td>${candidate.name || "-"}</td>
    <td>${candidate.email || "-"}</td>
    <td>${candidate.mobileNumber || "-"}</td>
    <td>${candidate.jdTitle || candidate.jobTitle || "-"}</td>
    <td>${formatExperience(candidate)}</td>
    <td>${candidate.currentCompany || "-"}</td>
    <td>${formatCtc(candidate)}</td>
    <td>${candidate.preferredLocation || "-"}</td>
    <td>
      <span class="status-pill">
        ${candidate.status || "-"}
      </span>
    </td>
    <td>
      ${renderResumeLink(candidate.resumeUrl)}
    </td>
    <td>
      <div class="candidate-actions">
        <button class="action-btn reject-btn" data-id="${candidateId}">
          Reject
        </button>

        <button class="action-btn l1-btn" data-id="${candidateId}">
          L1
        </button>

        <button class="action-btn l2-btn" data-id="${candidateId}">
          L2
        </button>

        <button class="action-btn hr-btn" data-id="${candidateId}">
          HR Round
        </button>

        <button class="action-btn select-btn" data-id="${candidateId}">
          Select
        </button>
      </div>
    </td>
  `;

    tableBody.appendChild(row);
  });
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
  const current = candidate.currentCtc || "-";
  const expected = candidate.expectedCtc || "-";

  return `${current} / ${expected}`;
}

/**
 * Renders resume link if candidate uploaded resume.
 */
function renderResumeLink(resumeUrl) {
  if (!resumeUrl) {
    return "-";
  }

  return `<a href="${resumeUrl}" target="_blank">View</a>`;
}
