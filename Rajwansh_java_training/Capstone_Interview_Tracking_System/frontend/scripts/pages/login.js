import { loginAPI } from "../services/authService.js";
import { storeAuthData } from "../utils/authStorage.js";
import { redirectByRole } from "../utils/redirect.js";
import { encryptPassword } from "../utils/password.js";

const form = document.getElementById("loginForm");
const message = document.getElementById("loginMessage");
const loginBtn = document.getElementById("login-btn");

function showMessage(text, type = "error") {
  if (!message) return;
  message.textContent = text;
  message.className =
    type === "success" ? "message msg-success" : "message msg-error";
}

const params = new URLSearchParams(window.location.search);

if (params.get("activated") === "true") {
  showMessage("Password set successfully. Please login.", "success");
}

function setLoginLoading(loading) {
  if (!loginBtn) return;
  loginBtn.disabled = loading;
  loginBtn.textContent = loading ? "Logging in..." : "Login";
}

form?.addEventListener("submit", async (e) => {
  e.preventDefault();

  const email = document.getElementById("email").value.trim().toLowerCase();
  const password = document.getElementById("password").value;

  showMessage("");

  if (!email || !password) {
    showMessage("Email and password are required.");
    return;
  }

  if (!/^\S+@\S+\.\S+$/.test(email)) {
    showMessage("Enter a valid email address.");
    return;
  }

  setLoginLoading(true);

  try {
    const encryptedPassword = await encryptPassword(password);
    const result = await loginAPI(email, encryptedPassword);

    if (!result?.success || !result?.data) {
      showMessage(result?.message || "Login failed.");
      return;
    }

    storeAuthData(result.data);
    showMessage("Login successful. Redirecting...", "success");

    setTimeout(() => {
      const pendingApplyJdId = localStorage.getItem("pendingApplyJdId");

      if (
        pendingApplyJdId &&
        String(result.data.role).toLowerCase() === "candidate"
      ) {
        window.location.href = "candidate-dashboard.html";
        return;
      }

      redirectByRole(result.data.role);
    }, 700);
  } catch (err) {
    showMessage(err.message || "Login failed.");
  } finally {
    setLoginLoading(false);
  }
});
