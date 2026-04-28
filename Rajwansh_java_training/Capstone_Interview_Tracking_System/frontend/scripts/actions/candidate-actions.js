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
      renderGrid(allJDs);
    } else {
      if (noJobs) noJobs.classList.remove("hidden");
    }
  } catch (err) {
    clearTimeout(fallbackTimer);
    if (loader) loader.classList.add("hidden");
    if (noJobs) noJobs.classList.remove("hidden");
  }
}

// Load candidate application status
export async function loadCandidateStatus() {
  const token = getToken();
  if (!token) return;

  try {
    const data = await getCandidateStatus(token);

    if (!data || data.status === "NOT_APPLIED") {
      document.getElementById("stageSection").style.display = "none";
      showBanner("You have not applied for any position yet.", "pending");
      return;
    }

    candidateStatus = data;
    appliedJdIds = ["applied"];

    const stage = data.status;
    const status = data.status;

    renderStageTracker(stage, status);

    document.getElementById("candidate-current-status").textContent = status;

    const finalStatus = document.getElementById("final-status");

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
  } catch (err) {}
}

// Handle application form submission
export async function handleApplySubmit(e, selectedApplyJD) {
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
  const jdId = extractJdId(selectedApplyJD);

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

  const requiredFields = [
    { value: fullName, label: "Full Name" },
    { value: email, label: "Email" },
    { value: mobile, label: "Mobile Number" },
    { value: currentOrg, label: "Current Organisation" },
    { value: preferredLocation, label: "Preferred Location" },
    { value: totalExp, label: "Total Experience" },
    { value: relevantExp, label: "Relevant Experience" },
    { value: currentCTC, label: "Current CTC" },
    { value: expectedCTC, label: "Expected CTC" },
    { value: noticePeriod, label: "Notice Period" },
    { value: source, label: "Source" },
  ];

  const missingFields = requiredFields
    .filter(
      (field) =>
        field.value === null ||
        field.value === undefined ||
        String(field.value).trim() === "",
    )
    .map((field) => field.label);

  if (missingFields.length > 0) {
    showFormMsg(
      "formError",
      `Please fill required field(s): ${missingFields.join(", ")}`,
      "error",
    );
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

      setTimeout(async () => {
        closeApplyModal();
        await loadCandidateStatus();
        showBanner("Application submitted", "success");
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

// Apply search/type filters to JD grid
export function applyFilters() {
  const searchInput = document.getElementById("searchInput")?.value;
  const typeInput = document.getElementById("filterType")?.value;

  const filtered = filterJDs(allJDs, searchInput, typeInput);
  renderGrid(filtered);

  const noJobs = document.getElementById("noJobs");
  if (noJobs) {
    noJobs.classList.toggle("hidden", filtered.length > 0);
  }
}
