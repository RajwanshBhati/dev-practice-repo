"""
Question 1:
Import utility functions from another module and use them.
"""

from string_utils import convert_to_uppercase, count_characters


def main() -> None:
    """
    Use utility functions from string_utils module.

    Import is used to reuse logic without writing the same code again.
    """

    text = "python packaging"

    uppercase_text = convert_to_uppercase(text)
    character_count = count_characters(text)

    print(f"Uppercase text: {uppercase_text}")
    print(f"Character count: {character_count}")


if __name__ == "__main__":
    main()
