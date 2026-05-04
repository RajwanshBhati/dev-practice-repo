import { showToast } from "./utils/toast.js";
import { encryptPassword } from "./utils/password.js";

const API_BASE = "http://localhost:8080/api/v1/panel";

const form = document.getElementById("activate-form");

const tokenInput = document.getElementById("token");
const passwordInput = document.getElementById("new-password");
const confirmInput = document.getElementById("confirm-password");

const btn = document.getElementById("activate-btn");
const btnText = document.getElementById("btn-text");
const btnLoader = document.getElementById("btn-loader");

const alertBox = document.getElementById("alert-box");
const successState = document.getElementById("success-state");

const params = new URLSearchParams(window.location.search);
const urlToken = params.get("token");

if (!urlToken) {
  showToast("Activation token missing from link", "error");
  form.style.display = "none";
} else {
  tokenInput.value = urlToken;
}

function setupPasswordToggle(buttonId, inputId) {
  const button = document.getElementById(buttonId);
  const input = document.getElementById(inputId);

  if (!button || !input) return;

  const eyeOpen = `
    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
      <circle cx="12" cy="12" r="3"/>
    </svg>
  `;

  const eyeClosed = `
    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
      <path d="M17.94 17.94A10.94 10.94 0 0 1 12 19c-7 0-11-7-11-7a21.77 21.77 0 0 1 5.06-5.94M9.9 4.24A10.94 10.94 0 0 1 12 5c7 0 11 7 11 7a21.77 21.77 0 0 1-3.17 4.34M1 1l22 22"/>
    </svg>
  `;

  button.addEventListener("click", () => {
    const isPassword = input.type === "password";

    input.type = isPassword ? "text" : "password";

    button.innerHTML = isPassword ? eyeClosed : eyeOpen;
  });
}

setupPasswordToggle("toggle-new-password", "new-password");
setupPasswordToggle("toggle-confirm-password", "confirm-password");

function setLoading(state) {
  if (!btn) return;

  btn.disabled = state;

  if (state) {
    btnText.style.display = "none";
    btnLoader.style.display = "inline-flex";
  } else {
    btnText.style.display = "inline";
    btnLoader.style.display = "none";
  }
}

form.addEventListener("submit", async (e) => {
  e.preventDefault();

  const token = tokenInput.value.trim();
  const password = passwordInput.value.trim();
  const confirmPassword = confirmInput.value.trim();

  if (!token) {
    showToast("Invalid activation link", "error");
    return;
  }

  if (!password || !confirmPassword) {
    showToast("Password and confirm password are required", "error");
    return;
  }

  if (password.length < 6) {
    showToast("Password must be at least 6 characters", "error");
    return;
  }

  if (password !== confirmPassword) {
    showToast("Passwords do not match", "error");
    return;
  }

  try {
    setLoading(true);

    const encryptedPassword = await encryptPassword(password);
    const encryptedConfirmPassword = await encryptPassword(confirmPassword);

    const response = await fetch(`${API_BASE}/activate`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        token,
        password: encryptedPassword,
        confirmPassword: encryptedConfirmPassword,
      }),
    });

    const text = await response.text();

    if (!response.ok) {
      throw new Error(text || "Activation failed");
    }

    form.style.display = "none";
    if (alertBox) alertBox.style.display = "none";
    if (successState) successState.style.display = "block";

    showToast("Password set successfully. Redirecting to login...", "success");

    setTimeout(() => {
      window.location.href = "login.html?activated=true";
    }, 1200);
  } catch (error) {
    showToast(error.message || "Something went wrong", "error");
  } finally {
    setLoading(false);
  }
});
