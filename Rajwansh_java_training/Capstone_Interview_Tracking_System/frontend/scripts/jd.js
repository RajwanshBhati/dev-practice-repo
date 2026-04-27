import {
  fetchAllJDs,
  searchJDs,
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
    console.log("Panel script loaded");
    if (window.loadPanels) {
      window.loadPanels();
    }
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
    console.error(err);
  }
}

async function applyFilters() {
  const title = document.getElementById("search-input")?.value.trim();
  const status = document.getElementById("filter-status")?.value;
  const jobType = document.getElementById("filter-type")?.value;
  try {
    const data = await searchJDs(title, status, jobType);
    if (data.success) renderTable(data.data || [], handleEdit, handleDelete);
  } catch (err) {
    console.error(err);
  }
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
  if (!validateJDForm()) return;

  const payload = buildPayload();
  setModalLoading(true);

  try {
    let data;

    if (editingId) {
      data = await updateJD(editingId, payload);

      // Update status if changed
      const newStatus = document.getElementById("jd-status")?.value;
      const existing = allJDs.find((j) => j.id === editingId);
      if (existing && newStatus && existing.status !== newStatus) {
        await updateJDStatus(editingId, newStatus);
      }
    } else {
      data = await createJD(payload);
    }

    if (data.success) {
      closeModal();
      editingId = null;
      showAlert(
        editingId ? "JD updated successfully!" : "JD created successfully!",
        "success",
      );
      await loadJDs();
    } else {
      showAlert(data.message || "Something went wrong", "error", "modal-alert");
    }
  } catch (err) {
    showAlert("Cannot connect to server", "error", "modal-alert");
    console.error(err);
  } finally {
    setModalLoading(false);
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
    console.error(err);
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
  // Auth guard
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

  // Skill input init
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
  document.getElementById("search-input")?.addEventListener("input", () => {
    clearTimeout(timer);
    timer = setTimeout(applyFilters, 400);
  });

  document
    .getElementById("filter-status")
    ?.addEventListener("change", applyFilters);

  document
    .getElementById("filter-type")
    ?.addEventListener("change", applyFilters);

  document.getElementById("clear-filter-btn")?.addEventListener("click", () => {
    document.getElementById("search-input").value = "";
    document.getElementById("filter-status").value = "";
    document.getElementById("filter-type").value = "";
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
