"""
Question 4:
Create a package for mathematical operations and use it.
"""

ZERO_DIVISOR = 0


def divide_numbers(first_number: float, second_number: float) -> float:
    """
    Divide first number by second number.

    ValueError is raised because division by zero is not allowed.
    """

    if second_number == ZERO_DIVISOR:
        raise ValueError("Division by zero is not allowed.")

    return first_number / second_number
