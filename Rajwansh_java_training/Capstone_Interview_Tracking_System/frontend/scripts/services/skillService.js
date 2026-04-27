let skills = [];

export function getSkills() {
  return skills;
}


//I am using a simple array to manage skills for the JD form. In a real application, this would likely be more complex and involve API calls to fetch available skills and manage them on the backend.
export function addSkill(val) {
  const v = val.trim().replace(/,/g, "");
  if (v && !skills.includes(v)) skills.push(v);
}

export function removeSkill(index) {
  skills.splice(index, 1);
}

export function resetSkills() {
  skills = [];
}
