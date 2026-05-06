export function showFieldError(inputId, message) {
  const input = document.getElementById(inputId);
  if (!input) return;

  // remove old error
  const existing = input.parentNode.querySelector(".error-text");
  if (existing) existing.remove();

  // create error
  const error = document.createElement("small");
  error.className = "error-text";
  error.style.color = "#dc2626";
  error.style.fontWeight = "600";
  error.textContent = message;

  input.parentNode.appendChild(error);

  input.focus();
}

export function clearErrors() {
  document.querySelectorAll(".error-text").forEach((e) => e.remove());
}
