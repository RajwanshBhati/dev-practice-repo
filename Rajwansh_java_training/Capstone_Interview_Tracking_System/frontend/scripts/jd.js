import {
  getJDs,
  createJD,
  updateJD,
  deleteJD,
  updateStatus,
} from "./services/jdService.js";

import { formatSalary, formatDate } from "./utils/format.js";
import { showFieldError, clearErrors } from "./utils/validation.js";
import {
  getSkills,
  addSkill,
  removeSkill,
  resetSkills,
} from "./services/skillService.js";

/* ─── STATE ─── */
let allJDs = [];
let editingId = null;
let deletingId = null;

/* ─── FETCH ─── */
async function fetchJDs() {
  const res = await getJDs();
  const data = await res.json();

  if (data.success) {
    allJDs = data.data;
    renderTable(allJDs);
    updateStats(allJDs);
  }
}

/* ─── RENDER ─── */
function renderTable(data) {
  const tbody = document.getElementById("jd-table-body");

  tbody.innerHTML = data
    .map(
      (jd) => `
    <tr>
      <td>${jd.jobTitle}</td>
      <td>${jd.location}</td>
      <td>${formatSalary(jd.minSalary)}</td>
      <td>${formatDate(jd.createdAt)}</td>
    </tr>
  `,
    )
    .join("");
}

function updateStats(data) {
  document.getElementById("stat-total").textContent = data.length;

  document.getElementById("stat-active").textContent = data.filter(
    (j) => j.status === "ACTIVE",
  ).length;

  document.getElementById("stat-inactive").textContent = data.filter(
    (j) => j.status === "INACTIVE",
  ).length;

  document.getElementById("stat-closed").textContent = data.filter(
    (j) => j.status === "CLOSED",
  ).length;
}

/* ─── INIT ─── */
document.addEventListener("DOMContentLoaded", () => {
  fetchJDs();
});
