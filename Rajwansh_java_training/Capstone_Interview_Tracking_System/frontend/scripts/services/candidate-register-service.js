import { registerCandidate } from "../candidate/candidate.js";

const form = document.getElementById("registerForm");
const btn = document.getElementById("registerBtn");

function showToast(message, type = "success") {
  const toast = document.getElementById("toast");

  if (!toast) {
    alert(message);
    return;
  }

  toast.textContent = message;
  toast.className = `toast show ${type}`;

  setTimeout(() => {
    toast.className = "toast";
  }, 3000);
}

function setError(id, message) {
  const input = document.getElementById(id);
  const error = document.getElementById(`${id}Error`);

  if (input) input.classList.add("input-error");
  if (error) error.textContent = message;
}

function clearError(id) {
  const input = document.getElementById(id);
  const error = document.getElementById(`${id}Error`);

  if (input) input.classList.remove("input-error");
  if (error) error.textContent = "";
}

function clearAllErrors() {
  ["name", "email", "mobileNumber", "dob", "gender"].forEach(clearError);
}

function isValidAge(dob) {
  if (!dob) return false;

  const birthDate = new Date(dob);
  const today = new Date();

  let age = today.getFullYear() - birthDate.getFullYear();
  const monthDiff = today.getMonth() - birthDate.getMonth();

  if (
    monthDiff < 0 ||
    (monthDiff === 0 && today.getDate() < birthDate.getDate())
  ) {
    age--;
  }

  return age >= 18;
}

function setLoading(loading) {
  if (!btn) return;

  btn.disabled = loading;
  btn.textContent = loading ? "Creating account..." : "Create Account";
}

function showServerError(message) {
  const lowerMessage = message.toLowerCase();

  showToast(message, "error");

  if (lowerMessage.includes("mobile")) {
    setError("mobileNumber", message);
    return;
  }

  if (lowerMessage.includes("email")) {
    setError("email", message);
    return;
  }
}

form?.addEventListener("submit", async (e) => {
  e.preventDefault();

  clearAllErrors();

  const fullName = document.getElementById("name")?.value.trim();
  const email = document.getElementById("email")?.value.trim().toLowerCase();
  const mobileNumber = document.getElementById("mobileNumber")?.value.trim();
  const dob = document.getElementById("dob")?.value;
  const gender = document.getElementById("gender")?.value;

  let hasError = false;

  if (!fullName) {
    setError("name", "Name is required");
    hasError = true;
  }

  if (!email) {
    setError("email", "Email is required");
    hasError = true;
  } else if (!/^\S+@\S+\.\S+$/.test(email)) {
    setError("email", "Invalid email");
    hasError = true;
  }

  if (!mobileNumber) {
    setError("mobileNumber", "Mobile number is required");
    hasError = true;
  } else if (!/^[6-9]\d{9}$/.test(mobileNumber)) {
    setError("mobileNumber", "Enter a valid 10-digit mobile number");
    hasError = true;
  }

  if (!dob) {
    setError("dob", "Date of birth is required");
    hasError = true;
  } else if (!isValidAge(dob)) {
    setError("dob", "Candidate must be at least 18 years old");
    hasError = true;
  }

  if (!gender) {
    setError("gender", "Gender is required");
    hasError = true;
  }

  if (hasError) return;

  setLoading(true);

  try {
    const data = await registerCandidate({
      fullName,
      email,
      mobileNumber,
      dob,
      gender,
    });

    if (data?.success === false) {
      showServerError(data?.message || "Registration failed.");
      return;
    }

    showToast(
      "Account created successfully Check Mail. Redirecting...",
      "success",
    );

    setTimeout(() => {
      window.location.href = "./login.html";
    }, 1000);
  } catch (err) {
    showServerError(err.message || "Registration failed.");
  } finally {
    setLoading(false);
  }
});
