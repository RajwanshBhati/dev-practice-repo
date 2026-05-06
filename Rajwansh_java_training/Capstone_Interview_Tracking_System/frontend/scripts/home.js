import { API } from "./config/candidate-api.js";

const jobGrid = document.getElementById("homeJobGrid");
const loader = document.getElementById("homeLoader");
const noJobs = document.getElementById("noJobs");

function formatSalary(value) {
  if (!value) return "Not disclosed";

  const num = Number(value);
  if (num >= 100000) {
    return `₹${(num / 100000).toFixed(1)} LPA`;
  }

  return `₹${num.toLocaleString("en-IN")}`;
}

function renderJobs(jobs) {
  jobGrid.innerHTML = "";

  jobs.forEach((job) => {
    const skills = (job.skillsRequired || [])
      .slice(0, 4)
      .map((skill) => `<span class="skill">${skill}</span>`)
      .join("");

    const card = document.createElement("div");
    card.className = "job-card";

    card.innerHTML = `
      <h3>${job.jobTitle || "Untitled Job"}</h3>

      <div class="job-meta">
        <span>${job.location || "Location not added"}</span>
        <span>${job.jobType || "Job Type"}</span>
        <span>${job.minExperience || 0}-${job.maxExperience || 0} yrs</span>
      </div>

      <p class="job-desc">
        ${job.jobDescription || "No description available."}
      </p>

      <div class="skills">${skills}</div>

      <p><strong>Salary:</strong> ${formatSalary(job.minSalary)} - ${formatSalary(job.maxSalary)}</p>

      <button class="btn btn-primary apply-btn" data-id="${job.id}">
        Apply Now
      </button>
    `;

    card.querySelector(".apply-btn").addEventListener("click", () => {
      localStorage.setItem("pendingApplyJdId", job.id);
      const token = localStorage.getItem("accessToken");
      const role = localStorage.getItem("role");

      if (!token) {
        window.location.href = "./pages/login.html";
      } else if (role === "HR") {
        window.location.href = "./pages/jd-management.html";
      } else if (role === "CANDIDATE") {
        window.location.href = "./pages/candidate-dashboard.html";
      } else if (role === "PANEL") {
        window.location.href = "./pages/panel-dashboard.html";
      } else {
        window.location.href = "./pages/login.html";
      }
    });

    jobGrid.appendChild(card);
  });
}

async function loadPublicJobs() {
  try {
    loader.classList.remove("hidden");

    const res = await fetch(API.HR.JD_LIST);
    const result = await res.json();

    const jobs = result?.data || [];

    loader.classList.add("hidden");

    if (!jobs.length) {
      noJobs.classList.remove("hidden");
      return;
    }

    renderJobs(jobs);
  } catch (error) {
    loader.classList.add("hidden");
    noJobs.classList.remove("hidden");
    noJobs.textContent = "Unable to load jobs. Please check backend server.";
  }
}

function getDashboardPath(role) {
  if (role === "HR") return "./pages/jd-management.html";
  if (role === "CANDIDATE") return "./pages/candidate-dashboard.html";
  if (role === "PANEL") return "./pages/panel-dashboard.html";
  return "./pages/login.html";
}

function setupHomeAuthButtons() {
  const token = localStorage.getItem("accessToken");
  const role = localStorage.getItem("role");

  const navActions = document.querySelector(".nav-actions");
  const heroButtons = document.querySelector(".hero-buttons");

  if (!token || !role) return;

  const dashboardPath = getDashboardPath(role);

  if (navActions) {
    navActions.innerHTML = `
      <a href="${dashboardPath}" class="btn btn-primary">Dashboard</a>
    `;
  }

  if (heroButtons) {
    heroButtons.innerHTML = `
      <a href="${dashboardPath}" class="btn btn-primary">Go to Dashboard</a>
    `;
  }
}

document.addEventListener("DOMContentLoaded", () => {
  setupHomeAuthButtons();
  loadPublicJobs();
});
