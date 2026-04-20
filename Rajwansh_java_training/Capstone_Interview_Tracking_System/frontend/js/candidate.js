const form = document.getElementById("candidateForm");

// Get JD ID from URL
const params = new URLSearchParams(window.location.search);
const jdId = params.get("jdId");

form.addEventListener("submit", async function (e) {
    e.preventDefault();

    const data = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        mobile: document.getElementById("mobile").value,
        jobDescription: {
            id: jdId
        }
    };

    const response = await postRequest("/candidate", data);

    alert("Application Submitted!");
});