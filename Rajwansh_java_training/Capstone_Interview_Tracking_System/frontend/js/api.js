const BASE_URL = "http://localhost:8080";

// Fetch data from the backend using a GET request
async function getRequest(endpoint) {
    const response = await fetch(BASE_URL + endpoint);
    return response.json();
}

// Send data to the backend using a POST request
async function postRequest(endpoint, data) {
    const response = await fetch(BASE_URL + endpoint, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });

    return response.json();
}