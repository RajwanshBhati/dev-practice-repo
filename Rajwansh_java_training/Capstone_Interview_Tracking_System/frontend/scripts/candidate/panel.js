const API = "http://localhost:8080/api/v1/panel";

const form = document.getElementById("panel-form");
const tableBody = document.getElementById("panel-table-body");

function getToken() {
  return localStorage.getItem("accessToken");
}

// Here I create a panel
if (form) {
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const payload = {
      fullName: document.getElementById("name").value.trim(),
      email: document.getElementById("email").value.trim(),
      mobile: document.getElementById("mobile").value.trim(),
      organization: document.getElementById("organization").value.trim(),
      designation: document.getElementById("designation").value.trim(),
    };

    console.log("Sending Payload:", payload);

    try {
      const res = await fetch(`${API}/create`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + getToken(),
        },
        body: JSON.stringify(payload),
      });

      let data = {};
      try {
        data = await res.json();
      } catch {}

      if (!res.ok) {
        alert(data.message || "Error creating panel");
        return;
      }

      alert("Panel created + activation email sent!");
      form.reset();
      loadPanels();
    } catch (err) {
      console.error(err);
      alert(" Server error");
    }
  });
}

async function loadPanels() {
  try {
    const token = getToken();

    if (!token) {
      console.error("❌ No token found");
      return;
    }

    const res = await fetch(`${API}/list`, {
      method: "GET",
      headers: {
        Authorization: "Bearer " + token,
      },
    });

    if (!res.ok) {
      console.error("Failed:", res.status);
      return;
    }

    const data = await res.json();
    console.log("Panels:", data);

    if (!tableBody) return;

    tableBody.innerHTML = "";

    if (!data || data.length === 0) {
      tableBody.innerHTML = `<tr><td colspan="5">No panel members found</td></tr>`;
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
    console.error(" Error loading panels", err);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  loadPanels();
});
