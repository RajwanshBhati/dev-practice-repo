"""
Question 4:
Use pdb breakpoints inside a loop and inspect variable values.
"""

import pdb

NUMBERS = [2, 4, 6, 8]


def print_double_values(numbers: list[int]) -> None:
    """
    Print double value of each number.

    pdb breakpoint is placed inside the loop to inspect values
    during each iteration.
    """

    for number in numbers:
        pdb.set_trace()

        double_value = number * 2

        print(f"Double value of {number}: {double_value}")


if __name__ == "__main__":
    print_double_values(NUMBERS)
