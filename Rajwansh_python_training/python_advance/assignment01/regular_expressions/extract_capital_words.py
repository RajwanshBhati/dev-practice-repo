"""
Question 5:
Use re.findall() to extract all words starting with a capital letter.
"""

import re

CAPITAL_WORD_PATTERN = r"\b[A-Z][a-zA-Z]*\b"


def extract_capital_words(sentence: str) -> list[str]:
    """
    Extract all words that start with a capital letter.

    re.findall() is used because we need all matching words.
    """

    return re.findall(CAPITAL_WORD_PATTERN, sentence)


def main() -> None:
    """Print words starting with capital letter."""

    sentence = "Rajwansh is learning Python in Indore."

    capital_words = extract_capital_words(sentence)

    print(capital_words)


if __name__ == "__main__":
    main()
