"""
Question 37:
Append data to existing file.
"""

FILE_NAME = "student.txt"
NEW_CONTENT = "\nLearning Python File Handling"


def append_data_to_file() -> None:
    """
    Append new content to the file.
    """

    # Add new content without removing the existing data.
    with open(FILE_NAME, "a", encoding="utf-8") as file:
        file.write(NEW_CONTENT)


def main() -> None:
    """Program entry point."""

    append_data_to_file()

    print("Data appended successfully.")


if __name__ == "__main__":
    main()
