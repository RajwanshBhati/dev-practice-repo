"""
Question 35:
Create a file and write your name into it.
"""

FILE_NAME = "student.txt"
STUDENT_NAME = "Rajwansh Bhati"


def write_name_to_file() -> None:
    """
    Create a file and write the student name.
    """

    # Open the file in write mode to create it if it does not already exist.
    with open(FILE_NAME, "w", encoding="utf-8") as file:
        file.write(STUDENT_NAME)


def main() -> None:
    """Program entry point."""

    write_name_to_file()

    print("Name written successfully.")


if __name__ == "__main__":
    main()
