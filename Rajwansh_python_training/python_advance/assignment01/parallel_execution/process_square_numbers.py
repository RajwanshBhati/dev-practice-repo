"""
Question 6:
Write a multiprocessing program to calculate the square of numbers
using Process class.
"""

import multiprocessing

NUMBERS = [1, 2, 3, 4, 5]


def print_square(number: int) -> None:
    """
    Calculate and print square of a number.

    This function runs inside a separate process.
    """

    square = number ** 2

    print(f"Square of {number}: {square}")


def main() -> None:
    """Create one process for each number."""

    processes = []

    for number in NUMBERS:
        process = multiprocessing.Process(
            target=print_square,
            args=(number,),
        )

        processes.append(process)
        process.start()

    for process in processes:
        process.join()

    print("Square calculation completed.")


if __name__ == "__main__":
    main()
