export function storeAuthData(data) {
  localStorage.setItem("accessToken", data.accessToken);
  localStorage.setItem("refreshToken", data.refreshToken);
  localStorage.setItem("role", data.role);
  localStorage.setItem("name", data.name);
  localStorage.setItem("email", data.email);
}

//these dunction will be used to get the auth data from local storage
export function getAuthRole() {
  return localStorage.getItem("role");
}

//this function will be used to clear the auth data from local storage on logout
export function isLoggedIn() {
  return !!localStorage.getItem("accessToken");
}
