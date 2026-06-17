"""
Question 5:
Write a program that catches all exceptions and prints the error message.
"""


def display_list_value() -> None:
    """
    Display a list value based on user index.

    Exception is used here to catch any unexpected error and print
    the actual error message.
    """

    numbers = [10, 20, 30]

    try:
        index_position = int(input("Enter index position: "))
        print(numbers[index_position])

    except Exception as error:
        # This catches any exception and stores the error message.
        print(f"Error: {error}")


if __name__ == "__main__":
    display_list_value()
