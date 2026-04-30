import { registerCandidate } from "../candidate/candidate.js";

const form = document.getElementById("registerForm");
const msg = document.getElementById("msg");
const btn = document.getElementById("registerBtn");

function showMsg(message, type = "error") {
  if (!msg) return;
  msg.textContent = message;
  msg.className = type === "success" ? "msg-success" : "msg-error";
}

function setLoading(loading) {
  if (!btn) return;
  btn.disabled = loading;
  btn.textContent = loading ? "Creating account..." : "Create Account";
}

form?.addEventListener("submit", async (e) => {
  e.preventDefault();

  const fullName = document.getElementById("name").value.trim();
  const email = document.getElementById("email").value.trim().toLowerCase();
  const mobileNumber = document.getElementById("mobileNumber").value.trim();
  const dob = document.getElementById("dob").value;
  const password = document.getElementById("password").value;
  const confirmPassword = document.getElementById("confirmPassword").value;

  showMsg("");

  if (
    !fullName ||
    !email ||
    !mobileNumber ||
    !dob ||
    !password ||
    !confirmPassword
  ) {
    showMsg("All fields are required.");
    return;
  }

  if (!/^\S+@\S+\.\S+$/.test(email)) {
    showMsg("Enter a valid email address.");
    return;
  }

  if (!/^[6-9]\d{9}$/.test(mobileNumber)) {
    showMsg("Enter a valid 10-digit mobile number.");
    return;
  }

  if (password.length < 8) {
    showMsg("Password must be at least 8 characters.");
    return;
  }

  if (password !== confirmPassword) {
    showMsg("Passwords do not match.");
    return;
  }

  setLoading(true);

  try {
    const data = await registerCandidate({
      fullName,
      email,
      mobileNumber,
      dob,
      password,
      confirmPassword,
    });

    if (!data?.success) {
      showMsg(data?.message || "Registration failed.");
      return;
    }

    showMsg("Account created successfully. Redirecting...", "success");

    setTimeout(() => {
      window.location.href = "login.html";
    }, 900);
  } catch (err) {
    showMsg(err.message || "Registration failed.");
  } finally {
    setLoading(false);
  }
});
