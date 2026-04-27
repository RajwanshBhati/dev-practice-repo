export function renderPanelTable(data, openFeedback) {
  const tbody = document.getElementById("panel-table-body");
  tbody.innerHTML = "";

  data.forEach((i) => {
    tbody.innerHTML += `
      <tr>
        <td>${i.candidateName}</td>
        <td>${i.candidateEmail}</td>
        <td>${i.candidateMobileNumber}</td>
        <td>${i.stage}</td>
        <td>${i.interviewDate}</td>
        <td>${i.interviewTime}</td>
        <td>${i.focusArea}</td>
        <td><a href="${i.resumeUrl}" target="_blank">View</a></td>

        <td>
          ${
            i.feedbackSubmitted
              ? "<span>Submitted</span>"
              : `<button onclick="window.openFeedback(${i.interviewId})">Give Feedback</button>`
          }
        </td>
      </tr>
    `;
  });
}
