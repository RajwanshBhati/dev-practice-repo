"""
Question 20:
Write a function using default parameters.
"""

DEFAULT_GREETING = "Hello"


def greet_user(user_name: str, greeting_message: str = DEFAULT_GREETING) -> None:
    """
    Display a greeting message.

    If no greeting message is provided,
    the default greeting will be used.
    """

    print(f"{greeting_message}, {user_name}!")


def main() -> None:
    """Demonstrate default parameter usage."""

    greet_user("Rajwansh")

    greet_user("Ajay", "Welcome")


if __name__ == "__main__":
    main()
