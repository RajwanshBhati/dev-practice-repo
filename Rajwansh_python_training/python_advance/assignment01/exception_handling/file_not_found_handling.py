"""
Question 8:
Write a program that handles FileNotFoundError
when trying to open a file.
"""

FILE_NAME = "student.txt"


def read_student_file() -> None:
    """
    Read content from a student file.

    FileNotFoundError is handled because the file may not exist
    at the given location.
    """

    try:
        with open(FILE_NAME, "r", encoding="utf-8") as file:
            file_content = file.read()

        print(file_content)

    except FileNotFoundError:
        # This block runs when the given file is not available.
        print(f"{FILE_NAME} does not exist.")


if __name__ == "__main__":
    read_student_file()
