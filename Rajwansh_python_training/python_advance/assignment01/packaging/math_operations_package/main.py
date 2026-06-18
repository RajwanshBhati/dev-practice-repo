"""
Question 4:
Use mathematical operations package.
"""

from math_operations import (
    add_numbers,
    subtract_numbers,
    multiply_numbers,
    divide_numbers,
)


def main() -> None:
    """
    Use math operation functions from math_operations package.

    Functions are imported from __init__.py for cleaner imports.
    """

    first_number = 20
    second_number = 10

    print(f"Addition: {add_numbers(first_number, second_number)}")
    print(f"Subtraction: {subtract_numbers(first_number, second_number)}")
    print(f"Multiplication: {multiply_numbers(first_number, second_number)}")
    print(f"Division: {divide_numbers(first_number, second_number)}")


if __name__ == "__main__":
    main()
