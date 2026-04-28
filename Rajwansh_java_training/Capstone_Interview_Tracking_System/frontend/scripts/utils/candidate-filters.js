/**
 * Filters JD list based on search and job type
 */
export function filterJDs(jds, searchText, type) {
  const search = (searchText || "").toLowerCase();

  return jds.filter((jd) => {
    const matchSearch =
      !search ||
      jd.jobTitle?.toLowerCase().includes(search) ||
      jd.location?.toLowerCase().includes(search);

    const matchType =
      !type || (jd.jobType && jd.jobType.toLowerCase() === type.toLowerCase());

    return matchSearch && matchType;
  });
}
