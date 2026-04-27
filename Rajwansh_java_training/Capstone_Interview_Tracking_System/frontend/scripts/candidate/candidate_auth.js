function togglePwd(id, btn) {
  const inp = document.getElementById(id);
  inp.type = inp.type === "password" ? "text" : "password";
  btn.textContent = inp.type === "password";
}

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("registerForm");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const errDiv = document.getElementById("errorMsg");
    const sucDiv = document.getElementById("successMsg");

    errDiv.classList.add("hidden");
    sucDiv.classList.add("hidden");

    const fullName = document.getElementById("fullName").value.trim();
    const email = document.getElementById("email").value.trim();
    const mobileCode = document.getElementById("mobileCode").value;
    const mobile = document.getElementById("mobile").value.trim();
    const dob = document.getElementById("dob").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    if (!fullName || !email || !mobile || !password) {
      errDiv.textContent = "Please fill all required fields.";
      errDiv.classList.remove("hidden");
      return;
    }

    if (password !== confirmPassword) {
      errDiv.textContent = "⚠ Passwords do not match.";
      errDiv.classList.remove("hidden");
      return;
    }

    if (password.length < 8) {
      errDiv.textContent = "Password must be at least 8 characters.";
      errDiv.classList.remove("hidden");
      return;
    }

    const btn = document.getElementById("registerBtn");
    btn.disabled = true;
    btn.textContent = "Creating account…";

    const result = await registerCandidate({
      fullName,
      email,
      mobileCode,
      mobile,
      dob,
      password,
    });

    if (result.success) {
      sucDiv.textContent = "Account created! Redirecting to login…";
      sucDiv.classList.remove("hidden");

      setTimeout(() => {
        window.location.href = "login.html";
      }, 2000);
    } else {
      errDiv.textContent = result.message || "Registration failed.";
      errDiv.classList.remove("hidden");

      btn.disabled = false;
      btn.textContent = "Create Account";
    }
  });
});
