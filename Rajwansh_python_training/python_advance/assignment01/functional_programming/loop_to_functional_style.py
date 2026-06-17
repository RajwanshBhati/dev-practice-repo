"""
Question 7:
Convert a simple loop-based program into a functional style
using map or filter.
"""

NUMBERS = [1, 2, 3, 4, 5]


def print_square_numbers() -> None:
    """
    Convert loop-based logic into functional style.

    map() removes the need for manually iterating and
    appending values.
    """

    square_numbers = map(
        lambda number: number ** 2,
        NUMBERS
    )

    print(list(square_numbers))


if __name__ == "__main__":
    print_square_numbers()
