const API_BASE = "http://localhost:8080/api";

/* Utility: Alert Box */

/**
 * Displays alert box with given message and type.
 * @param {string} message
 * @param {'error'|'success'} type
 */
function showAlert(message, type = "error") {
  const box = document.getElementById("alert-box");
  const msg = document.getElementById("alert-message");
  const icon = document.getElementById("alert-icon");

  if (!box) return;

  box.className = `alert alert-${type}`;
  msg.textContent = message;
  icon.textContent = type === "success" ? "✓" : "!";
  box.style.display = "flex";

  /* Auto-hide success alerts after 4 seconds */
  if (type === "success") {
    setTimeout(() => {
      box.style.display = "none";
    }, 4000);
  }
}

/**
 * Hides the alert box.
 */
function hideAlert() {
  const box = document.getElementById("alert-box");
  if (box) box.style.display = "none";
}

/* Utility: Form Validation and Feedback */

/**
 * Shows field-level error message.
 * @param {string} fieldId  - input element id
 * @param {string} message  - error text
 */
function showFieldError(fieldId, message) {
  const input = document.getElementById(fieldId);
  const error = document.getElementById(`${fieldId}-error`);
  if (input) input.classList.add("input-error");
  if (error) error.textContent = message;
}

/**
 * Clears field-level error.
 * @param {string} fieldId
 */
function clearFieldError(fieldId) {
  const input = document.getElementById(fieldId);
  const error = document.getElementById(`${fieldId}-error`);
  if (input) {
    input.classList.remove("input-error");
    input.classList.remove("input-success");
  }
  if (error) error.textContent = "";
}

/**
 * Marks field as valid with green border.
 * @param {string} fieldId
 */
function markFieldSuccess(fieldId) {
  const input = document.getElementById(fieldId);
  if (input) {
    input.classList.remove("input-error");
    input.classList.add("input-success");
  }
}

/* Utility: Button Loading State */

/**
 * Toggles button between loading and normal state.
 * @param {string}  btnId    - button element id
 * @param {boolean} loading
 */
function setLoading(btnId, loading) {
  const btn = document.getElementById(btnId);
  const text = document.getElementById("btn-text");
  const loader = document.getElementById("btn-loader");

  if (!btn) return;

  btn.disabled = loading;
  if (text) text.style.display = loading ? "none" : "inline";
  if (loader) loader.style.display = loading ? "flex" : "none";
}

/* Utility: Password Show/Hide Toggle */

/**
 * Sets up show/hide toggle for a password input.
 * @param {string} toggleBtnId - id of toggle button
 * @param {string} inputId     - id of password input
 */
function setupPasswordToggle(toggleBtnId, inputId) {
  const btn = document.getElementById(toggleBtnId);
  const input = document.getElementById(inputId);
  if (!btn || !input) return;

  btn.addEventListener("click", () => {
    const isPassword = input.type === "password";
    input.type = isPassword ? "text" : "password";

    /* Swap eye icon */
    btn.innerHTML = isPassword
      ? /* Eye-off icon */
        `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor"
              stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
           <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8
                    a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0
                    1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07
                    a3 3 0 1 1-4.24-4.24"/>
           <line x1="1" y1="1" x2="23" y2="23"/>
         </svg>`
      : /* Eye icon */
        `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor"
              stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
           <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
           <circle cx="12" cy="12" r="3"/>
         </svg>`;
  });
}

/* Utility: Password Strength Meter */

/**
 * Evaluates password strength and updates the strength bar UI.
 * @param {string} password
 */
function evaluatePasswordStrength(password) {
  const fill = document.getElementById("strength-fill");
  const label = document.getElementById("strength-label");
  if (!fill || !label) return;

  if (password.length === 0) {
    fill.style.width = "0%";
    label.textContent = "";
    return;
  }

  let score = 0;
  if (password.length >= 8) score++;
  if (password.length >= 12) score++;
  if (/[A-Z]/.test(password)) score++;
  if (/[0-9]/.test(password)) score++;
  if (/[^A-Za-z0-9]/.test(password)) score++;

  const levels = [
    { pct: "20%", cls: "strength-weak", text: "Weak" },
    { pct: "40%", cls: "strength-weak", text: "Weak" },
    { pct: "60%", cls: "strength-fair", text: "Fair" },
    { pct: "80%", cls: "strength-good", text: "Good" },
    { pct: "100%", cls: "strength-strong", text: "Strong" },
  ];

  const level = levels[Math.min(score, levels.length) - 1] || levels[0];

  fill.style.width = level.pct;
  fill.className = `strength-fill ${level.cls}`;
  label.textContent = level.text;
  label.style.color = getComputedStyle(fill).backgroundColor;
}

/* Utility: Role-Based Redirection */

/**
 * Redirects user to their role-specific dashboard.
 * @param {'HR'|'PANEL'|'CANDIDATE'} role
 */
function redirectByRole(role) {
  const routes = {
    HR: "hr-dashboard.html",
    PANEL: "panel-dashboard.html",
    CANDIDATE: "candidate-dashboard.html",
  };
  const path = routes[role] || "login.html";
  window.location.href = path;
}

/* Utility: Auth Data Storage */

/**
 * Saves auth data to localStorage after successful login.
 * @param {object} data - API response data object
 */
function storeAuthData(data) {
  localStorage.setItem("accessToken", data.accessToken);
  localStorage.setItem("refreshToken", data.refreshToken);
  localStorage.setItem("role", data.role);
  localStorage.setItem("name", data.name);
  localStorage.setItem("email", data.email);
}

/* Login Flow */

/**
 * Validates login form inputs.
 * @returns {boolean} true if valid
 */
function validateLoginForm() {
  let valid = true;

  const email = document.getElementById("email");
  const password = document.getElementById("password");

  clearFieldError("email");
  clearFieldError("password");
  hideAlert();

  if (!email || !email.value.trim()) {
    showFieldError("email", "Email is required");
    valid = false;
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value.trim())) {
    showFieldError("email", "Enter a valid email address");
    valid = false;
  } else {
    markFieldSuccess("email");
  }

  if (!password || !password.value) {
    showFieldError("password", "Password is required");
    valid = false;
  } else if (password.value.length < 6) {
    showFieldError("password", "Password must be at least 6 characters");
    valid = false;
  } else {
    markFieldSuccess("password");
  }

  return valid;
}

/**
 * Submits login form to backend and handles response.
 * @param {Event} e - form submit event
 */
async function handleLogin(e) {
  e.preventDefault();

  if (!validateLoginForm()) return;

  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value;

  setLoading("login-btn", true);

  try {
    const response = await fetch(`${API_BASE}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });

    const result = await response.json();

    if (response.ok && result.success) {
      storeAuthData(result.data);
      showAlert("Login successful! Redirecting...", "success");
      setTimeout(() => redirectByRole(result.data.role), 1000);
    } else {
      /* Backend returned error message */
      const message = result.message || "Invalid email or password";
      showAlert(message, "error");
    }
  } catch (error) {
    /* Network error or server down */
    showAlert("Unable to connect to server. Please try again.", "error");
    console.error("Login error:", error);
  } finally {
    setLoading("login-btn", false);
  }
}

/* Activation Flow */

/**
 * Validates activation form inputs.
 * @returns {boolean} true if valid
 */
function validateActivateForm() {
  let valid = true;

  const token = document.getElementById("token");
  const newPassword = document.getElementById("new-password");
  const confirmPassword = document.getElementById("confirm-password");

  clearFieldError("token");
  clearFieldError("new-password");
  clearFieldError("confirm-password");
  hideAlert();

  if (!token || !token.value.trim()) {
    showFieldError("token", "Activation token is required");
    valid = false;
  } else {
    markFieldSuccess("token");
  }

  if (!newPassword || !newPassword.value) {
    showFieldError("new-password", "Password is required");
    valid = false;
  } else if (newPassword.value.length < 8) {
    showFieldError("new-password", "Password must be at least 8 characters");
    valid = false;
  } else {
    markFieldSuccess("new-password");
  }

  if (!confirmPassword || !confirmPassword.value) {
    showFieldError("confirm-password", "Please confirm your password");
    valid = false;
  } else if (
    newPassword &&
    newPassword.value &&
    confirmPassword.value !== newPassword.value
  ) {
    showFieldError("confirm-password", "Passwords do not match");
    valid = false;
  } else if (confirmPassword.value) {
    markFieldSuccess("confirm-password");
  }

  return valid;
}

/**
 * Submits activation form to backend.
 * @param {Event} e - form submit event
 */
async function handleActivate(e) {
  e.preventDefault();

  if (!validateActivateForm()) return;

  const token = document.getElementById("token").value.trim();
  const newPassword = document.getElementById("new-password").value;
  const confirmPassword = document.getElementById("confirm-password").value;

  setLoading("activate-btn", true);

  try {
    const response = await fetch(`${API_BASE}/auth/activate`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ token, newPassword, confirmPassword }),
    });

    const result = await response.json();

    if (response.ok && result.success) {
      /* Hide form, show success state */
      document.getElementById("activate-form").style.display = "none";
      document.getElementById("success-state").style.display = "block";
    } else {
      const message =
        result.message || "Activation failed. Please check your token.";
      showAlert(message, "error");
    }
  } catch (error) {
    showAlert("Unable to connect to server. Please try again.", "error");
    console.error("Activation error:", error);
  } finally {
    setLoading("activate-btn", false);
  }
}

/* Prefill Token from URL*/

/**
 * If activation page is opened with ?token in URL,
 * auto-fills the token input field.
 */
function prefillTokenFromURL() {
  const params = new URLSearchParams(window.location.search);
  const token = params.get("token");
  const input = document.getElementById("token");
  if (token && input) {
    input.value = token;
    markFieldSuccess("token");
  }
}

/* Check Already Logged In */

/**
 * If user already has a valid token in localStorage,
 * redirect them directly to their dashboard.
 */
function checkAlreadyLoggedIn() {
  const token = localStorage.getItem("accessToken");
  const role = localStorage.getItem("role");
  if (token && role) {
    redirectByRole(role);
  }
}

/* Initialize Event Listeners */

document.addEventListener("DOMContentLoaded", () => {
  const loginForm = document.getElementById("login-form");
  const activateForm = document.getElementById("activate-form");

  /* ── Login Page ── */
  if (loginForm) {
    checkAlreadyLoggedIn();

    loginForm.addEventListener("submit", handleLogin);

    setupPasswordToggle("toggle-password", "password");

    /* Clear errors on input */
    document
      .getElementById("email")
      ?.addEventListener("input", () => clearFieldError("email"));
    document
      .getElementById("password")
      ?.addEventListener("input", () => clearFieldError("password"));
  }

  /* ── Activate Page ── */
  if (activateForm) {
    prefillTokenFromURL();

    activateForm.addEventListener("submit", handleActivate);

    setupPasswordToggle("toggle-new-password", "new-password");

    /* Password strength meter */
    document.getElementById("new-password")?.addEventListener("input", (e) => {
      evaluatePasswordStrength(e.target.value);
      clearFieldError("new-password");
    });

    /* Live confirm match check */
    document
      .getElementById("confirm-password")
      ?.addEventListener("input", (e) => {
        const np = document.getElementById("new-password")?.value;
        if (e.target.value && np && e.target.value !== np) {
          showFieldError("confirm-password", "Passwords do not match");
        } else {
          clearFieldError("confirm-password");
          if (e.target.value) markFieldSuccess("confirm-password");
        }
      });

    document
      .getElementById("token")
      ?.addEventListener("input", () => clearFieldError("token"));
  }
});
