"""
Question 3:
Write a regular expression to validate a 10-digit mobile number.
"""

import re

MOBILE_NUMBER_PATTERN = r"^[6-9]\d{9}$"


def is_valid_mobile_number(mobile_number: str) -> bool:
    """
    Validate Indian 10-digit mobile number.

    Pattern starts with 6-9 and then allows exactly 9 more digits.
    """

    return re.fullmatch(MOBILE_NUMBER_PATTERN, mobile_number) is not None


def main() -> None:
    """Take mobile number input and validate it."""

    mobile_number = input("Enter mobile number: ")

    if is_valid_mobile_number(mobile_number):
        print("Valid mobile number.")
    else:
        print("Invalid mobile number.")


if __name__ == "__main__":
    main()
