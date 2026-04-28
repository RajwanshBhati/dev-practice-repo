import { registerCandidate } from "../candidate/candidate.js";

function showMsg(id, msg, type) {
  const el = document.getElementById(id);
  if (!el) return;
  el.textContent = msg;
  el.className = `alert alert-${type}`;
  el.classList.remove("hidden");
}

function hideMsg(id) {
  const el = document.getElementById(id);
  if (el) el.classList.add("hidden");
}

function setLoading(loading) {
  const btn = document.getElementById("registerBtn");
  if (!btn) return;
  btn.disabled = loading;
  btn.textContent = loading ? "Creating account…" : "Create Account";
}

document.addEventListener("DOMContentLoaded", () => {
  // Password toggle
  document.querySelectorAll(".eye-btn").forEach((btn) => {
    btn.addEventListener("click", () => {
      const input = document.getElementById(btn.dataset.target);
      if (!input) return;
      input.type = input.type === "password" ? "text" : "password";
      btn.textContent = input.type === "password" ? "👁" : "🙈";
    });
  });

  // Form submit
  document
    .getElementById("registerForm")
    ?.addEventListener("submit", async (e) => {
      e.preventDefault();
      hideMsg("errorMsg");
      hideMsg("successMsg");

      const fullName = document.getElementById("fullName")?.value.trim();
      const email = document.getElementById("email")?.value.trim();
      const password = document.getElementById("password")?.value.trim();
      const confirmPassword = document
        .getElementById("confirmPassword")
        ?.value.trim();

      // Validation
      if (!fullName || !email || !password || !confirmPassword) {
        showMsg("errorMsg", "All fields are required.", "error");
        return;
      }

      if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        showMsg("errorMsg", "Enter a valid email address.", "error");
        return;
      }

      const passwordRegex =
        /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/;

      if (!passwordRegex.test(password)) {
        showMsg(
          "errorMsg",
          "Password must be at least 8 characters and include uppercase, lowercase, number, and special character.",
          "error",
        );
        return;
      }

      if (password !== confirmPassword) {
        showMsg("errorMsg", "Passwords do not match.", "error");
        return;
      }

      setLoading(true);

      try {
        const data = await registerCandidate({
          fullName,
          email,
          password,
          confirmPassword,
        });

        if (data.success) {
          showMsg(
            "successMsg",
            "Account created! Redirecting to login…",
            "success",
          );
          setTimeout(() => {
            window.location.href = "login.html";
          }, 1500);
        } else {
          showMsg("errorMsg", data.message || "Registration failed.", "error");
        }
      } catch (err) {
        showMsg("errorMsg", "Cannot connect to server.", "error");
      } finally {
        setLoading(false);
      }
    });
});
