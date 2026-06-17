"""
Question 2:
Use map() to convert a list of numbers into their squares.
"""

NUMBERS = [1, 2, 3, 4, 5]


def print_square_numbers() -> None:
    """
    Convert numbers into squares using map().

    map() is used because the same operation must be applied
    to every element of the list.
    """

    square_numbers = map(
        lambda number: number ** 2,
        NUMBERS
    )

    print(list(square_numbers))


if __name__ == "__main__":
    print_square_numbers()
