"""
Question 39:
Search a word in a file.
"""

# Store the file name in a constant so it can be reused
# easily and changed in one place if needed.
FILE_NAME = "student.txt"


def search_word(search_text: str) -> bool:
    """
    search_text: str
    Used to accept the word that needs to be searched.

    -> bool
    Used to return True if the word exists in the file,
    otherwise False.
    """

    # Read the file content before searching.
    with open(FILE_NAME, "r", encoding="utf-8") as file:
        file_content = file.read()

    # Check whether the required word exists in the file content.
    return search_text in file_content


def main() -> None:
    """Take input and search for the word."""

    user_word = input("Enter a word to search: ")

    if search_word(user_word):
        print("Word found in file.")
    else:
        print("Word not found in file.")


if __name__ == "__main__":
    main()
