"""
Question 3:
Write a program using try-except-else-finally to read a number
from a file and print its square.
"""

FILE_NAME = "number.txt"


def print_square_from_file() -> None:
    """
    Read a number from a file and print its square.

    try is used for risky file reading.
    except handles file and value errors.
    else runs only when no error occurs.
    finally always runs to show completion.
    """

    try:
        with open(FILE_NAME, "r", encoding="utf-8") as file:
            number = int(file.read().strip())

    except FileNotFoundError:
        # This handles the case when the file is missing.
        print("File not found.")

    except ValueError:
        # This handles the case when file content is not a valid integer.
        print("File does not contain a valid integer.")

    else:
        square = number ** 2
        print(f"Square: {square}")

    finally:
        # finally always executes whether error occurs or not.
        print("File operation completed.")


if __name__ == "__main__":
    print_square_from_file()
