import { logoutCandidate } from "../candidate/candidate.js";
import {
  getToken,
  getName,
  extractJdId,
  appliedJdIds,
  allJDs,
  loadJDs,
  loadCandidateStatus,
  handleApplySubmit,
  applyFilters,
} from "../actions/candidate-actions.js";

let selectedJD = null;
let selectedApplyJD = null;

// Stage tracker config
const STAGE_IDS = ["st-PROFILING", "st-L1", "st-L2", "st-HR"];
const STAGE_MAP = {
  NOT_APPLIED: -1,
  PROFILING: 0,
  SCREENING: 0,
  L1_TECHNICAL: 1,
  L2_TECHNICAL: 2,
  HR_ROUND: 3,
  REJECTED: 3,
  SELECTED: 3,
};

// Formatting helpers
export function formatSalary(val) {
  if (!val) return "—";
  const num = parseFloat(val);
  if (num >= 100000) return `₹${(num / 100000).toFixed(1)}L`;
  return `₹${num.toLocaleString("en-IN")}`;
}

export function jobTypeLabel(type) {
  const map = {
    FULL_TIME: "Full Time",
    CONTRACT: "Contract",
    REMOTE: "Remote",
  };
  return map[type] || type;
}

export function jobTypeBadgeClass(type) {
  const map = {
    FULL_TIME: "badge-full-time",
    CONTRACT: "badge-contract",
    REMOTE: "badge-remote",
  };
  return map[type] || "";
}

// Stage tracker renderer
export function renderStageTracker(stage, status) {
  const section = document.getElementById("stageSection");
  if (section) section.style.display = "block";

  const activeIndex = STAGE_MAP[stage] ?? 0;
  const isRejected = status === "REJECTED";

  STAGE_IDS.forEach((id, i) => {
    const el = document.getElementById(id);
    if (!el) return;
    el.classList.remove("active", "done", "rejected");

    if (isRejected && i === activeIndex) {
      el.classList.add("rejected");
    } else if (i < activeIndex) {
      el.classList.add("done");
    } else if (i === activeIndex) {
      el.classList.add("active");
    }
  });
}

// Banner
export function showBanner(msg, type) {
  const el = document.getElementById("appBanner");
  if (!el) return;
  el.textContent = msg;
  el.className = `app-banner ${type}`;
  el.classList.remove("hidden");
}

// Form message helpers
export function showFormMsg(id, msg, type) {
  const el = document.getElementById(id);
  if (!el) return;
  el.textContent = msg;
  el.className = `alert alert-${type}`;
  el.classList.remove("hidden");
}

export function hideFormMessages() {
  document.getElementById("formError")?.classList.add("hidden");
  document.getElementById("formSuccess")?.classList.add("hidden");
}

// JD grid renderer
export function renderGrid(data, appliedJdIds = []) {
  const grid = document.getElementById("jdGrid");
  if (!grid) return;

  grid.innerHTML = "";

  data.forEach((jd) => {
    const hasAlreadyApplied = appliedJdIds.includes(jd.id);

    const skillsHtml = (jd.skillsRequired || [])
      .slice(0, 3)
      .map((s) => `<span class="skill-chip">${s}</span>`)
      .join("");

    const more = (jd.skillsRequired || []).length - 3;

    const moreHtml =
      more > 0 ? `<span class="skill-chip-more">+${more}</span>` : "";

    const card = document.createElement("div");
    card.className = "jd-card";

    card.innerHTML = `
      <div class="jd-card-header">
        <div class="jd-card-title">${jd.jobTitle}</div>
        <span class="badge ${jobTypeBadgeClass(jd.jobType)}">
          ${jobTypeLabel(jd.jobType)}
        </span>
      </div>

      <div class="jd-card-meta">
        <span>${jd.location}</span>
        <span>${jd.minExperience}–${jd.maxExperience} yrs</span>
      </div>

      <div class="jd-card-skills">
        ${skillsHtml}${moreHtml}
      </div>
      <div class="jd-card-footer">
        <span class="jd-card-salary">
          ${formatSalary(jd.minSalary)} – ${formatSalary(jd.maxSalary)}
        </span>

        <button class="apply-btn"
          ${hasAlreadyApplied ? "disabled" : ""}
          data-id="${jd.id}">
          ${hasAlreadyApplied ? "Applied" : "Apply Now"}
        </button>
      </div>
    `;
    const applyBtn = card.querySelector(".apply-btn");

    if (applyBtn && !hasAlreadyApplied) {
      applyBtn.addEventListener("click", (e) => {
        e.stopPropagation();
        openDetailModal(jd);
      });
    }
    card.addEventListener("click", () => openDetailModal(jd));

    grid.appendChild(card);
  });
}

// Detail modal
function openDetailModal(jd) {
  selectedJD = jd;

  document.getElementById("detailTitle").textContent = jd.jobTitle;
  document.getElementById("detailType").textContent = jobTypeLabel(jd.jobType);
  document.getElementById("detailDesc").textContent = jd.jobDescription || "—";
  document.getElementById("detailExp").textContent =
    `${jd.minExperience} – ${jd.maxExperience} years`;
  document.getElementById("detailSalary").textContent =
    `${formatSalary(jd.minSalary)} – ${formatSalary(jd.maxSalary)}`;

  document.getElementById("detailMeta").innerHTML = `
    <span class="detail-meta-item">${jd.location}</span>
    <span class="detail-meta-item">${jobTypeLabel(jd.jobType)}</span>
    <span class="detail-meta-item">${jd.minExperience}–${jd.maxExperience} yrs</span>
  `;

  const skillsEl = document.getElementById("detailSkills");
  if (skillsEl) {
    skillsEl.innerHTML = (jd.skillsRequired || [])
      .map((s) => `<span class="skill-chip">${s}</span>`)
      .join("");
  }

  const applyBtn = document.getElementById("detailApplyBtn");

  if (applyBtn) {
    if (appliedJdIds.length > 0) {
      applyBtn.textContent = "Applied";
      applyBtn.disabled = true;
      applyBtn.classList.add("disabled");
    } else {
      applyBtn.textContent = "Apply for this Position";
      applyBtn.disabled = false;
      applyBtn.classList.remove("disabled");
    }
  }

  const modal = document.getElementById("jdDetailModal");
  if (modal) modal.classList.remove("hidden");
}

function closeDetailModal() {
  const modal = document.getElementById("jdDetailModal");
  if (modal) modal.classList.add("hidden");
  selectedJD = null;
}

// Apply modal
function openApplyModal(jd) {
  closeDetailModal();
  selectedApplyJD = jd;

  const jobId = extractJdId(jd);
  document.getElementById("modalJobTitle").textContent = jd.jobTitle;
  document.getElementById("modalJobId").value = jobId;

  const name = getName();
  const nameEl = document.getElementById("pFullName");
  const emailEl = document.getElementById("pEmail");

  if (emailEl) {
    emailEl.value = localStorage.getItem("email") || "";
    emailEl.readOnly = true;
  }
  if (nameEl && name) nameEl.value = name;

  const modal = document.getElementById("applyModal");
  if (modal) modal.classList.remove("hidden");
}

export function closeApplyModal() {
  const modal = document.getElementById("applyModal");
  if (modal) modal.classList.add("hidden");
  document.getElementById("profilingForm")?.reset();
  hideFormMessages();
}

// DOM bootstrap
document.addEventListener("DOMContentLoaded", async () => {
  const token = getToken();
  if (!token) {
    window.location.href = "login.html";
    return;
  }

  // Navbar user info
  const name = getName();
  const nameEl = document.getElementById("userName");
  const avatarEl = document.getElementById("avatarInitial");
  if (nameEl) nameEl.textContent = name;
  if (avatarEl) {
    avatarEl.textContent = name
      .split(" ")
      .map((w) => w[0])
      .join("")
      .toUpperCase()
      .slice(0, 2);
  }

  await loadCandidateStatus();
  await loadJDs();

  const pendingApplyJdId = localStorage.getItem("pendingApplyJdId");

  if (pendingApplyJdId) {
    const jd = allJDs.find(
      (item) => String(item.id) === String(pendingApplyJdId),
    );

    if (jd && appliedJdIds.length === 0) {
      openApplyModal(jd);
    }

    localStorage.removeItem("pendingApplyJdId");
  }

  // Detail modal events
  document
    .getElementById("detailClose")
    ?.addEventListener("click", closeDetailModal);
  document.getElementById("jdDetailModal")?.addEventListener("click", (e) => {
    if (e.target.id === "jdDetailModal") closeDetailModal();
  });

  document.getElementById("detailApplyBtn")?.addEventListener("click", () => {
    if (selectedJD) openApplyModal(selectedJD);
  });

  // Apply modal events
  document
    .getElementById("applyModalClose")
    ?.addEventListener("click", closeApplyModal);
  document.getElementById("applyModal")?.addEventListener("click", (e) => {
    if (e.target.id === "applyModal") closeApplyModal();
  });

  // Form submit — passes selectedApplyJD into action handler
  document
    .getElementById("profilingForm")
    ?.addEventListener("submit", (e) => handleApplySubmit(e, selectedApplyJD));

  // Search & filter
  let timer;
  document.getElementById("searchInput")?.addEventListener("input", () => {
    clearTimeout(timer);
    timer = setTimeout(applyFilters, 300);
  });
  document
    .getElementById("filterType")
    ?.addEventListener("change", applyFilters);

  // Logout
  document.getElementById("logoutBtn")?.addEventListener("click", async () => {
    const token = getToken();
    try {
      if (token) await logoutCandidate(token);
    } catch (e) {}

    ["accessToken", "name", "email", "role", "hasApplied"].forEach((key) =>
      localStorage.removeItem(key),
    );

    window.location.href = "login.html";
  });
});
