"""
Question 31:
Remove duplicates from list using set.
"""


def remove_duplicates(numbers: list[int]) -> list[int]:
    """
    Remove duplicate values from the list.
    """

    # Use a set to automatically remove duplicate values.
    return list(set(numbers))


def main() -> None:
    """Create sample list and remove duplicates."""

    number_list = [10, 20, 20, 30, 40, 40, 50]

    unique_numbers = remove_duplicates(number_list)

    print(unique_numbers)


if __name__ == "__main__":
    main()
