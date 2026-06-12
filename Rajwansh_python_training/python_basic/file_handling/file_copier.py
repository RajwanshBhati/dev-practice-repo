"""
Question 38:
Copy content from one file to another.
"""

SOURCE_FILE = "student.txt"
DESTINATION_FILE = "student_copy.txt"


def copy_file_content() -> None:
    """
    Copy content from one file to another.
    """

    # Read the content from the source file.
    with open(SOURCE_FILE, "r", encoding="utf-8") as source_file:
        file_content = source_file.read()

    # Copy the content into the destination file.
    with open(DESTINATION_FILE, "w", encoding="utf-8") as destination_file:
        destination_file.write(file_content)


def main() -> None:
    """Program entry point."""

    copy_file_content()

    print("File copied successfully.")


if __name__ == "__main__":
    main()
