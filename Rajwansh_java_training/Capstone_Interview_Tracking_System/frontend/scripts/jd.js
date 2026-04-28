import {
  fetchAllJDs,
  createJD,
  updateJD,
  updateJDStatus,
  deleteJD,
  logoutAPI,
} from "./jd/api.js";
import { renderTable, updateStats, showTableLoader } from "./jd/render.js";
import {
  validateJDForm,
  buildPayload,
  setModalLoading,
  initSkillInput,
} from "./jd/form.js";
import {
  openCreateModal,
  openEditModal,
  closeModal,
  openDeleteModal,
  closeDeleteModal,
  setDeleteLoading,
} from "./jd/modal.js";
import { showAlert } from "./jd/utils.js";
import { initHrCandidateSection } from "../scripts/pages/hr-candidate-main.js";

// here i defined states
let allJDs = [];
let editingId = null;
let deletingId = null;

const mainContent = document.getElementById("main-content");
document.querySelectorAll(".nav-item").forEach((item) => {
  item.addEventListener("click", async (e) => {
    e.preventDefault();

    const page = item.getAttribute("data-page");

    if (page === "panel") {
      loadPanelPage();
    }

    if (page === "candidates") {
      loadCandidatePage();
    }
  });
});

async function loadPanelPage() {
  const res = await fetch("./panel-management.html");
  const html = await res.text();

  mainContent.innerHTML = html;

  // remove old script
  const oldScript = document.getElementById("panel-script");
  if (oldScript) oldScript.remove();

  // inject new script
  const script = document.createElement("script");
  script.src = "../scripts/candidate/panel.js";
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

  // remove old script
  const oldScript = document.getElementById("candidate-script");
  if (oldScript) oldScript.remove();

  // inject new script
  const script = document.createElement("script");
  script.type = "module";
  script.src = "../scripts/pages/hr-candidate-main.js";
  script.id = "candidate-script";

  script.onload = () => {
    initHrCandidateSection();
  };

  document.body.appendChild(script);
}

// Load JD
async function loadJDs() {
  showTableLoader();

  try {
    const data = await fetchAllJDs();

    if (data.success) {
      allJDs = data.data || [];

      renderTable(allJDs, handleEdit, handleDelete);
      updateStats(allJDs);
    } else {
      showAlert(data.message || "Failed to load");
    }
  } catch (err) {
    showAlert("Cannot connect to server. Is backend running?");
  }
}
function applyFilters() {
  const search = document.getElementById("jd-search-input").value.toLowerCase();
  const status = document.getElementById("jd-status-filter").value;
  const type = document.getElementById("jd-type-filter").value;

  const normalize = (v) =>
    String(v || "")
      .toUpperCase()
      .replaceAll(" ", "_");

  const filtered = allJDs.filter((jd) => {
    const text = `${jd.jobTitle} ${jd.location}`.toLowerCase();

    return (
      (!search || text.includes(search)) &&
      (!status || normalize(jd.status) === normalize(status)) &&
      (!type || normalize(jd.jobType) === normalize(type))
    );
  });

  renderTable(filtered, handleEdit, handleDelete);
}

// Handel edit functionality
function handleEdit(id) {
  editingId = id;
  const jd = allJDs.find((j) => j.id === id);
  if (jd) openEditModal(jd);
}

// Handle delete
function handleDelete(id, title) {
  deletingId = id;
  openDeleteModal(id, title);
}

// submit jd
async function handleSubmit() {
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
    } else {
      await createJD(payload);
    }

    closeModal();

    // clear filters
    document.getElementById("jd-search-input").value = "";
    document.getElementById("jd-status-filter").value = "";
    document.getElementById("jd-type-filter").value = "";

    await loadJDs();

    showAlert("JD updated successfully", "success");

    editingId = null;
  } catch (err) {
    console.error(err);
  }
}

// Delete confirmation
async function handleConfirmDelete() {
  if (!deletingId) return;
  setDeleteLoading(true);

  try {
    const data = await deleteJD(deletingId);
    closeDeleteModal();
    if (data.success) {
      deletingId = null;
      showAlert("Job Description deleted", "success");
      await loadJDs();
    } else {
      showAlert(data.message || "Delete failed");
    }
  } catch (err) {
    closeDeleteModal();
    showAlert("Cannot connect to server");
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

document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("accessToken");
  const role = localStorage.getItem("role");
  if (!token || role?.toLowerCase() !== "hr") {
    window.location.href = "login.html";
    return;
  }

  // Sidebar user info
  const name = localStorage.getItem("name") || "HR Admin";
  const nameEl = document.getElementById("user-name");
  const avatarEl = document.getElementById("user-avatar");
  if (nameEl) nameEl.textContent = name;
  if (avatarEl)
    avatarEl.textContent = name
      .split(" ")
      .map((w) => w[0])
      .join("")
      .toUpperCase()
      .slice(0, 2);

  // Load data JD
  loadJDs();
  initSkillInput();

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
      document.getElementById("jd-search-input").value = "";
      document.getElementById("jd-status-filter").value = "";
      document.getElementById("jd-type-filter").value = "";

      renderTable(allJDs, handleEdit, handleDelete);
    });

  document
    .getElementById("logout-btn")
    ?.addEventListener("click", handleLogout);

  // Empty state create button
  window.openCreateModal = () => {
    editingId = null;
    openCreateModal();
  };
});
