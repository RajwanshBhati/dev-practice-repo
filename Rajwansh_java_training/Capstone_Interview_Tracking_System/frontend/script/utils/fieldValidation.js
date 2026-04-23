export function showFieldError(fieldId, message) {
  const input = document.getElementById(fieldId);
  const error = document.getElementById(`${fieldId}-error`);

  if (input) input.classList.add("input-error");
  if (error) error.textContent = message;
}

// this function will be used to clear the error message and styling from the input field
export function clearFieldError(fieldId) {
  const input = document.getElementById(fieldId);
  const error = document.getElementById(`${fieldId}-error`);

  if (input) {
    input.classList.remove("input-error", "input-success");
  }
  if (error) error.textContent = "";
}

// this function will be used to mark the input field as success after successful validation
export function markFieldSuccess(fieldId) {
  const input = document.getElementById(fieldId);
  if (input) {
    input.classList.add("input-success");
    input.classList.remove("input-error");
  }
}
