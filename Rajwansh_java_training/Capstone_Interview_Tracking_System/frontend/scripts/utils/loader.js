// loader.js here I have created a simple loader function that can be used to show a loading spinner on buttons while an API request is being made. The function takes the button ID and a boolean value to indicate whether to show or hide the loader
export function setLoading(btnId, loading) {
  const btn = document.getElementById(btnId);
  const text = document.getElementById("btn-text");
  const loader = document.getElementById("btn-loader");

  if (!btn) return;

  btn.disabled = loading;

  if (text) text.style.display = loading ? "none" : "inline";
  if (loader) loader.style.display = loading ? "flex" : "none";
}
