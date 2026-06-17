"""
Question 1:
Write a program that takes a number as input and handles ValueError
if the input is not a valid integer.
"""


def read_integer() -> None:
    """
    Read an integer from the user.

    ValueError is handled because int() will fail if the user enters
    text or any non-integer value.
    """

    try:
        user_number = int(input("Enter an integer: "))
        print(f"Entered number: {user_number}")

    except ValueError:
        # This block runs when the input cannot be converted into integer.
        print("Please enter a valid integer.")


if __name__ == "__main__":
    read_integer()
