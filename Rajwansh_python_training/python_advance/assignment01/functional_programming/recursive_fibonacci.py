"""
Question 6:
Write a recursive function to calculate Fibonacci.
"""

BASE_FIBONACCI_INDEX = 1


def fibonacci(number: int) -> int:
    """
    Return Fibonacci number using recursion.

    Recursion is used because each Fibonacci value depends
    on the previous two Fibonacci values.
    """

    if number <= BASE_FIBONACCI_INDEX:
        return number

    return fibonacci(number - 1) + fibonacci(number - 2)


def main() -> None:
    """
    Print Fibonacci numbers.
    """

    for index in range(10):
        print(fibonacci(index))


if __name__ == "__main__":
    main()
