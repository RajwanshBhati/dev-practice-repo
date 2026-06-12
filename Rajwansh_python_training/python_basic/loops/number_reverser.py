"""
Question 15:
Reverse a number using loop.
"""


def reverse_number(number: int) -> int:
    """
    Reverse the given number using a loop.

    Returns:
        int: Reversed number.
    """

    reversed_number = 0

    # Use a temporary variable so the original number is not modified.
    temporary_number = number

    # Keep processing digits until no digits are left.
    while temporary_number > 0:

        # Get the last digit of the number.
        last_digit = temporary_number % 10

        # Add the extracted digit to the reversed number.
        reversed_number = (reversed_number * 10) + last_digit

        # Remove the digit that has already been processed.
        temporary_number //= 10

    # Return the final reversed number.
    return reversed_number


def main() -> None:
    """
    Take input from the user and display the reversed number.

    Returns:
        None: This function only handles input and output.
    """

    user_number = int(input("Enter a number: "))

    reversed_number = reverse_number(user_number)

    print(f"Reversed number is {reversed_number}")


if __name__ == "__main__":
    # Run the program only when this file is executed directly.
    main()
