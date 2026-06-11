import platform


def get_python_version() -> str:
    """Return the Python version."""
    return platform.python_version()


if __name__ == "__main__":
    # Fetch version dynamically from the system
    print(f"Python Version: {get_python_version()}")
