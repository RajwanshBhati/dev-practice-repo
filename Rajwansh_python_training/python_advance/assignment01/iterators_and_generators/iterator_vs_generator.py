"""
Question 6:
Explain the difference between iterator and generator with a small example.
"""


class SimpleIterator:
    """
    Custom iterator example.

    Iterator needs __iter__() and __next__() methods manually.
    """

    def __init__(self) -> None:
        """Initialize current value."""

        self.current_number = 1

    def __iter__(self) -> "SimpleIterator":
        """Return iterator object."""

        return self

    def __next__(self) -> int:
        """Return next value until 3."""

        if self.current_number > 3:
            raise StopIteration

        number = self.current_number
        self.current_number += 1

        return number


def simple_generator():
    """
    Generator example.

    Generator is shorter because yield automatically creates iterator behavior.
    """

    yield 1
    yield 2
    yield 3


def main() -> None:
    """Compare iterator and generator output."""

    print("Iterator output:")
    for number in SimpleIterator():
        print(number)

    print("Generator output:")
    for number in simple_generator():
        print(number)


if __name__ == "__main__":
    main()
