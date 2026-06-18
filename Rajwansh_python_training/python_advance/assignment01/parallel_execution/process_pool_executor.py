"""
Question 8:
Convert a normal function into parallel execution using ProcessPoolExecutor.
"""

from concurrent.futures import ProcessPoolExecutor

MAX_WORKERS = 3
NUMBERS = [1, 2, 3, 4, 5]


def calculate_square(number: int) -> int:
    """
    Calculate square of a number.

    ProcessPoolExecutor is useful for CPU-bound calculation tasks.
    """

    return number ** 2


def main() -> None:
    """
    Execute calculate_square function in parallel using ProcessPoolExecutor.

    Separate processes are used to perform calculations in parallel.
    """

    with ProcessPoolExecutor(max_workers=MAX_WORKERS) as executor:
        square_numbers = executor.map(calculate_square, NUMBERS)

    for square in square_numbers:
        print(square)


if __name__ == "__main__":
    main()
