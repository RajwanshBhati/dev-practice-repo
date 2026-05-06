const STEP_IDS = ["st-PROFILING", "st-L1", "st-L2", "st-HR"];

const STAGE_MAP = {
  NOT_APPLIED: -1,
  PROFILING: 0,
  SCREENING: 0,
  L1_TECHNICAL: 1,
  L2_TECHNICAL: 2,
  HR_ROUND: 3,
  REJECTED: 3,
  SELECTED: 3,
};

/**
 * Renders candidate progress steps.
 */
export function renderCandidateProgress(status) {
  clearProgressSteps();

  const currentStep = STAGE_MAP[status] ?? 0;

  if (status === "NOT_APPLIED") {
    return;
  }

  if (status === "SELECTED") {
    STEP_IDS.forEach((id) => {
      const step = document.getElementById(id);

      if (step) {
        step.classList.add("done");
      }
    });

    return;
  }

  if (status === "REJECTED") {
    STEP_IDS.forEach((id, index) => {
      const step = document.getElementById(id);

      if (!step) {
        return;
      }

      if (index < currentStep) {
        step.classList.add("done");
      }

      if (index === currentStep) {
        step.classList.add("rejected");
      }
    });

    return;
  }

  STEP_IDS.forEach((id, index) => {
    const step = document.getElementById(id);

    if (!step) {
      return;
    }

    if (index < currentStep) {
      step.classList.add("done");
    }

    if (index === currentStep) {
      step.classList.add("active");
    }
  });
}

/**
 * Shows selected or rejected status clearly.
 */
export function renderFinalStatus(status) {
  const container = document.getElementById("final-status");

  if (!container) {
    return;
  }

  if (status === "SELECTED") {
    container.innerHTML = `
      <div class="final-status selected">
        <h2>Congratulations! You are selected.</h2>
        <p>HR has marked your application as selected.</p>
      </div>
    `;
    return;
  }

  if (status === "REJECTED") {
    container.innerHTML = `
      <div class="final-status rejected">
        <h2>Your application was rejected.</h2>
        <p>HR has marked your application as rejected.</p>
      </div>
    `;
    return;
  }

  container.innerHTML = "";
}

/**
 * Shows candidate current status text.
 */
export function renderCurrentStatus(status) {
  const statusBox = document.getElementById("candidate-current-status");

  if (!statusBox) {
    return;
  }

  statusBox.textContent = status || "NOT_APPLIED";
}

/**
 * Shows scheduled interview details.
 */
export function renderInterviewDetails(interviews) {
  const container = document.getElementById("candidate-interview-info");

  if (!container) {
    return;
  }

  if (!interviews || interviews.length === 0) {
    container.innerHTML = "<p>No interview scheduled yet.</p>";
    return;
  }

  container.innerHTML = "";

  interviews.forEach((interview) => {
    container.innerHTML += `
      <div class="interview-card">
        <h3>${interview.stage || "Interview"}</h3>
        <p><strong>Date:</strong> ${interview.interviewDate || interview.date || "-"}</p>
        <p><strong>Time:</strong> ${interview.interviewTime || interview.time || "-"}</p>
        <p><strong>Focus Area:</strong> ${interview.focusArea || "-"}</p>
      </div>
    `;
  });
}

/**
 * Shows or hides loader.
 */
export function setCandidateLoader(show) {
  const loader = document.getElementById("candidate-loader");

  if (loader) {
    loader.style.display = show ? "block" : "none";
  }
}

/**
 * Shows error message.
 */
export function showCandidateError(message) {
  const errorBox = document.getElementById("candidate-error");

  if (errorBox) {
    errorBox.textContent = message;
    errorBox.style.display = "block";
  }
}

/**
 * Clears all stage classes before fresh render.
 */
function clearProgressSteps() {
  STEP_IDS.forEach((id) => {
    const step = document.getElementById(id);

    if (step) {
      step.classList.remove("active", "done", "rejected");
    }
  });
}
