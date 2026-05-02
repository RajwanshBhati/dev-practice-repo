import { showToast } from "../utils/toast.js";

const API = "http://localhost:8080/api/v1/panel";

const form = document.getElementById("panel-form");
const tableBody = document.getElementById("panel-table-body");

function getToken() {
  return localStorage.getItem("accessToken");
}

function getValue(id) {
  const element = document.getElementById(id);
  return element ? element.value.trim() : "";
}

if (form) {
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const payload = {
      fullName: getValue("name"),
      email: getValue("email"),
      mobile: getValue("mobile"),
      organization: getValue("organization"),
      designation: getValue("designation"),
    };

    if (
      !payload.fullName ||
      !payload.email ||
      !payload.mobile ||
      !payload.organization ||
      !payload.designation
    ) {
      showToast("Please fill all required fields", "error");
      return;
    }

    const btn = form.querySelector("button[type='submit']");
    const oldText = btn?.textContent || "Create Panel";

    if (btn) {
      btn.disabled = true;
      btn.textContent = "Creating...";
    }

    try {
      const res = await fetch(`${API}/create`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${getToken()}`,
        },
        body: JSON.stringify(payload),
      });

      let data = {};
      try {
        data = await res.json();
      } catch {}

      if (!res.ok) {
        showToast(data.message || "Error creating panel", "error");
        return;
      }

      showToast("Panel created and activation email sent!", "success");
      form.reset();
      loadPanels();
    } catch (err) {
      showToast("Server error", "error");
    } finally {
      if (btn) {
        btn.disabled = false;
        btn.textContent = oldText;
      }
    }
  });
}

async function loadPanels() {
  try {
    const token = getToken();

    if (!token || !tableBody) return;

    const res = await fetch(`${API}/list`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!res.ok) return;

    const data = await res.json();

    tableBody.innerHTML = "";

    if (!data || data.length === 0) {
      tableBody.innerHTML = `
        <tr>
          <td colspan="5">No panel members found</td>
        </tr>
      `;
      return;
    }

    data.forEach((panel) => {
      const row = document.createElement("tr");

      row.innerHTML = `
        <td>${panel.fullName || "-"}</td>
        <td>${panel.email || "-"}</td>
        <td>${panel.mobile || "-"}</td>
        <td>${panel.organization || "-"}</td>
        <td>${panel.designation || "-"}</td>
      `;

      tableBody.appendChild(row);
    });
  } catch (err) {
    console.error("Error loading panels:", err);
  }
}

document.addEventListener("DOMContentLoaded", loadPanels);

window.loadPanels = loadPanels;
