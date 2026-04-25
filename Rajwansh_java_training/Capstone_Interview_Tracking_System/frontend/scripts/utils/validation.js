export function showFieldError(id, msg) {
  const el = document.getElementById(`${id}-error`);
  const input = document.getElementById(id);

  if (el) el.textContent = msg;
  if (input) input.classList.add("input-error");
}

export function clearErrors() {
  document
    .querySelectorAll(".field-error")
    .forEach((e) => (e.textContent = ""));
  document
    .querySelectorAll(".input-error")
    .forEach((e) => e.classList.remove("input-error"));
}
