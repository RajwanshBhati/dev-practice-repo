/**
 * Renders candidate table for HR dashboard.
 */
export function renderCandidateTable(candidates) {
  const tbody = document.getElementById("candidate-table-body");

  tbody.innerHTML = "";

  candidates.forEach((candidate) => {
    const row = document.createElement("tr");

    row.innerHTML = `
      <td>${candidate.name || "-"}</td>
      <td>${candidate.email || "-"}</td>
      <td>${candidate.totalExperience || "-"}</td>
      <td>${candidate.currentCompany || "-"}</td>
      <td>${candidate.status || "-"}</td>

      <td>
        <button class="btn-reject" data-id="${candidate.id}">
          Reject
        </button>

        <button class="btn-l1" data-id="${candidate.id}">
          Send L1
        </button>

        <button class="btn-l2" data-id="${candidate.id}">
          Send L2
        </button>
      </td>
    `;

    tbody.appendChild(row);
  });
}

/**
 * Shows simple alert message.
 */
export function showMessage(message) {
  alert(message);
}
