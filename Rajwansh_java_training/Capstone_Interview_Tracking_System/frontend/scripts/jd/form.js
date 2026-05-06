import {
  showFieldError,
  clearAllErrors,
  hideAlert,
  showAlert,
} from "./utils.js";

let skills = [];

export function getSkills() {
  return [...skills];
}

export function setSkills(arr) {
  skills = [...arr];
  renderSkillTags();
}

export function resetSkills() {
  skills = [];
  renderSkillTags();
}

export function renderSkillTags() {
  const container = document.getElementById("skills-tags");
  if (!container) return;
  container.innerHTML = skills
    .map(
      (s, i) => `
    <span class="skill-input-tag">
      ${s}
      <button type="button" class="remove-skill" data-index="${i}">×</button>
    </span>
  `,
    )
    .join("");

  // Remove skill events
  container.querySelectorAll(".remove-skill").forEach((btn) => {
    btn.addEventListener("click", () => {
      skills.splice(parseInt(btn.dataset.index), 1);
      renderSkillTags();
    });
  });
}

export function addSkill(value) {
  const val = value.trim().replace(/,/g, "").trim();
  if (val && !skills.includes(val)) {
    skills.push(val);
    renderSkillTags();
  }
  const input = document.getElementById("skill-input");
  if (input) input.value = "";
}

export function initSkillInput() {
  const input = document.getElementById("skill-input");
  if (!input) return;
  input.addEventListener("keydown", (e) => {
    if (e.key === "Enter" || e.key === ",") {
      e.preventDefault();
      addSkill(e.target.value);
    }
  });
  input.addEventListener("blur", (e) => {
    if (e.target.value.trim()) addSkill(e.target.value);
  });
}

export function validateJDForm() {
  clearAllErrors();
  let valid = true;

  const title = document.getElementById("job-title")?.value.trim();
  const desc = document.getElementById("job-description")?.value.trim();
  const loc = document.getElementById("location")?.value.trim();
  const minExp = parseInt(document.getElementById("min-exp")?.value);
  const maxExp = parseInt(document.getElementById("max-exp")?.value);
  const minSal = parseFloat(document.getElementById("min-salary")?.value);
  const maxSal = parseFloat(document.getElementById("max-salary")?.value);
  const type = document.getElementById("job-type")?.value;

  if (!title) {
    showFieldError("job-title", "Job title is required");
    valid = false;
  }
  if (!desc) {
    showFieldError("job-description", "Description is required");
    valid = false;
  }
  if (!loc) {
    showFieldError("location", "Location is required");
    valid = false;
  }
  if (!type) {
    showFieldError("job-type", "Job type is required");
    valid = false;
  }

  if (skills.length === 0) {
    const el = document.getElementById("skills-error");
    if (el) el.textContent = "At least one skill is required";
    valid = false;
  }

  if (isNaN(minExp) || minExp < 0) {
    showFieldError("min-exp", "Enter valid min experience");
    valid = false;
  }
  if (isNaN(maxExp) || maxExp < 0) {
    showFieldError("max-exp", "Enter valid max experience");
    valid = false;
  }
  if (!isNaN(minExp) && !isNaN(maxExp) && minExp > maxExp) {
    showFieldError("max-exp", "Max must be >= min");
    valid = false;
  }
  if (isNaN(minSal) || minSal <= 0) {
    showFieldError("min-salary", "Enter valid min salary");
    valid = false;
  }
  if (isNaN(maxSal) || maxSal <= 0) {
    showFieldError("max-salary", "Enter valid max salary");
    valid = false;
  }
  if (!isNaN(minSal) && !isNaN(maxSal) && minSal > maxSal) {
    showFieldError("max-salary", "Max must be >= min");
    valid = false;
  }

  return valid;
}

export function buildPayload() {
  return {
    jobTitle: document.getElementById("job-title").value.trim(),
    jobDescription: document.getElementById("job-description").value.trim(),
    skillsRequired: [...skills],
    minExperience: parseInt(document.getElementById("min-exp").value),
    maxExperience: parseInt(document.getElementById("max-exp").value),
    minSalary: parseFloat(document.getElementById("min-salary").value),
    maxSalary: parseFloat(document.getElementById("max-salary").value),
    location: document.getElementById("location").value.trim(),
    jobType: document.getElementById("job-type").value,
  };
}

export function setModalLoading(loading) {
  const btn = document.getElementById("modal-submit");
  const text = document.getElementById("modal-btn-text");
  const loader = document.getElementById("modal-btn-loader");
  if (btn) btn.disabled = loading;
  if (text) text.style.display = loading ? "none" : "inline";
  if (loader) loader.style.display = loading ? "flex" : "none";
}
