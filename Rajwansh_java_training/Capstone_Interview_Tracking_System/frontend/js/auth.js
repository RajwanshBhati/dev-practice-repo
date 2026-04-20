async function login() {
    const data = {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    };

    const response = await postRequest("/auth/login", data);

    // Save token
    localStorage.setItem("token", response.token);

    alert("Login Successful!");
}