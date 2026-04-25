import { showAlert, hideAlert } from "../utils/alert.js";
import {
  showFieldError,
  clearFieldError,
  markFieldSuccess,
} from "../utils/fieldValidation.js";
import { setLoading } from "../utils/loader.js";
import { loginAPI } from "../services/authService.js";
import { storeAuthData } from "../utils/authStorage.js";
import { redirectByRole } from "../utils/redirect.js";

export async function handleLogin(e) {
  e.preventDefault();

  // Get form values and trim whitespace
  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value;

  hideAlert();

  let valid = true;

  // Basic validation
  if (!email) {
    showFieldError("email", "Email required");
    valid = false;
  }

  if (email && !/^\S+@\S+\.\S+$/.test(email)) {
    showFieldError("email", "Invalid email format");
    valid = false;
  }

  // Simple email format validation
  if (!password) {
    showFieldError("password", "Password required");
    valid = false;
  }

  if (!valid) return;

  setLoading("login-btn", true);

  // Clear previous errors
  clearFieldError("email");
  clearFieldError("password");
  try {
    const result = await loginAPI(email, password);

    if (result.success) {
      storeAuthData(result.data);
      showAlert("Login success", "success");

      setTimeout(() => {
        redirectByRole(result.data.role);
      }, 1000);
    } else {
      showAlert(result.message, "error");
    }
  } catch (err) {
    showAlert("Server error", "error");
  } finally {
    setLoading("login-btn", false);
  }
}
