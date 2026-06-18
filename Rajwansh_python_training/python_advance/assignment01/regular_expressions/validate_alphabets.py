"""
Question 7:
Write a pattern to check if a string contains only alphabets.
"""

import re

ALPHABET_PATTERN = r"^[A-Za-z]+$"


def contains_only_alphabets(text: str) -> bool:
    """
    Validate that text contains only alphabets.

    ^ and $ are used to ensure the complete string contains
    alphabets only.
    """

    return re.fullmatch(ALPHABET_PATTERN, text) is not None


def main() -> None:
    """Take input and validate alphabets."""

    text = input("Enter text: ")

    if contains_only_alphabets(text):
        print("Text contains only alphabets.")
    else:
        print("Text does not contain only alphabets.")


if __name__ == "__main__":
    main()
