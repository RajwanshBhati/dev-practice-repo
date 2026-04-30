import {
  loadPanelDashboard,
  submitFeedback,
} from "../actions/panel-actions.js";

import { clearAuthData, getName } from "../utils/authStorage.js";
import { fetchJDs } from "../services/jd.service.js";

function renderPanelJDs(jobs = []) {
  const grid = document.getElementById("panel-jd-grid");
  if (!grid) return;

  if (!Array.isArray(jobs) || jobs.length === 0) {
    grid.innerHTML = `<p class="panel-empty-state">No JDs available.</p>`;
    return;
  }

  grid.innerHTML = jobs
    .map(
      (job) => `
        <div class="panel-jd-card">
          <h3>${job.jobTitle || "Untitled Job"}</h3>

          <div class="job-meta">
            <span>${job.location || "-"}</span>
            <span>${job.jobType || "-"}</span>
            <span>${job.minExperience || 0}-${job.maxExperience || 0} yrs</span>
          </div>

          <p class="job-desc">${job.jobDescription || "No description available."}</p>

          <div class="skills">
            ${(job.skillsRequired || [])
              .slice(0, 4)
              .map((skill) => `<span class="skill">${skill}</span>`)
              .join("")}
          </div>

          <p class="salary-text">
            <strong>Salary:</strong> ₹${job.minSalary || "-"} - ₹${job.maxSalary || "-"}
          </p>

          <button class="view-only-btn" disabled>View Only</button>
        </div>
      `,
    )
    .join("");
}

document.addEventListener("DOMContentLoaded", async () => {
  await loadPanelDashboard();

  const interviewSection = document.getElementById("panel-interview-section");
  const jdSection = document.getElementById("panel-jd-section");
  let jdLoaded = false;

  document.querySelectorAll(".nav-item").forEach((item) => {
    item.addEventListener("click", async () => {
      document.querySelectorAll(".nav-item").forEach((nav) => {
        nav.classList.remove("active");
      });

      item.classList.add("active");

      const page = item.dataset.page;
      const filter = item.dataset.filter;

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

      document.querySelectorAll("#panel-table-body tr").forEach((row) => {
        const isSubmitted = row.querySelector(".submitted-chip");
        const isPending = row.querySelector(".btn-feedback");

        if (filter === "all") row.style.display = "";
        if (filter === "submitted")
          row.style.display = isSubmitted ? "" : "none";
        if (filter === "pending") row.style.display = isPending ? "" : "none";
      });
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
