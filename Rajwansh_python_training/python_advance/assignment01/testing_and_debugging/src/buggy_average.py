"""
Question 3:
Create a function with a logical bug and use pdb to identify the issue.
"""

import pdb

NUMBERS = [10, 20, 30]


def calculate_average(numbers: list[int]) -> float:
    """
    Calculate average of numbers.

    pdb.set_trace() is used to stop program execution and inspect variables.
    """

    total_sum = sum(numbers)

    pdb.set_trace()

    # Logical bug:
    # Average should be total_sum / len(numbers), but here it is divided by 2.
    average = total_sum / 2

    return average


def main() -> None:
    """Call average function and print result."""

    result = calculate_average(NUMBERS)

    print(f"Average: {result}")


if __name__ == "__main__":
    main()
