import { getCandidateStatus, applyCandidate } from "../candidate/candidate.js";
import { API } from "../config/candidate-api.js";
import { filterJDs } from "../utils/candidate-filters.js";
import {
  renderGrid,
  renderStageTracker,
  showBanner,
  showFormMsg,
  hideFormMessages,
  closeApplyModal,
} from "../candidate/candidate-dashboard.js";

import { showFieldError, clearErrors } from "../validation.js";

export let allJDs = [];
export let appliedJdIds = [];
export let candidateStatus = null;

export function getToken() {
  return localStorage.getItem("accessToken");
}

export function getName() {
  return localStorage.getItem("name") || "Candidate";
}

export function extractJdId(jd) {
  if (!jd) {
    throw new Error("Invalid JD object");
  }
  const id = jd.id ?? jd.jd_id;

  if (!id) {
    throw new Error("JD ID missing in object");
  }
  return id;
}

// Load JDs from API
export async function loadJDs() {
  const loader = document.getElementById("loader");
  const grid = document.getElementById("jdGrid");
  const noJobs = document.getElementById("noJobs");

  if (loader) loader.classList.remove("hidden");
  if (grid) grid.innerHTML = "";
  if (noJobs) noJobs.classList.add("hidden");

  const fallbackTimer = setTimeout(() => {
    if (loader) loader.classList.add("hidden");
    if (noJobs) noJobs.classList.remove("hidden");
  }, 10000);

  try {
    const res = await fetch(API.HR.JD_LIST);
    const data = await res.json();

    clearTimeout(fallbackTimer);
    if (loader) loader.classList.add("hidden");

    if (data.success && data.data?.length > 0) {
      allJDs = data.data;
      renderGrid(allJDs, appliedJdIds);
    } else {
      if (noJobs) noJobs.classList.remove("hidden");
    }
  } catch (err) {
    clearTimeout(fallbackTimer);
    if (loader) loader.classList.add("hidden");
    if (noJobs) noJobs.classList.remove("hidden");
  }
}
export async function loadCandidateStatus() {
  const token = getToken();

  if (!token) {
    window.location.href = "login.html";
    return;
  }

  try {
    const data = await getCandidateStatus(token);

    const stageSection = document.getElementById("stageSection");
    const currentStatusEl = document.getElementById("candidate-current-status");
    const finalStatus = document.getElementById("final-status");

    if (!data || data.status === "NOT_APPLIED") {
      if (stageSection) stageSection.style.display = "none";
      renderMyApplications(data);
      showBanner("You have not applied for any position yet.", "pending");
      return;
    }

    if (stageSection) stageSection.style.display = "block";

    candidateStatus = data;
    appliedJdIds = data.status === "REJECTED" ? [] : [data.jdId];
    renderMyApplications(data);

    const status = data.status;

    renderStageTracker(data.currentStage || status, status, data.rejectedStage);

    if (currentStatusEl) {
      currentStatusEl.textContent = status;
    }

    if (finalStatus) {
      if (status === "SELECTED") {
        finalStatus.innerHTML = `
          <div class="final-status selected">
            <h2>Congratulations! You are selected.</h2>
          </div>
        `;
      } else if (status === "REJECTED") {
        finalStatus.innerHTML = `
          <div class="final-status rejected">
            <h2>Your application was rejected.</h2>
          </div>
        `;
      } else {
        finalStatus.innerHTML = "";
      }
    }

    const bannerMsg =
      status === "REJECTED"
        ? "Application rejected"
        : status === "SELECTED"
          ? "Congratulations! Selected"
          : `Current Stage: ${status}`;

    const bannerType =
      status === "REJECTED"
        ? "rejected"
        : status === "SELECTED"
          ? "success"
          : "pending";

    showBanner(bannerMsg, bannerType);
  } catch (err) {
    console.error("Candidate status load failed:", err);

    if (err.message?.includes("403")) {
      showBanner(
        "Session expired or access denied. Please login again.",
        "rejected",
      );
      localStorage.clear();
      setTimeout(() => {
        window.location.href = "login.html";
      }, 1200);
      return;
    }

    showBanner("Unable to load candidate status.", "rejected");
  }
}
export async function handleApplySubmit(e, selectedApplyJD) {
  e.preventDefault();
  hideFormMessages();
  clearErrors();

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
  const jdId = extractJdId(selectedApplyJD);

  if (appliedJdIds.length > 0 && candidateStatus?.status !== "REJECTED") {
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

  const requiredFields = [
    { id: "pFullName", value: fullName, message: "Full name is required." },
    { id: "pEmail", value: email, message: "Email is required." },
    { id: "pMobile", value: mobile, message: "Mobile number is required." },
    {
      id: "pCurrentOrg",
      value: currentOrg,
      message: "Current organisation is required.",
    },
    {
      id: "pPreferredLocation",
      value: preferredLocation,
      message: "Preferred location is required.",
    },
    {
      id: "pTotalExp",
      value: totalExp,
      message: "Total experience is required.",
    },
    {
      id: "pRelevantExp",
      value: relevantExp,
      message: "Relevant experience is required.",
    },
    {
      id: "pCurrentCTC",
      value: currentCTC,
      message: "Current CTC is required.",
    },
    {
      id: "pExpectedCTC",
      value: expectedCTC,
      message: "Expected CTC is required.",
    },
    {
      id: "pNoticePeriod",
      value: noticePeriod,
      message: "Notice period is required.",
    },
    { id: "pSource", value: source, message: "Source is required." },
  ];

  for (const field of requiredFields) {
    if (
      field.value === null ||
      field.value === undefined ||
      String(field.value).trim() === ""
    ) {
      showFieldError(field.id, field.message);
      return;
    }
  }

  if (!/^[6-9]\d{9}$/.test(mobile)) {
    showFieldError("pMobile", "Enter a valid 10-digit mobile number.");
    return;
  }

  const totalExpNum = Number(totalExp);
  const relevantExpNum = Number(relevantExp);
  const currentCTCNum = Number(currentCTC);
  const expectedCTCNum = Number(expectedCTC);

  if (relevantExpNum > totalExpNum) {
    showFieldError(
      "pRelevantExp",
      "Relevant experience cannot be greater than total experience.",
    );
    return;
  }

  if (expectedCTCNum < currentCTCNum) {
    showFieldError(
      "pExpectedCTC",
      "Expected CTC must be greater than current CTC.",
    );
    return;
  }

  if (!resumeFile) {
    showFieldError("pResumeFile", "Resume is required.");
    return;
  }

  if (resumeFile.type !== "application/pdf") {
    showFieldError("pResumeFile", "Only PDF file is allowed.");
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
  formData.append("totalExp", totalExp);
  formData.append("relevantExp", relevantExp);
  formData.append("currentCtc", currentCTC);
  formData.append("expectedCtc", expectedCTC);
  formData.append("noticePeriod", noticePeriod);
  formData.append("preferredLocation", preferredLocation);
  formData.append("source", source || "Website");
  formData.append("jdId", jdId);
  formData.append("resumeFile", resumeFile);

  try {
    const data = await applyCandidate(formData, token);

    if (data.success || data.id) {
      showFormMsg("formSuccess", "Application submitted!", "success");
      appliedJdIds.push(jdId);
      localStorage.setItem("hasApplied", "true");

      document.querySelectorAll(".apply-btn").forEach((btn) => {
        btn.disabled = true;
        btn.textContent = "Applied";
      });

      const detailApplyBtn = document.getElementById("detailApplyBtn");

      if (detailApplyBtn) {
        detailApplyBtn.disabled = true;
        detailApplyBtn.textContent = "Applied";
      }

      setTimeout(async () => {
        closeApplyModal();

        await loadCandidateStatus();
        await loadJDs();

        document
          .querySelector('[data-page="browse-jobs"]')
          ?.classList.remove("active");
        document
          .querySelector('[data-page="my-applications"]')
          ?.classList.add("active");

        document.getElementById("browse-jobs-section")?.classList.add("hidden");
        document
          .getElementById("my-applications-section")
          ?.classList.remove("hidden");

        showBanner("Application submitted successfully", "success");
      }, 1200);
    } else {
      showFormMsg(
        "formError",
        data.message || data.error || "Submission failed",
        "error",
      );
    }
  } catch (err) {
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
function renderMyApplications(data) {
  const list = document.getElementById("my-applications-list");
  const details = document.getElementById("candidateStatusDetails");

  if (!list) return;

  if (!data || data.status === "NOT_APPLIED") {
    list.innerHTML = `
      <div class="application-empty-card">
        <h3>No Applications Found</h3>
        <p>You have not applied for any job yet.</p>
      </div>
    `;
    if (details) details.style.display = "none";
    return;
  }

  if (details) details.style.display = "block";

  const jd = allJDs.find((item) => String(item.id) === String(data.jdId));

  list.innerHTML = `
    <div class="application-card">
      <div>
        <h3>${jd?.jobTitle || "Applied Job"}</h3>
        <p>${jd?.location || "Location not available"}</p>
      </div>

      <div>
        <span class="status-badge">${data.status}</span>
      </div>

      <div class="application-meta">
        <p><strong>Candidate:</strong> ${data.name || "-"}</p>
        <p><strong>Email:</strong> ${data.email || "-"}</p>
        <p><strong>JD ID:</strong> ${data.jdId || "-"}</p>
      </div>
    </div>
  `;
}

// Apply search/type filters to JD grid
export function applyFilters() {
  const searchInput = document.getElementById("searchInput")?.value;
  const typeInput = document.getElementById("filterType")?.value;

  const filtered = filterJDs(allJDs, searchInput, typeInput);
  renderGrid(filtered, appliedJdIds);

  const noJobs = document.getElementById("noJobs");
  if (noJobs) {
    noJobs.classList.toggle("hidden", filtered.length > 0);
  }
}
