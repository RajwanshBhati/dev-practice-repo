#!/usr/bin/env python
import sys
import os
from pathlib import Path

# Add backend folder to Python path
backend_path = Path(__file__).parent.parent.parent
backend_path_str = str(backend_path.absolute())

if backend_path_str not in sys.path:
    sys.path.insert(0, backend_path_str)

# Also add current directory
current_dir = Path(__file__).parent.absolute()
if str(current_dir) not in sys.path:
    sys.path.insert(0, str(current_dir))

# Set environment variable for subprocesses
os.environ['PYTHONPATH'] = backend_path_str

if __name__ == "__main__":
    import uvicorn

    print("=" * 60)
    print("Starting User Service")
    print("=" * 60)
    print(f"Backend Path: {backend_path_str}")
    print(f"Service Path: {current_dir}")
    print("=" * 60)

    uvicorn.run(
        "app.main:app",
        host="0.0.0.0",
        port=8001,
        reload=True,
        log_level="info"
    )
