"""
Question 8:
Show an example of a built-in generator like range and iterate over it.
"""


def print_range_values() -> None:
    """
    Print values using range.

    range is memory efficient because it does not create a full list immediately.
    """

    number_range = range(1, 6)

    for number in number_range:
        print(number)


if __name__ == "__main__":
    print_range_values()
