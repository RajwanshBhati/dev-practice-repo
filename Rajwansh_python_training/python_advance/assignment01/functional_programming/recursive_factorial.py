"""
Question 5:
Write a recursive function to calculate factorial.
"""

MINIMUM_FACTORIAL_VALUE = 1


def calculate_factorial(number: int) -> int:
    """
    Calculate factorial using recursion.

    Recursion is used because factorial naturally follows:
    n! = n × (n - 1)!
    """

    if number <= MINIMUM_FACTORIAL_VALUE:
        return 1

    return number * calculate_factorial(number - 1)


def main() -> None:
    """
    Print factorial value.
    """

    print(calculate_factorial(5))


if __name__ == "__main__":
    main()
