import { loadComponent } from "../utils/component-loader.js";

document.addEventListener("DOMContentLoaded", async () => {
  await loadComponent("sidebar-container", "../components/sidebar.html");
  await loadComponent("header-container", "../components/header.html");
  await loadComponent("stats-container", "../components/stats.html");
  await loadComponent("filter-container", "../components/filters.html");
  await loadComponent("table-container", "../components/jd-table.html");
  await loadComponent("modals-container", "../components/modals/jd-modal.html");

  //here I can also initialize any event listeners related to the JD page after the components are loaded
  initJDTable();
});
