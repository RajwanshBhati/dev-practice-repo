import {
  formatSalary,
  formatDate,
  jobTypeLabel,
  jobTypeBadgeClass,
  statusBadgeClass,
} from "./utils.js";

export function updateStats(data) {
  const set = (id, val) => {
    const el = document.getElementById(id);
    if (el) el.textContent = val;
  };
  set("stat-total", data.length);
  set("stat-active", data.filter((j) => j.status === "ACTIVE").length);
  set("stat-inactive", data.filter((j) => j.status === "INACTIVE").length);
  set("stat-closed", data.filter((j) => j.status === "CLOSED").length);
}

export function renderTable(data, onEdit, onDelete) {
  const tbody = document.getElementById("jd-table-body");
  const emptyState = document.getElementById("empty-state");
  const table = document.getElementById("jd-table");
  const loader = document.getElementById("table-loader");

  if (loader) loader.style.display = "none";

  if (!data || data.length === 0) {
    if (table) table.style.display = "none";
    if (emptyState) emptyState.style.display = "block";
    return;
  }

  if (table) table.style.display = "table";
  if (emptyState) emptyState.style.display = "none";
  if (!tbody) return;

  tbody.innerHTML = "";

  data.forEach((jd) => {
    const skillsHtml = (jd.skillsRequired || [])
      .slice(0, 3)
      .map((s) => `<span class="skill-tag">${s}</span>`)
      .join("");

    const moreCount = (jd.skillsRequired || []).length - 3;
    const moreHtml =
      moreCount > 0 ? `<span class="skill-tag-more">+${moreCount}</span>` : "";

    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>
        <div class="jd-title">${jd.jobTitle}</div>
        <div class="jd-date">${formatDate(jd.createdAt)}</div>
      </td>
      <td>${jd.location}</td>
      <td>
        <span class="badge ${jobTypeBadgeClass(jd.jobType)}">
          ${jobTypeLabel(jd.jobType)}
        </span>
      </td>
      <td>${jd.minExperience}–${jd.maxExperience} yrs</td>
      <td>${formatSalary(jd.minSalary)} – ${formatSalary(jd.maxSalary)}</td>
      <td><div class="skills-cell">${skillsHtml}${moreHtml}</div></td>
      <td>
        <span class="badge ${statusBadgeClass(jd.status)}">${jd.status}</span>
      </td>
      <td>
        <div class="action-btns">
          <button class="action-btn edit-btn" data-id="${jd.id}" title="Edit">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor"
                 stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
              <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
            </svg>
          </button>
          <button class="action-btn delete delete-btn"
                  data-id="${jd.id}"
                  data-title="${jd.jobTitle.replace(/"/g, "&quot;")}"
                  title="Delete">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor"
                 stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <polyline points="3 6 5 6 21 6"/>
              <path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6"/>
              <path d="M10 11v6"/><path d="M14 11v6"/>
              <path d="M9 6V4h6v2"/>
            </svg>
          </button>
        </div>
      </td>
    `;

    // Button events
    tr.querySelector(".edit-btn").addEventListener("click", () =>
      onEdit(jd.id),
    );
    tr.querySelector(".delete-btn").addEventListener("click", () =>
      onDelete(jd.id, jd.jobTitle),
    );

    tbody.appendChild(tr);
  });
}

export function showTableLoader() {
  const loader = document.getElementById("table-loader");
  const table = document.getElementById("jd-table");
  const emptyState = document.getElementById("empty-state");
  if (loader) loader.style.display = "block";
  if (table) table.style.display = "none";
  if (emptyState) emptyState.style.display = "none";
}
