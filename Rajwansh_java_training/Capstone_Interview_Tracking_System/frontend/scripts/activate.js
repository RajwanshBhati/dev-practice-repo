const API_BASE = "http://localhost:8080/api/v1/panel";

const form = document.getElementById("activate-form");

const tokenInput = document.getElementById("token");
const passwordInput = document.getElementById("new-password");
const confirmInput = document.getElementById("confirm-password");

const btn = document.getElementById("activate-btn");
const btnText = document.getElementById("btn-text");
const btnLoader = document.getElementById("btn-loader");

const alertBox = document.getElementById("alert-box");
const alertMessage = document.getElementById("alert-message");

const successState = document.getElementById("success-state");

const params = new URLSearchParams(window.location.search);
const urlToken = params.get("token");

if (urlToken) {
  tokenInput.value = urlToken;
}

function setupPasswordToggle(buttonId, inputId) {
  const button = document.getElementById(buttonId);
  const input = document.getElementById(inputId);

  if (!button || !input) return;

  button.addEventListener("click", () => {
    input.type = input.type === "password" ? "text" : "password";
  });
}

setupPasswordToggle("toggle-new-password", "new-password");
setupPasswordToggle("toggle-confirm-password", "confirm-password");
function showAlert(msg, type = "error") {
  alertBox.style.display = "flex";
  alertMessage.innerText = msg;
  alertBox.className = "alert " + type;
}

function setLoading(state) {
  if (state) {
    btn.disabled = true;
    btnText.style.display = "none";
    btnLoader.style.display = "inline-flex";
  } else {
    btn.disabled = false;
    btnText.style.display = "inline";
    btnLoader.style.display = "none";
  }
}

function checkStrength(pwd) {
  if (pwd.length < 6) return "Weak";
  if (pwd.length < 10) return "Medium";
  return "Strong";
}

passwordInput.addEventListener("input", () => {});

form.addEventListener("submit", async (e) => {
  e.preventDefault();

  const token = tokenInput.value.trim();
  const password = passwordInput.value.trim();
  const confirmPassword = confirmInput.value.trim();

  if (!token) return showAlert("Token missing");
  if (!password || !confirmPassword)
    return showAlert("All fields are required");

  if (password.length < 6) {
    return showAlert("Password must be at least 6 characters");
  }

  if (password !== confirmPassword) {
    return showAlert("Passwords do not match");
  }

  try {
    setLoading(true);

    const response = await fetch(`${API_BASE}/activate`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        token: token,
        password: password,
        confirmPassword: confirmPassword,
      }),
    });

    let data = {};
    try {
      data = await response.json();
    } catch (e) {}

    if (!response.ok) {
      throw new Error(data.message || "Activation failed");
    }

    form.style.display = "none";
    successState.style.display = "block";
    alertBox.style.display = "none";
  } catch (error) {
    showAlert(error.message || "Something went wrong");
  } finally {
    setLoading(false);
  }
});
