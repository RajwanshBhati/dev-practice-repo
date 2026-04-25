async function loadJobs() {
    const jobs = await getRequest("/jd");

    const container = document.getElementById("job-list");
    container.innerHTML = "";

    jobs.forEach(job => {
        const div = document.createElement("div");
        div.className = "job-card";

        div.innerHTML = `
            <h3>${job.title}</h3>
            <p>${job.description}</p>
            <p><b>Location:</b> ${job.location}</p>
            <button onclick="applyJob(${job.id})">Apply</button>
        `;

        container.appendChild(div);
    });
}

function applyJob(jobId) {
    window.location.href = `pages/candidate-form.html?jdId=${jobId}`;
}

loadJobs();