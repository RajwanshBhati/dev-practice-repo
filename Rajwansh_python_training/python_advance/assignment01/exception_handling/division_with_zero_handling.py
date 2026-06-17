"""
Question 2:
Write a program to divide two numbers entered by the user
and handle ZeroDivisionError.
"""


def divide_numbers() -> None:
    """
    Divide two numbers entered by the user.

    ZeroDivisionError is handled because division by zero is not allowed.
    """

    try:
        first_number = float(input("Enter first number: "))
        second_number = float(input("Enter second number: "))

        result = first_number / second_number
        print(f"Result: {result}")

    except ZeroDivisionError:
        # This block runs when the second number is zero.
        print("Division by zero is not allowed.")


if __name__ == "__main__":
    divide_numbers()
