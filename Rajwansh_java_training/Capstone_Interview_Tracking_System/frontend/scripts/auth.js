import { handleLogin } from "./pages/login.js";
import { setupPasswordToggle } from "./utils/password.js";

document.addEventListener("DOMContentLoaded", () => {
  setupPasswordToggle("toggle-password", "password");

  const loginForm = document.getElementById("login-form");

  if (loginForm) {
    loginForm.addEventListener("submit", handleLogin);
  }
});
