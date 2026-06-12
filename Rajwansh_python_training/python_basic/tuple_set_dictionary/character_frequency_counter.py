"""
Question 33:
Count frequency of characters in a string using dictionary.
"""


def count_character_frequency(text: str) -> dict[str, int]:
    """
    Count frequency of each character.
    """

    character_frequency: dict[str, int] = {}

    # Count how many times each character appears in the string.
    for character in text:
        if character in character_frequency:
            character_frequency[character] += 1
        else:
            character_frequency[character] = 1

    return character_frequency


def main() -> None:
    """Take input and display character frequencies."""

    user_text = input("Enter a string: ")

    print(count_character_frequency(user_text))


if __name__ == "__main__":
    main()
