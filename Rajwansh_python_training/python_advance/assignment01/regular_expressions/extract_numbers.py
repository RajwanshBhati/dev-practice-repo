"""
Question 1:
Write a program to extract all numbers from a given string
using regular expressions.
"""

import re

NUMBER_PATTERN = r"\d+"


def extract_numbers(text: str) -> list[str]:
    """
    Extract all numbers from the given text.

    re.findall() is used because we need all matching numbers
    from the string.
    """

    return re.findall(NUMBER_PATTERN, text)


def main() -> None:
    """Print all extracted numbers."""

    input_text = "Raj has 2 laptops, 15 books, and 500 rupees."

    numbers = extract_numbers(input_text)

    print(numbers)


if __name__ == "__main__":
    main()
