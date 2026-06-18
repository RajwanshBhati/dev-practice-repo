"""
Question 2:
Write a regular expression to validate an email address.
"""

import re

EMAIL_PATTERN = r"^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$"


def is_valid_email(email: str) -> bool:
    """
    Validate email address using regex.

    re.fullmatch() is used because the complete email should match
    the given pattern.
    """

    return re.fullmatch(EMAIL_PATTERN, email) is not None


def main() -> None:
    """Take email input and validate it."""

    email = input("Enter email address: ")

    if is_valid_email(email):
        print("Valid email address.")
    else:
        print("Invalid email address.")


if __name__ == "__main__":
    main()
