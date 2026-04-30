import {
  fetchAllJDs,
  createJD,
  updateJD,
  updateJDStatus,
  deleteJD,
  logoutAPI,
} from "./jd/api.js";

import { renderTable, updateStats, showTableLoader } from "./jd/render.js";

import { validateJDForm, buildPayload, initSkillInput } from "./jd/form.js";

import {
  openCreateModal,
  openEditModal,
  closeModal,
  openDeleteModal,
  closeDeleteModal,
  setDeleteLoading,
} from "./jd/modal.js";

import { initHrCandidateSection } from "./pages/hr-candidate-main.js";
import { showToast } from "./utils/toast.js";
import { onboardCandidate } from "./services/hr-interview-service.js";
import { getAllCandidates } from "./services/hr-interview-service.js";
import { fetchCandidateFeedback } from "./services/hr-feedback-service.js";

let allJDs = [];
let editingId = null;
let deletingId = null;

const mainContent = document.getElementById("main-content");

function setActiveNav(clickedItem) {
  document.querySelectorAll(".nav-item").forEach((nav) => {
    nav.classList.remove("active");
  });

  clickedItem.classList.add("active");
}

async function loadPanelPage() {
  const res = await fetch("./panel-management.html");
  const html = await res.text();

  mainContent.innerHTML = html;

  const oldScript = document.getElementById("panel-script");
  if (oldScript) oldScript.remove();

  const script = document.createElement("script");
  script.src = "../scripts/candidate/panel.js";
  script.type = "module";
  script.id = "panel-script";

  script.onload = () => {
    if (window.loadPanels) {
      window.loadPanels();
    }
  };

  document.body.appendChild(script);
}

async function loadCandidatePage() {
  const res = await fetch("./hr-candidates-section.html");
  const html = await res.text();

  mainContent.innerHTML = html;

  const oldScript = document.getElementById("candidate-script");
  if (oldScript) oldScript.remove();

  const script = document.createElement("script");
  script.type = "module";
  script.src = "../scripts/pages/hr-candidate-main.js";
  script.id = "candidate-script";

  script.onload = () => {
    initHrCandidateSection();
  };

  document.body.appendChild(script);
}

function loadOnboardPage() {
  mainContent.innerHTML = `
    <div class="page-header">
      <div class="page-header-left">
        <h1>Candidate Onboard</h1>
        <p>Add candidate and send activation email.</p>
      </div>
    </div>

    <form id="candidate-onboard-form" class="onboard-form">
      <div class="form-group">
        <label for="candidate-name">Full Name</label>
        <input type="text" id="candidate-name" placeholder="Enter candidate name" required />
      </div>

      <div class="form-group">
        <label for="candidate-email">Email</label>
        <input type="email" id="candidate-email" placeholder="Enter candidate email" required />
      </div>

      <div class="form-group">
        <label for="candidate-mobile">Mobile Number</label>
        <input type="text" id="candidate-mobile" placeholder="Enter mobile number" required />
      </div>

      <div class="form-group">
        <label for="candidate-dob">Date of Birth</label>
        <input type="date" id="candidate-dob" required />
      </div>

      <button type="submit" class="btn-primary">
        Onboard Candidate
      </button>

      <p id="onboard-msg" style="margin-top:14px;font-weight:700;"></p>
    </form>
  `;

  initOnboardForm();
}

async function loadFeedbackPage() {
  mainContent.innerHTML = `
    <div class="page-header">
      <div class="page-header-left">
        <h1>Candidate Feedback</h1>
        <p>View panel feedback submitted for candidates.</p>
      </div>
    </div>

    <div class="table-wrapper">
      <table class="jd-table">
        <thead>
          <tr>
            <th>Candidate Name</th>
            <th>Email</th>
            <th>Job Title</th>
            <th>Round</th>
            <th>Feedback</th>
          </tr>
        </thead>
        <tbody id="feedback-table-body">
          <tr>
            <td colspan="5">Loading feedback...</td>
          </tr>
        </tbody>
      </table>
    </div>
  `;

  const tbody = document.getElementById("feedback-table-body");

  try {
    const candidates = await getAllCandidates();
    let rows = "";

    for (const candidate of candidates) {
      const candidateId =
        candidate.id || candidate.candidateId || candidate.candidateUserId;

      if (!candidateId) continue;

      const feedbackList = await fetchCandidateFeedback(candidateId);

      if (!feedbackList || feedbackList.length === 0) {
        continue;
      }

      feedbackList.forEach((feedback) => {
        rows += `
          <tr>
            <td>${candidate.name || "-"}</td>
            <td>${candidate.email || "-"}</td>
            <td>${candidate.jdTitle || candidate.jobTitle || "-"}</td>
            <td>${feedback.stage || "-"}</td>
            <td>
              <strong>Comments:</strong> ${feedback.comments || "-"}<br/>
              <strong>Strengths:</strong> ${feedback.strengths || "-"}<br/>
              <strong>Weaknesses:</strong> ${feedback.weaknesses || "-"}<br/>
              <strong>Rating:</strong> ${feedback.rating ?? "-"}<br/>
              <strong>Decision:</strong> ${feedback.decision || "-"}
            </td>
          </tr>
        `;
      });
    }

    tbody.innerHTML =
      rows ||
      `<tr>
        <td colspan="5">No feedback submitted yet.</td>
      </tr>`;
  } catch (error) {
    tbody.innerHTML = `
      <tr>
        <td colspan="5">Failed to load feedback.</td>
      </tr>
    `;
  }
}

function initOnboardForm() {
  const form = document.getElementById("candidate-onboard-form");

  if (!form) return;

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const msg = document.getElementById("onboard-msg");

    const payload = {
      fullName: document.getElementById("candidate-name").value.trim(),
      email: document.getElementById("candidate-email").value.trim(),
      mobileNumber: document.getElementById("candidate-mobile").value.trim(),
      dob: document.getElementById("candidate-dob").value,
    };

    try {
      msg.textContent = "Sending email...";
      msg.style.color = "#b45309";

      await onboardCandidate(payload);

      msg.textContent = "Candidate onboarded & mail sent";
      msg.style.color = "#047857";

      form.reset();
    } catch (err) {
      msg.textContent = err.message || "Failed to onboard candidate";
      msg.style.color = "#dc2626";
    }
  });
}

async function loadJDs() {
  showTableLoader();

  try {
    const data = await fetchAllJDs();

    if (data.success) {
      allJDs = data.data || [];
      renderTable(allJDs, handleEdit, handleDelete);
      updateStats(allJDs);
    } else {
      showToast(data.message, "error");
    }
  } catch (err) {
    showToast("Cannot connect to server. Is backend running?", "error");
  }
}

function applyFilters() {
  const search =
    document.getElementById("jd-search-input")?.value.toLowerCase() || "";
  const status = document.getElementById("jd-status-filter")?.value || "";
  const type = document.getElementById("jd-type-filter")?.value || "";

  const normalize = (v) =>
    String(v || "")
      .toUpperCase()
      .replaceAll(" ", "_");

  const filtered = allJDs.filter((jd) => {
    const text = `${jd.jobTitle || ""} ${jd.location || ""}`.toLowerCase();

    return (
      (!search || text.includes(search)) &&
      (!status || normalize(jd.status) === normalize(status)) &&
      (!type || normalize(jd.jobType) === normalize(type))
    );
  });

  renderTable(filtered, handleEdit, handleDelete);
}

function handleEdit(id) {
  editingId = id;
  const jd = allJDs.find((j) => j.id === id);
  if (jd) openEditModal(jd);
}

function handleDelete(id, title) {
  deletingId = id;
  openDeleteModal(id, title);
}

async function handleSubmit() {
  if (!validateJDForm()) {
    showToast("Please fill all required JD fields correctly", "error");
    return;
  }

  const payload = buildPayload();
  const isEditMode = Boolean(editingId);

  try {
    if (isEditMode) {
      const newStatus = document.getElementById("jd-status")?.value;
      const existing = allJDs.find((j) => String(j.id) === String(editingId));

      if (existing && newStatus && existing.status !== newStatus) {
        await updateJDStatus(editingId, newStatus);
      }

      await updateJD(editingId, payload);
      showToast("JD updated successfully", "success");
    } else {
      await createJD(payload);
      showToast("JD created successfully", "success");
    }

    closeModal();

    const searchInput = document.getElementById("jd-search-input");
    const statusFilter = document.getElementById("jd-status-filter");
    const typeFilter = document.getElementById("jd-type-filter");

    if (searchInput) searchInput.value = "";
    if (statusFilter) statusFilter.value = "";
    if (typeFilter) typeFilter.value = "";

    await loadJDs();
    editingId = null;
  } catch (err) {
    showToast(err.message || "Something went wrong", "error");
  }
}

async function handleConfirmDelete() {
  if (!deletingId) return;

  setDeleteLoading(true);

  try {
    const data = await deleteJD(deletingId);
    closeDeleteModal();

    if (data.success) {
      deletingId = null;
      showToast("Job Description deleted", "success");
      await loadJDs();
    } else {
      showToast(data.message, "error");
    }
  } catch (err) {
    closeDeleteModal();
    showToast("Cannot connect to server", "error");
  } finally {
    setDeleteLoading(false);
  }
}

async function handleLogout() {
  try {
    await logoutAPI();
  } catch (e) {}

  localStorage.clear();
  window.location.href = "login.html";
}

function bindSidebarEvents() {
  document.querySelectorAll(".nav-item").forEach((item) => {
    item.addEventListener("click", async (e) => {
      const page = item.dataset.page;

      if (!page) return;

      e.preventDefault();
      setActiveNav(item);

      if (page === "panel") {
        await loadPanelPage();
        return;
      }

      if (page === "candidates") {
        await loadCandidatePage();
        return;
      }

      if (page === "onboard") {
        loadOnboardPage();
        return;
      }

      if (page === "feedback") {
        await loadFeedbackPage();
        return;
      }
    });
  });
}

function bindJDEvents() {
  document.getElementById("create-jd-btn")?.addEventListener("click", () => {
    editingId = null;
    openCreateModal();
  });

  document
    .getElementById("modal-submit")
    ?.addEventListener("click", handleSubmit);
  document.getElementById("modal-close")?.addEventListener("click", closeModal);
  document
    .getElementById("modal-cancel")
    ?.addEventListener("click", closeModal);

  document.getElementById("jd-modal")?.addEventListener("click", (e) => {
    if (e.target.id === "jd-modal") closeModal();
  });

  document
    .getElementById("delete-confirm")
    ?.addEventListener("click", handleConfirmDelete);
  document
    .getElementById("delete-cancel")
    ?.addEventListener("click", closeDeleteModal);
  document
    .getElementById("delete-modal-close")
    ?.addEventListener("click", closeDeleteModal);

  document.getElementById("delete-modal")?.addEventListener("click", (e) => {
    if (e.target.id === "delete-modal") closeDeleteModal();
  });

  let timer;

  document.getElementById("jd-search-input")?.addEventListener("input", () => {
    clearTimeout(timer);
    timer = setTimeout(applyFilters, 300);
  });

  document
    .getElementById("jd-status-filter")
    ?.addEventListener("change", applyFilters);
  document
    .getElementById("jd-type-filter")
    ?.addEventListener("change", applyFilters);

  document
    .getElementById("jd-clear-filter-btn")
    ?.addEventListener("click", () => {
      const searchInput = document.getElementById("jd-search-input");
      const statusFilter = document.getElementById("jd-status-filter");
      const typeFilter = document.getElementById("jd-type-filter");

      if (searchInput) searchInput.value = "";
      if (statusFilter) statusFilter.value = "";
      if (typeFilter) typeFilter.value = "";

      renderTable(allJDs, handleEdit, handleDelete);
    });

  document
    .getElementById("logout-btn")
    ?.addEventListener("click", handleLogout);

  window.openCreateModal = () => {
    editingId = null;
    openCreateModal();
  };
}

document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("accessToken");
  const role = localStorage.getItem("role");

  if (!token || role?.toLowerCase() !== "hr") {
    window.location.href = "login.html";
    return;
  }

  const name = localStorage.getItem("name") || "HR Admin";
  const nameEl = document.getElementById("user-name");
  const avatarEl = document.getElementById("user-avatar");

  if (nameEl) nameEl.textContent = name;

  if (avatarEl) {
    avatarEl.textContent = name
      .split(" ")
      .map((w) => w[0])
      .join("")
      .toUpperCase()
      .slice(0, 2);
  }

  bindSidebarEvents();
  bindJDEvents();

  loadJDs();
  initSkillInput();
});
