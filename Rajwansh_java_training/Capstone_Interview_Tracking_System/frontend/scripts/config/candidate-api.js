const API_BASE = "http://localhost:8080/api";

export const API = {
  CANDIDATE: {
    REGISTER: `${API_BASE}/candidates/register`,
    LOGIN: `${API_BASE}/candidates/login`,
    APPLY: `${API_BASE}/candidates/apply`,
    STATUS: `${API_BASE}/candidates/my-status`,
    LOGOUT: `${API_BASE}/candidates/logout`,
  },
  HR: {
    JD_LIST: `${API_BASE}/hr/jd`,
  },
};
