import { API } from "../config/candidate-api.js";

export async function fetchJDs() {
  const res = await fetch(API.HR.JD_LIST);
  return res.json();
}
