import { clearAllErrors } from "./utils.js";
import { setSkills, resetSkills, renderSkillTags } from "./form.js";

export function openCreateModal() {
  const form = document.getElementById("jd-form");
  const modalTitle = document.getElementById("modal-title");
  const btnText = document.getElementById("modal-btn-text");
  const statusGroup = document.getElementById("status-group");
  const modal = document.getElementById("jd-modal");

  if (form) form.reset();
  clearAllErrors();
  resetSkills();

  if (modalTitle) modalTitle.textContent = "Create Job Description";
  if (btnText) btnText.textContent = "Create JD";
  if (statusGroup) statusGroup.style.display = "none";
  if (modal) modal.style.display = "flex";
}

export function openEditModal(jd) {
  clearAllErrors();
  setSkills(jd.skillsRequired || []);

  document.getElementById("jd-id").value = jd.id;
  document.getElementById("job-title").value = jd.jobTitle;
  document.getElementById("job-description").value = jd.jobDescription;
  document.getElementById("location").value = jd.location;
  document.getElementById("min-exp").value = jd.minExperience;
  document.getElementById("max-exp").value = jd.maxExperience;
  document.getElementById("min-salary").value = jd.minSalary;
  document.getElementById("max-salary").value = jd.maxSalary;
  document.getElementById("job-type").value = jd.jobType;
  document.getElementById("jd-status").value = jd.status;

  document.getElementById("modal-title").textContent = "Edit Job Description";
  document.getElementById("modal-btn-text").textContent = "Save Changes";
  document.getElementById("status-group").style.display = "block";
  document.getElementById("jd-modal").style.display = "flex";
}

export function closeModal() {
  const modal = document.getElementById("jd-modal");
  if (modal) modal.style.display = "none";
  resetSkills();
}

export function openDeleteModal(id, title) {
  const titleEl = document.getElementById("delete-jd-title");
  const modal = document.getElementById("delete-modal");
  if (titleEl) titleEl.textContent = title;
  if (modal) modal.style.display = "flex";
}

export function closeDeleteModal() {
  const modal = document.getElementById("delete-modal");
  if (modal) modal.style.display = "none";
}

export function setDeleteLoading(loading) {
  const btn = document.getElementById("delete-confirm");
  const text = document.getElementById("delete-btn-text");
  const loader = document.getElementById("delete-btn-loader");
  if (btn) btn.disabled = loading;
  if (text) text.style.display = loading ? "none" : "inline";
  if (loader) loader.style.display = loading ? "flex" : "none";
}
