import {
  loadPanelDashboard,
  submitFeedback,
  getPanelInterviewData,
} from "../actions/panel-actions.js";

import { clearAuthData, getName } from "../utils/authStorage.js";
import { fetchJDs } from "../services/jd.service.js";
import { renderPanelTable } from "../ui/panel-ui.js";

function formatSalary(value) {
  if (value === null || value === undefined || value === "") return "-";
  return `₹${Number(value).toLocaleString("en-IN")}`;
}

function renderPanelJDs(jobs = []) {
  const grid = document.getElementById("panel-jd-grid");
  if (!grid) return;

  if (!Array.isArray(jobs) || jobs.length === 0) {
    grid.innerHTML = `
      <div class="panel-empty-state jd-empty">
        <div class="empty-icon">📄</div>
        <h3>No JDs available</h3>
        <p>Job descriptions will appear here once HR creates them.</p>
      </div>
    `;
    return;
  }

  grid.innerHTML = jobs
    .map((job) => {
      const skills = Array.isArray(job.skillsRequired)
        ? job.skillsRequired
        : [];

      return `
        <article class="panel-jd-card">
          <div class="jd-card-top">
            <div>
              <h3>${job.jobTitle || "Untitled Job"}</h3>
              <p>${job.location || "-"}</p>
            </div>
            <span class="jd-status">${job.status || "ACTIVE"}</span>
          </div>

          <p class="job-desc">
            ${job.jobDescription || "No description available."}
          </p>

          <div class="jd-info-grid">
            <div>
              <span>Job Type</span>
              <strong>${job.jobType || "-"}</strong>
            </div>
            <div>
              <span>Experience</span>
              <strong>${job.minExperience || 0}-${job.maxExperience || 0} yrs</strong>
            </div>
            <div>
              <span>Salary</span>
              <strong>${formatSalary(job.minSalary)} - ${formatSalary(job.maxSalary)}</strong>
            </div>
          </div>

          <div class="jd-skills-simple">
            <strong>Skills:</strong>
            <span>${skills.length ? skills.join(", ") : "-"}</span>
          </div>

          <button class="view-only-btn" disabled>View Only</button>
        </article>
      `;
    })
    .join("");
}

document.addEventListener("DOMContentLoaded", async () => {
  await loadPanelDashboard();

  const interviewSection = document.getElementById("panel-interview-section");
  const jdSection = document.getElementById("panel-jd-section");
  const assignText = document.getElementById("assign");

  let jdLoaded = false;

  document.querySelectorAll(".nav-item").forEach((item) => {
    item.addEventListener("click", async () => {
      document.querySelectorAll(".nav-item").forEach((nav) => {
        nav.classList.remove("active");
      });

      item.classList.add("active");

      const page = item.dataset.page;
      const filter = item.dataset.filter || "all";

      if (page === "all-jds") {
        interviewSection.classList.add("hidden");
        jdSection.classList.remove("hidden");

        if (!jdLoaded) {
          const result = await fetchJDs();
          renderPanelJDs(result?.data || result || []);
          jdLoaded = true;
        }

        return;
      }

      interviewSection.classList.remove("hidden");
      jdSection.classList.add("hidden");

      if (assignText) {
        if (filter === "submitted")
          assignText.textContent = "Submitted Feedback";
        else if (filter === "pending")
          assignText.textContent = "Pending Feedback";
        else assignText.textContent = "Assigned Interviews";
      }

      renderPanelTable(getPanelInterviewData(), filter);
    });
  });

  const userName = getName();
  document.getElementById("user-name").textContent = userName || "Panel Member";
  document.getElementById("user-avatar").textContent = (userName || "P")
    .charAt(0)
    .toUpperCase();

  document.getElementById("logout-btn").addEventListener("click", () => {
    clearAuthData();
    window.location.href = "./login.html";
  });

  const form = document.getElementById("feedback-form");

  if (form) {
    form.addEventListener("submit", async (e) => {
      e.preventDefault();
      await submitFeedback();
    });
  }
});
