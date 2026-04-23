/// these are utility function for redirecting the user
export function redirectByRole(role) {
  const routes = {
    HR: "hr-dashboard.html",
    PANEL: "panel-dashboard.html",
    CANDIDATE: "candidate-dashboard.html",
  };

  window.location.href = routes[role] || "login.html";
}
