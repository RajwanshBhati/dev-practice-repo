export async function fetchPanelInterviews() {
  const res = await fetch("http://localhost:8080/api/v1/panel/interviews", {
    headers: {
      Authorization: "Bearer " + localStorage.getItem("accessToken"),
    },
  });

  if (!res.ok) throw new Error("Failed to load interviews");

  return res.json();
}

export async function submitFeedbackAPI(payload) {
  const res = await fetch("http://localhost:8080/api/interview/feedback", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + localStorage.getItem("accessToken"),
    },
    body: JSON.stringify(payload),
  });

  if (!res.ok) {
    const errorText = await res.text();
    console.error("Feedback API failed:", res.status, errorText);
    throw new Error("Feedback failed");
  }
}
