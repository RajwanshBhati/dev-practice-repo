# Store the message in a constant so it can be reused easily
WELCOME_MESSAGE = "Welcome to Python Training"


def print_welcome_message() -> None:
    """Print the welcome message."""
    print(WELCOME_MESSAGE)


if __name__ == "__main__":
    print_welcome_message()
