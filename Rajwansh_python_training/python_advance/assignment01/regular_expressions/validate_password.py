"""
Question 8:
Create a password validation program using regex.

Password rules:
- Minimum length should be 8 characters.
- Password should contain at least one digit.
- Password should contain at least one special character.
"""

import re

PASSWORD_PATTERN = r"^(?=.*\d)(?=.*[@#$%^&+=!]).{8,}$"


def is_valid_password(password: str) -> bool:
    """
    Validate password using regex.

    (?=.*\\d) checks at least one digit.
    (?=.*[@#$%^&+=!]) checks at least one special character.
    .{8,} checks minimum 8 characters.
    """

    return re.fullmatch(PASSWORD_PATTERN, password) is not None


def main() -> None:
    """Take password input and validate it."""

    password = input("Enter password: ")

    if is_valid_password(password):
        print("Valid password.")
    else:
        print("Invalid password.")


if __name__ == "__main__":
    main()
