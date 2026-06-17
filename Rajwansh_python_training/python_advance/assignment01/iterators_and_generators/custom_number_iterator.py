"""
Question 2:
Write a custom iterator class that returns numbers from 1 to N.
"""


class NumberIterator:
    """
    Custom iterator class to return numbers from 1 to the given limit.

    __iter__() returns the iterator object.
    __next__() returns the next value one by one.
    """

    def __init__(self, limit: int) -> None:
        """Initialize current number and maximum limit."""

        self.current_number = 1
        self.limit = limit

    def __iter__(self) -> "NumberIterator":
        """Return the iterator object itself."""

        return self

    def __next__(self) -> int:
        """
        Return the next number.

        StopIteration is raised when all numbers are completed.
        """

        if self.current_number > self.limit:
            raise StopIteration

        number = self.current_number
        self.current_number += 1

        return number


def main() -> None:
    """Create custom iterator and print numbers."""

    number_iterator = NumberIterator(5)

    for number in number_iterator:
        print(number)


if __name__ == "__main__":
    main()
