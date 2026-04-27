/// these are utility function for redirecting the user
export function redirectByRole(role) {
  const routes = {
    hr: "jd-management.html",
    panel: "panel-dashboard.html",
    candidate: "candidate-dashboard.html",
  };

  window.location.href = routes[role.toLowerCase()] || "login.html";
}
