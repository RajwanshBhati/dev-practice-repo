"""
Question 7:
Write a program that processes a large dataset using a generator
instead of storing all values in a list.
"""

TOTAL_RECORDS = 1000000


def generate_large_dataset(limit: int):
    """
    Generate large dataset values one by one.

    Generator is used here to avoid storing all values in memory.
    """

    for number in range(1, limit + 1):
        yield number


def calculate_total_sum() -> None:
    """
    Calculate sum of large dataset.

    Values are processed one by one, so memory usage remains low.
    """

    total_sum = 0

    for number in generate_large_dataset(TOTAL_RECORDS):
        total_sum += number

    print(f"Total sum: {total_sum}")


if __name__ == "__main__":
    calculate_total_sum()
