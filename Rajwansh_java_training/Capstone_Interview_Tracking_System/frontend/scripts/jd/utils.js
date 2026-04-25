export function formatSalary(val) {
  if (!val) return "—";
  const num = parseFloat(val);
  if (num >= 100000) return `₹${(num / 100000).toFixed(1)}L`;
  return `₹${num.toLocaleString("en-IN")}`;
}

export function formatDate(dateStr) {
  if (!dateStr) return "";
  return new Date(dateStr).toLocaleDateString("en-IN", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  });
}

export function jobTypeLabel(type) {
  const map = {
    FULL_TIME: "Full Time",
    CONTRACT: "Contract",
    REMOTE: "Remote",
  };
  return map[type] || type;
}

export function jobTypeBadgeClass(type) {
  const map = {
    FULL_TIME: "badge-full-time",
    CONTRACT: "badge-contract",
    REMOTE: "badge-remote",
  };
  return map[type] || "";
}

export function statusBadgeClass(status) {
  const map = {
    ACTIVE: "badge-active",
    INACTIVE: "badge-inactive",
    CLOSED: "badge-closed",
  };
  return map[status] || "";
}

export function showAlert(message, type = "error", boxId = "alert-box") {
  const box = document.getElementById(boxId);
  const msg = document.getElementById(
    boxId === "alert-box" ? "alert-message" : "modal-alert-message",
  );
  const icon = box ? box.querySelector(".alert-icon") : null;
  if (!box) return;
  box.className = `alert alert-${type}`;
  if (msg) msg.textContent = message;
  if (icon) icon.textContent = type === "success" ? "✓" : "!";
  box.style.display = "flex";
  if (type === "success") {
    setTimeout(() => {
      box.style.display = "none";
    }, 3000);
  }
}

export function hideAlert(boxId = "alert-box") {
  const box = document.getElementById(boxId);
  if (box) box.style.display = "none";
}

export function showFieldError(id, msg) {
  const el = document.getElementById(`${id}-error`);
  const input = document.getElementById(id);
  if (el) el.textContent = msg;
  if (input) input.classList.add("input-error");
}

export function clearAllErrors() {
  document
    .querySelectorAll(".field-error")
    .forEach((el) => (el.textContent = ""));
  document
    .querySelectorAll(".input-error")
    .forEach((el) => el.classList.remove("input-error"));
  hideAlert("modal-alert");
}
