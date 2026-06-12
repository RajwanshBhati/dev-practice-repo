"""
Question 17:
Write a function to calculate square of a number.
"""


def calculate_square(number: int) -> int:
    """
    Calculate and return the square of a number.
    """

    # Multiply the number by itself to get the square.
    return number * number


def main() -> None:
    """Take input and display the square value."""

    user_number = int(input("Enter a number: "))

    square_result = calculate_square(user_number)

    print(f"Square of {user_number} is {square_result}")


if __name__ == "__main__":
    main()
