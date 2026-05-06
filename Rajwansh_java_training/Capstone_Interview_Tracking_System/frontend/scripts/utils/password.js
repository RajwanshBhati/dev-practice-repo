// password.js here I have created two functions, one for toggling the visibility of the password input field and another for evaluating the strength of the password and updating the UI accordingly.

export function setupPasswordToggle(toggleBtnId, inputId) {
  const btn = document.getElementById(toggleBtnId);
  const input = document.getElementById(inputId);

  if (!btn || !input) return;

  btn.addEventListener("click", () => {
    const isPassword = input.type === "password";
    input.type = isPassword ? "text" : "password";
  });
}

export function encodePasswordBase64(password) {
  return btoa(String.fromCharCode(...new TextEncoder().encode(password)));
}

export function evaluatePasswordStrength(password) {
  const fill = document.getElementById("strength-fill");
  const label = document.getElementById("strength-label");

  if (!fill || !label) return;

  if (!password) {
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

  const levels = ["Weak", "Weak", "Fair", "Good", "Strong"];

  const pct = [20, 40, 60, 80, 100][Math.min(score, 4)];

  fill.style.width = pct + "%";
  label.textContent = levels[Math.min(score, 4)];
}
