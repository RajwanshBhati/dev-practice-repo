export function getToken() {
  return localStorage.getItem("accessToken");
}

export function getName() {
  return localStorage.getItem("name") || "Candidate";
}
