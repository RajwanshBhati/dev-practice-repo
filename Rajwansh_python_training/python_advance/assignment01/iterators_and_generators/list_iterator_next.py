"""
Question 1:
Create an iterator for a list and print elements using next().
"""


def print_list_using_iterator() -> None:
    """
    Convert a list into an iterator and print values using next().

    iter() is used because a list is iterable, but next() works on an iterator.
    """

    numbers = [10, 20, 30, 40]

    number_iterator = iter(numbers)

    print(next(number_iterator))
    print(next(number_iterator))
    print(next(number_iterator))
    print(next(number_iterator))


if __name__ == "__main__":
    print_list_using_iterator()
