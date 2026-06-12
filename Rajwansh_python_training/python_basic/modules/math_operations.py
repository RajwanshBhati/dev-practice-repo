"""
Question 22:
Use math module to find square root, power, and factorial.
"""

import math


def display_math_operations(number: int) -> None:
    """
    Display square root, power, and factorial of a number.
    """

    # Use math.sqrt() to calculate the square root.
    square_root_result = math.sqrt(number)

    # Use math.pow() to calculate the power value.
    power_result = math.pow(number, 2)

    # Use math.factorial() to calculate factorial.
    factorial_result = math.factorial(number)

    print(f"Square Root: {square_root_result}")
    print(f"Power: {power_result}")
    print(f"Factorial: {factorial_result}")


def main() -> None:
    """Take input and display math operations."""

    user_number = int(input("Enter a number: "))

    display_math_operations(user_number)


if __name__ == "__main__":
    main()
