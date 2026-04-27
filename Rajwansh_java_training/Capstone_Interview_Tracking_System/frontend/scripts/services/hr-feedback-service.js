export async function fetchCandidateFeedback(candidateId) {
  const res = await fetch(
    `http://localhost:8080/api/hr/feedback/${candidateId}`,
    {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("accessToken"),
      },
    },
  );

  if (!res.ok) throw new Error("Failed to load feedback");

  return res.json();
}
