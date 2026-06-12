"""
Question 34:
Merge two dictionaries.
"""


def merge_dictionaries() -> None:
    """
    Merge two dictionaries and display the result.
    """

    first_dictionary = {
        "name": "Rajwansh"
    }

    second_dictionary = {
        "course": "Python"
    }

    # Merge both dictionaries into a single dictionary.
    merged_dictionary = first_dictionary | second_dictionary

    print(merged_dictionary)


def main() -> None:
    """Program entry point."""

    merge_dictionaries()


if __name__ == "__main__":
    main()
