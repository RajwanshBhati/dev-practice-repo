"""
Question 6:
Replace multiple spaces in a string with a single space using re.sub().
"""

import re

MULTIPLE_SPACE_PATTERN = r"\s+"


def replace_multiple_spaces(text: str) -> str:
    """
    Replace multiple spaces with a single space.

    re.sub() is used because we need to replace matching spaces
    with one normal space.
    """

    return re.sub(MULTIPLE_SPACE_PATTERN, " ", text).strip()


def main() -> None:
    """Print cleaned text."""

    text = "Python     is     very     useful."

    cleaned_text = replace_multiple_spaces(text)

    print(cleaned_text)


if __name__ == "__main__":
    main()
