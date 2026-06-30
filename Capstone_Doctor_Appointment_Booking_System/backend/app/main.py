from fastapi import FastAPI

app = FastAPI(title="Doctor Appointment System", version="1.0.0")

@app.get("/")
async def root():
    return {"message": "Doctor Appointment System API"}

@app.get("/health")
async def health():
    return {"status": "healthy"}
