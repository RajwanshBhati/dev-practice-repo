"""
Question 1:
Create a module with two utility functions and import it into another Python file.
"""


def convert_to_uppercase(text: str) -> str:
    """
    Convert text into uppercase.

    This function is kept in a separate module because it can be reused
    in multiple Python files.
    """

    return text.upper()


def count_characters(text: str) -> int:
    """
    Count total characters in the given text.

    This utility function is separated to keep the main file clean.
    """

    return len(text)
