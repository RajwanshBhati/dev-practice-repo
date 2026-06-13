"""
Question 27:
Reverse a list without using reverse().
"""


def reverse_list(numbers: list[int]) -> list[int]:
    """
    Return a reversed version of the list.
    """

    reversed_numbers = []

    # Add elements from the end of the list to the new list.
    for index in range(len(numbers) - 1, -1, -1):
        reversed_numbers.append(numbers[index])

    return reversed_numbers


def main() -> None:
    """Create a sample list and display the reversed list."""

    number_list = [10, 20, 30, 40, 50]

    reversed_numbers = reverse_list(number_list)

    print(f"Original List: {number_list}")
    print(f"Reversed List: {reversed_numbers}")

# Used to execute the main program only when this file
# is run directly, not when imported as a module.
if __name__ == "__main__":
    main()
