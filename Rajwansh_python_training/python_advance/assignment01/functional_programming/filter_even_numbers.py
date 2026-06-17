"""
Question 3:
Use filter() to extract even numbers from a list.
"""

NUMBERS = [1, 2, 3, 4, 5, 6, 7, 8]


def print_even_numbers() -> None:
    """
    Extract even numbers using filter().

    filter() is used because only elements matching a condition
    should be selected.
    """

    even_numbers = filter(
        lambda number: number % 2 == 0,
        NUMBERS
    )

    print(list(even_numbers))


if __name__ == "__main__":
    print_even_numbers()
