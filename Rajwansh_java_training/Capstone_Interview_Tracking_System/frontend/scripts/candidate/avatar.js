document.addEventListener("DOMContentLoaded", () => {
  const user = Session.get();
  if (user && user.name) {
    const avatarEl = document.getElementById("avatarInitial");
    if (avatarEl) avatarEl.textContent = user.name.charAt(0).toUpperCase();
  }
});
