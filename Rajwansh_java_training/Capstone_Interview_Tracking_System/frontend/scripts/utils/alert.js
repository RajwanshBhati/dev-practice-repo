// alert.js here I have created a simple alert system that can be used across the application to show success or error messages. The alert box will automatically hide after 4 seconds if it's a success message, while error messages will require manual dismissal.

export function showAlert(message, type = "error") {
  const box = document.getElementById("alert-box");
  const msg = document.getElementById("alert-message");
  const icon = document.getElementById("alert-icon");

  if (!box) return;

  box.className = `alert alert-${type}`;
  msg.textContent = message;
  icon.textContent = type === "success" ? "✓" : "!";

  box.style.display = "flex";

  if (type === "success") {
    setTimeout(() => (box.style.display = "none"), 4000);
  }
}

export function hideAlert() {
  const box = document.getElementById("alert-box");
  if (box) box.style.display = "none";
}
