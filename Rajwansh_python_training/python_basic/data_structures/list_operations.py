"""
Question 25:
Create a list of 10 numbers and find sum, max, sort it,
and remove duplicates.
"""


def process_number_list(numbers: list[int]) -> None:
    """
    Display sum, maximum value, sorted list,
    and list without duplicates.

    numbers: list[int]
    Used to indicate that the function expects a list containing integer values.

    -> None
    Used because the function only displays output and does not return any value.
    """

    # Calculate the sum of all numbers in the list.
    total_sum = sum(numbers)

    # Find the largest number present in the list.
    maximum_number = max(numbers)

    # Create a sorted version of the list.
    sorted_numbers = sorted(numbers)

    # Convert the list to a set to remove duplicate values.
    unique_numbers = list(set(numbers))

    print(f"Sum: {total_sum}")
    print(f"Maximum Number: {maximum_number}")
    print(f"Sorted List: {sorted_numbers}")
    print(f"List Without Duplicates: {unique_numbers}")


def main() -> None:
    """Create a sample list and perform operations."""

    number_list = [10, 20, 30, 40, 50, 20, 30, 60, 70, 80]

    process_number_list(number_list)

# Used to execute the main program only when this file
# is run directly, not when imported as a module.
if __name__ == "__main__":
    main()
