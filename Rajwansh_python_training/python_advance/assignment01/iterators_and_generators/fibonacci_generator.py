"""
Question 4:
Write a generator to produce Fibonacci numbers.
"""

FIRST_FIBONACCI_NUMBER = 0
SECOND_FIBONACCI_NUMBER = 1


def generate_fibonacci_numbers(limit: int):
    """
    Generate Fibonacci numbers up to the given count.

    yield is used to produce Fibonacci numbers one by one.
    """

    first_number = FIRST_FIBONACCI_NUMBER
    second_number = SECOND_FIBONACCI_NUMBER

    for _ in range(limit):
        yield first_number

        first_number, second_number = (
            second_number,
            first_number + second_number,
        )


def main() -> None:
    """Print Fibonacci numbers."""

    for number in generate_fibonacci_numbers(10):
        print(number)


if __name__ == "__main__":
    main()
