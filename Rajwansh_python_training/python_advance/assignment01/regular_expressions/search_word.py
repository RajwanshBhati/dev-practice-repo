"""
Question 4:
Use re.search() to check whether a word exists in a sentence.
"""

import re

SEARCH_WORD = "Python"


def is_word_present(sentence: str, word: str) -> bool:
    """
    Check whether a word exists in the sentence.

    re.search() is used because we only need to know whether
    the word exists anywhere in the sentence.
    """

    return re.search(word, sentence) is not None


def main() -> None:
    """Search word in sentence."""

    sentence = "Python is easy to learn."

    if is_word_present(sentence, SEARCH_WORD):
        print("Word found.")
    else:
        print("Word not found.")


if __name__ == "__main__":
    main()
