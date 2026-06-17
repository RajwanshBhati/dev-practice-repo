"""
Question 4:
Use reduce() to find the product of all elements in a list.
"""

from functools import reduce

NUMBERS = [1, 2, 3, 4, 5]


def print_product() -> None:
    """
    Calculate product using reduce().

    reduce() is used because all values must be combined
    into a single result.
    """

    product = reduce(
        lambda first_number, second_number:
        first_number * second_number,
        NUMBERS
    )

    print(product)


if __name__ == "__main__":
    print_product()
