let jdList = [];
let jdEditHandler = null;
let jdDeleteHandler = null;
let renderHandler = null;
let filtersAttached = false;

export function setJdFilterData(data, renderTable, onEdit, onDelete) {
  jdList = data || [];
  renderHandler = renderTable;
  jdEditHandler = onEdit;
  jdDeleteHandler = onDelete;
}

export function applyJdFilters() {
  const searchInput = document.getElementById("jd-search-input");
  const statusFilter = document.getElementById("jd-status-filter");

  const searchText = (searchInput?.value || "").toLowerCase().trim();
  const selectedStatus = statusFilter?.value || "";

  const filteredData = jdList.filter((jd) => {
    const searchableText = [
      jd.jobTitle,
      jd.location,
      jd.jobType,
      jd.status,
      jd.minExperience,
      jd.maxExperience,
      ...(jd.skillsRequired || []),
    ]
      .join(" ")
      .toLowerCase();

    const searchMatched = !searchText || searchableText.includes(searchText);
    const statusMatched = !selectedStatus || jd.status === selectedStatus;

    return searchMatched && statusMatched;
  });

  if (renderHandler) {
    renderHandler(filteredData, jdEditHandler, jdDeleteHandler, false);
  }
}

export function attachJdFilters() {
  if (filtersAttached) return;

  const searchInput = document.getElementById("jd-search-input");
  const statusFilter = document.getElementById("jd-status-filter");

  if (searchInput) {
    searchInput.addEventListener("input", applyJdFilters);
  }

  if (statusFilter) {
    statusFilter.addEventListener("change", applyJdFilters);
  }

  filtersAttached = true;
}
