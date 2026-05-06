import { showToast } from "../utils/toast.js";
let candidateList = [];
let currentPage = 1;
const pageSize = 5;

export function setCandidateList(candidates) {
  candidateList = candidates || [];
}

export function getCandidateList() {
  return candidateList;
}

export function renderCandidateTable(candidates) {
  const totalPages = Math.ceil((candidates || []).length / pageSize);
  if (currentPage > totalPages) currentPage = 1;

  const startIndex = (currentPage - 1) * pageSize;
  const paginatedCandidates = (candidates || []).slice(
    startIndex,
    startIndex + pageSize,
  );
  const container = document.getElementById("candidate-container");
  const emptyState = document.getElementById("candidate-empty-state");
  const loader = document.getElementById("candidate-table-loader");

  if (!container) return;

  if (loader) loader.style.display = "none";
  container.innerHTML = "";

  if (!paginatedCandidates || paginatedCandidates.length === 0) {
    if (emptyState) emptyState.style.display = "block";
    return;
  }

  if (emptyState) emptyState.style.display = "none";

  container.innerHTML = paginatedCandidates
    .map((candidate) => {
      const candidateId =
        candidate.id || candidate.candidateId || candidate.candidateUserId;

      const status = String(candidate.status || "PROFILING").toLowerCase();

      return `
        <div class="candidate-card">
          <div class="candidate-profile">
            <div class="candidate-avatar">
              ${escapeHtml(candidate.name?.charAt(0)?.toUpperCase() || "C")}
            </div>

            <div>
              <h3 class="candidate-name">${escapeHtml(candidate.name || "-")}</h3>
              <p class="candidate-contact">✉ ${escapeHtml(candidate.email || "-")}</p>
              <p class="candidate-contact">☎ ${escapeHtml(candidate.mobileNumber || "-")}</p>
            </div>
          </div>

          <div class="candidate-detail-grid">
            ${renderInfoBox("Job", candidate.jdTitle || candidate.jobTitle || "-")}
            ${renderInfoBox("Company", candidate.currentCompany || "-")}
            ${renderInfoBox("Location", candidate.preferredLocation || "-")}
            ${renderInfoBox("Source", candidate.source || "-")}
            ${renderInfoBox("Current CTC", formatLpa(candidate.currentCtc))}
            ${renderInfoBox("Expected CTC", formatLpa(candidate.expectedCtc))}
            ${renderInfoBox("Total Experience", formatYears(candidate.totalExperience))}
            ${renderInfoBox("Relevant Experience", formatYears(candidate.relevantExperience))}
          </div>

          <div class="candidate-actions">
            <span class="status ${status}">
              ${formatStage(candidate.status)}
            </span>

            ${renderResumeLink(candidate.resumeUrl)}

            <select class="candidate-action-select" data-id="${candidateId}">
              <option value="">Move Stage</option>
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

  renderCandidatePagination(candidates.length);
}

export function applyCandidateFilters() {
  const searchInput = document.getElementById("candidate-search-input");
  const stageFilter = document.getElementById("candidate-stage-filter");

  const searchText = (searchInput?.value || "").toLowerCase();
  const selectedStage = stageFilter?.value || "";

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

function renderCandidatePagination(totalItems) {
  const pagination = document.getElementById("candidate-pagination");
  if (!pagination) return;

  const totalPages = Math.ceil(totalItems / pageSize);

  if (totalPages <= 1) {
    pagination.innerHTML = "";
    return;
  }

  pagination.innerHTML = `
    <button ${currentPage === 1 ? "disabled" : ""} id="candidate-prev-page">
      Prev
    </button>

    <span>Page ${currentPage} of ${totalPages}</span>

    <button ${currentPage === totalPages ? "disabled" : ""} id="candidate-next-page">
      Next
    </button>
  `;

  document
    .getElementById("candidate-prev-page")
    ?.addEventListener("click", () => {
      currentPage--;
      renderCandidateTable(getCandidateList());
    });

  document
    .getElementById("candidate-next-page")
    ?.addEventListener("click", () => {
      currentPage++;
      renderCandidateTable(getCandidateList());
    });
}

export function showCandidateLoader() {
  const loader = document.getElementById("candidate-table-loader");
  const emptyState = document.getElementById("candidate-empty-state");

  if (loader) loader.style.display = "block";
  if (emptyState) emptyState.style.display = "none";
}

export function showCandidateMessage(message, type = "success") {
  showToast(message, type);
}

function renderInfoBox(label, value) {
  return `
    <div class="info-box">
      <span>${escapeHtml(label)}</span>
      <strong>${escapeHtml(value)}</strong>
    </div>
  `;
}

function renderResumeLink(resumeUrl) {
  if (!resumeUrl) {
    return `<span class="resume-link">No Resume</span>`;
  }

  const finalUrl = resumeUrl.startsWith("http")
    ? resumeUrl
    : `http://localhost:8080${resumeUrl}`;

  return `
    <a href="${escapeHtml(finalUrl)}" target="_blank" class="resume-link">
      View Resume
    </a>
  `;
}

function formatLpa(value) {
  return value === null || value === undefined || value === ""
    ? "-"
    : `${value} LPA`;
}

function formatYears(value) {
  return value === null || value === undefined || value === ""
    ? "-"
    : `${value} yrs`;
}

function formatStage(stage) {
  if (!stage) return "New";

  return String(stage)
    .replaceAll("_", " ")
    .toLowerCase()
    .replace(/\b\w/g, (char) => char.toUpperCase());
}

function escapeHtml(value) {
  return String(value ?? "-")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}
