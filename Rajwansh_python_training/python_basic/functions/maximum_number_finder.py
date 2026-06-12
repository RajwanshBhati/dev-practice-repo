"""
Question 19:
Write a function that returns maximum number from a list.
"""


def find_maximum_number(numbers: list[int]) -> int:
    """
    Return the largest number from the given list.
    """

    maximum_number = numbers[0]

    # Compare each number with the current maximum value.
    for current_number in numbers:
        if current_number > maximum_number:
            maximum_number = current_number

    return maximum_number


def main() -> None:
    """Create a sample list and display the maximum number."""

    number_list = [12, 45, 8, 90, 33]

    maximum_number = find_maximum_number(number_list)

    print(f"Maximum number is {maximum_number}")


if __name__ == "__main__":
    main()
