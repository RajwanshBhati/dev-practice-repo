console.log("loaded js");
import {
  getCandidateStatus,
  applyCandidate,
  logoutCandidate,
} from "./candidate.js";
import { API } from "../config/candidate-api.js";

let allJDs = [];
let selectedJD = null;
let selectedApplyJD = null;
let lastOpenedJD = null;
let appliedJdIds = [];
let candidateStatus = null;
let isSubmitting = false;

function getToken() {
  return localStorage.getItem("accessToken");
}
function getName() {
  return localStorage.getItem("name") || "Candidate";
}

// here i maped the stages
const STAGE_IDS = ["st-PROFILING", "st-L1", "st-L2", "st-HR"];
const STAGE_MAP = {
  PROFILING: 0,
  SCREENING: 0,
  L1: 1,
  L2: 2,
  HR: 3,
  HR_ROUND: 3,
};

//create a helper function for salary─
function formatSalary(val) {
  if (!val) return "—";
  const num = parseFloat(val);
  if (num >= 100000) return `₹${(num / 100000).toFixed(1)}L`;
  return `₹${num.toLocaleString("en-IN")}`;
}

function jobTypeLabel(type) {
  const map = {
    FULL_TIME: "Full Time",
    CONTRACT: "Contract",
    REMOTE: "Remote",
  };
  return map[type] || type;
}

function jobTypeBadgeClass(type) {
  const map = {
    FULL_TIME: "badge-full-time",
    CONTRACT: "badge-contract",
    REMOTE: "badge-remote",
  };
  return map[type] || "";
}

// Extract JD ID
function extractJdId(jd) {
  if (!jd) return "";
  // Support all possible field names from API
  return jd.jobId ?? jd.jdId ?? jd.id ?? jd.job_id ?? jd.JdId ?? "";
}

// stage tracker
function renderStageTracker(stage, status) {
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

function showBanner(msg, type) {
  const el = document.getElementById("appBanner");
  if (!el) return;
  el.textContent = msg;
  el.className = `app-banner ${type}`;
  el.classList.remove("hidden");
}

//  Load JDs
async function loadJDs() {
  console.log("Starting loadJDs");
  const loader = document.getElementById("loader");
  const grid = document.getElementById("jdGrid");
  const noJobs = document.getElementById("noJobs");

  if (loader) loader.classList.remove("hidden");
  if (grid) grid.innerHTML = "";
  if (noJobs) noJobs.classList.add("hidden");

  // Fallback: hide loader after 10 seconds
  const fallbackTimer = setTimeout(() => {
    console.log("Fallback: hiding loader after timeout");
    if (loader) loader.classList.add("hidden");
    if (noJobs) noJobs.classList.remove("hidden");
  }, 10000);

  try {
    console.log("API URL:", API.HR.JD_LIST);
    const res = await fetch(API.HR.JD_LIST);
    console.log("Fetch response status:", res.status);
    const data = await res.json();
    console.log("JD DATA:", data);

    clearTimeout(fallbackTimer);
    if (loader) loader.classList.add("hidden");

    if (data.success && data.data?.length > 0) {
      allJDs = data.data;
      renderGrid(allJDs);
    } else {
      if (noJobs) noJobs.classList.remove("hidden");
    }
  } catch (err) {
    console.log("Error in loadJDs:", err);
    clearTimeout(fallbackTimer);
    if (loader) loader.classList.add("hidden");
    if (noJobs) noJobs.classList.remove("hidden");
    console.error(err);
  }
}

// function render the grid
function renderGrid(data) {
  const grid = document.getElementById("jdGrid");
  if (!grid) return;
  grid.innerHTML = "";

  data.forEach((jd) => {
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
        <span>📍 ${jd.location}</span>
        <span>💼 ${jd.minExperience}–${jd.maxExperience} yrs</span>
      </div>
      <div class="jd-card-skills">
        ${skillsHtml}${moreHtml}
      </div>
      <div class="jd-card-footer">
        <span class="jd-card-salary">
          ${formatSalary(jd.minSalary)} – ${formatSalary(jd.maxSalary)}
        </span>
        <span style="font-size:0.8rem;color:var(--primary);font-weight:600;">
          View Details →
        </span>
      </div>
    `;

    card.addEventListener("click", () => openDetailModal(jd));
    grid.appendChild(card);
  });
}

//when i click to apply open model
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
    <span class="detail-meta-item">📍 ${jd.location}</span>
    <span class="detail-meta-item">💼 ${jobTypeLabel(jd.jobType)}</span>
    <span class="detail-meta-item">⏱ ${jd.minExperience}–${jd.maxExperience} yrs</span>
  `;

  const skillsEl = document.getElementById("detailSkills");
  if (skillsEl) {
    skillsEl.innerHTML = (jd.skillsRequired || [])
      .map((s) => `<span class="skill-chip">${s}</span>`)
      .join("");
  }

  const applyBtn = document.getElementById("detailApplyBtn");
  const jdId = extractJdId(jd);
  if (applyBtn) {
    if (appliedJdIds.length > 0) {
      applyBtn.textContent = "Already Applied";
      applyBtn.disabled = true;
    } else {
      applyBtn.textContent = "Apply for this Position";
      applyBtn.disabled = false;
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

// apply more
function openApplyModal(jd) {
  closeDetailModal();

  selectedApplyJD = jd;

  const jobId = extractJdId(jd);

  console.log("Opening apply modal — JD object:", JSON.stringify(jd));
  console.log("Resolved jobId:", jobId);

  document.getElementById("modalJobTitle").textContent = jd.jobTitle;
  document.getElementById("modalJobId").value = jobId;

  // Pre-fill name and email from localStorage
  const name = getName();
  const email = localStorage.getItem("candidateEmail") || "";
  const nameEl = document.getElementById("pFullName");
  const emailEl = document.getElementById("pEmail");
  if (nameEl && name) nameEl.value = name;
  if (emailEl && email) emailEl.value = email;

  const modal = document.getElementById("applyModal");
  if (modal) modal.classList.remove("hidden");
}

function closeApplyModal() {
  const modal = document.getElementById("applyModal");
  if (modal) modal.classList.add("hidden");
  document.getElementById("profilingForm")?.reset();
  hideFormMessages();
}

function hideFormMessages() {
  document.getElementById("formError")?.classList.add("hidden");
  document.getElementById("formSuccess")?.classList.add("hidden");
}

//submit application
async function handleApplySubmit(e) {
  e.preventDefault();
  hideFormMessages();

  const token = getToken();
  if (!token) {
    window.location.href = "login.html";
    return;
  }

  const fullName = document.getElementById("pFullName")?.value.trim();
  const email = document.getElementById("pEmail")?.value.trim();
  const mobileCode = document.getElementById("pMobileCode")?.value;
  const mobile = document.getElementById("pMobile")?.value.trim();
  const dob = document.getElementById("pDob")?.value;
  const currentOrg = document.getElementById("pCurrentOrg")?.value.trim();
  const preferredLocation = document
    .getElementById("pPreferredLocation")
    ?.value.trim();
  const totalExp = document.getElementById("pTotalExp")?.value;
  const relevantExp = document.getElementById("pRelevantExp")?.value;
  const currentCTC = document.getElementById("pCurrentCTC")?.value;
  const expectedCTC = document.getElementById("pExpectedCTC")?.value;
  const noticePeriod = document.getElementById("pNoticePeriod")?.value;
  const source = document.getElementById("pSource")?.value;
  const resumeFile = document.getElementById("pResumeFile")?.files?.[0];

  console.log("selectedApplyJD:", selectedApplyJD);
  const jdId = extractJdId(selectedApplyJD);

  console.log("JD ID:", jdId, "appliedJdIds:", appliedJdIds);

  if (appliedJdIds.length > 0) {
    showFormMsg(
      "formError",
      "You have already applied for a position.",
      "error",
    );
    return;
  }

  if (!jdId) {
    showFormMsg("formError", "Job ID missing. Reopen form.", "error");
    return;
  }

  if (!resumeFile) {
    showFormMsg("formError", "Resume required.", "error");
    return;
  }

  if (resumeFile.type !== "application/pdf") {
    showFormMsg("formError", "Only PDF allowed.", "error");
    return;
  }

  const btn = document.getElementById("submitProfilingBtn");
  if (btn) {
    btn.disabled = true;
    btn.textContent = "Submitting…";
  }

  const formData = new FormData();
  formData.append("name", fullName);
  formData.append("email", email);
  formData.append("mobileCode", mobileCode);
  formData.append("mobileNumber", mobile);
  formData.append("dateOfBirth", dob || "");
  formData.append("currentCompany", currentOrg);
  formData.append("totalExp", totalExp || "");
  formData.append("relevantExp", relevantExp || "");
  formData.append("currentCtc", currentCTC || "");
  formData.append("expectedCtc", expectedCTC || "");
  formData.append("noticePeriod", noticePeriod || "");
  formData.append("preferredLocation", preferredLocation);
  formData.append("source", source || "Website");
  formData.append("jdId", jdId);
  formData.append("resumeFile", resumeFile);

  console.debug("Submitting candidate application", {
    jdId,
    fullName,
    email,
    mobileCode,
    mobile,
    currentOrg,
    preferredLocation,
    totalExp,
    relevantExp,
    currentCTC,
    expectedCTC,
    noticePeriod,
    source,
    resumeFile,
  });

  try {
    const data = await applyCandidate(formData, token);

    console.log("API response:", data);

    if (data.success || data.id) {
      showFormMsg("formSuccess", "Application submitted!", "success");
      appliedJdIds.push(jdId);
      localStorage.setItem("hasApplied", "true"); // Persist application status

      setTimeout(() => {
        closeApplyModal();
        renderStageTracker("PROFILING", "IN_PROGRESS");
        showBanner("Application submitted", "success");
      }, 1200);
    } else {
      console.warn("Application submission failed", data);
      showFormMsg(
        "formError",
        data.message || data.error || "Submission failed",
        "error",
      );
    }
  } catch (err) {
    console.error("Application submission error", err);
    showFormMsg(
      "formError",
      err.message || "Server error. Try again.",
      "error",
    );
  } finally {
    if (btn) {
      btn.disabled = false;
      btn.textContent = "Submit Application";
    }
  }
}

function showFormMsg(id, msg, type) {
  const el = document.getElementById(id);
  if (!el) return;
  el.textContent = msg;
  el.className = `alert alert-${type}`;
  el.classList.remove("hidden");
}

// Load Candidate Status
async function loadCandidateStatus() {
  const token = getToken();
  if (!token) return;

  // Check localStorage for persisted application status
  const persistedApplied = localStorage.getItem("hasApplied");
  if (persistedApplied === "true") {
    console.log("Using persisted applied status");
    hasApplied = true;
    appliedJdIds = ["applied"];
    renderStageTracker("PROFILING", "IN_PROGRESS");
    showBanner("Application submitted — Profiling in progress", "pending");
    return;
  }

  try {
    const data = await getCandidateStatus(token);
    console.log("Candidate status response:", data);
    if (data.success && data.data) {
      candidateStatus = data.data;
      appliedJdIds = ["applied"]; // Prevent further applications

      const { stage, status, appliedJD } = data.data;
      renderStageTracker(stage, status);

      const bannerMsg =
        status === "REJECTED"
          ? `Application rejected at ${stage} stage`
          : status === "SELECTED"
            ? `🎉 Congratulations! Selected — ${stage}`
            : `Current Stage: ${stage} — ${status}`;

      const bannerType =
        status === "REJECTED"
          ? "rejected"
          : status === "SELECTED"
            ? "success"
            : "pending";

      showBanner(bannerMsg, bannerType);
    }
  } catch (err) {
    console.error("Status fetch error:", err);
  }
}

// Filters
function applyFilters() {
  const search =
    document.getElementById("searchInput")?.value.toLowerCase() || "";
  const type = document.getElementById("filterType")?.value || "";

  const filtered = allJDs.filter((jd) => {
    const matchSearch =
      !search ||
      jd.jobTitle?.toLowerCase().includes(search) ||
      jd.location?.toLowerCase().includes(search);

    const matchType =
      !type || jobTypeLabel(jd.jobType).toLowerCase() === type.toLowerCase();

    return matchSearch && matchType;
  });

  renderGrid(filtered);

  const noJobs = document.getElementById("noJobs");
  if (noJobs) {
    noJobs.classList.toggle("hidden", filtered.length > 0);
  }
}

document.addEventListener("DOMContentLoaded", async () => {
  const token = getToken();
  if (!token) {
    window.location.href = "login.html";
    return;
  }

  // User info in navbar
  const name = getName();
  const nameEl = document.getElementById("userName");
  const avatarEl = document.getElementById("avatarInitial");
  if (nameEl) nameEl.textContent = name;
  if (avatarEl)
    avatarEl.textContent = name
      .split(" ")
      .map((w) => w[0])
      .join("")
      .toUpperCase()
      .slice(0, 2);

  await loadCandidateStatus();
  await loadJDs();

  // Detail modal close
  document
    .getElementById("detailClose")
    ?.addEventListener("click", closeDetailModal);
  document.getElementById("jdDetailModal")?.addEventListener("click", (e) => {
    if (e.target.id === "jdDetailModal") closeDetailModal();
  });

  // Apply button in detail modal
  document.getElementById("detailApplyBtn")?.addEventListener("click", () => {
    if (selectedJD) openApplyModal(selectedJD);
  });

  // Apply modal close
  document
    .getElementById("applyModalClose")
    ?.addEventListener("click", closeApplyModal);
  document.getElementById("applyModal")?.addEventListener("click", (e) => {
    if (e.target.id === "applyModal") closeApplyModal();
  });

  // Profiling form submit
  document
    .getElementById("profilingForm")
    ?.addEventListener("submit", handleApplySubmit);

  // Filters
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
    try {
      await logoutCandidate(token);
    } catch (e) {}
    localStorage.removeItem("accessToken");
    localStorage.removeItem("name");
    localStorage.removeItem("email");
    localStorage.removeItem("role");
    localStorage.removeItem("hasApplied");
    window.location.href = "login.html";
  });
});
