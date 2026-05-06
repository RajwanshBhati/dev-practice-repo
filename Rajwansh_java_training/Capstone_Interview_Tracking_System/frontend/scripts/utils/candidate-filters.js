function normalize(value) {
  return String(value || "")
    .trim()
    .toLowerCase()
    .replace(/[-\s]/g, "_");
}

/**
 * Filters JD list based on search and job type
 */
export function filterJDs(jds, searchText, type) {
  const search = String(searchText || "")
    .trim()
    .toLowerCase();
  const selectedType = normalize(type);

  return (jds || []).filter((jd) => {
    const title = String(jd.jobTitle || "").toLowerCase();
    const location = String(jd.location || "").toLowerCase();
    const jobType = normalize(jd.jobType);

    const matchSearch =
      !search || title.includes(search) || location.includes(search);

    const matchType = !selectedType || jobType === selectedType;

    return matchSearch && matchType;
  });
}
