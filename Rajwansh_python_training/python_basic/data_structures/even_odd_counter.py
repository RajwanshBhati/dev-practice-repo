"""
Question 26:
Count even and odd numbers in a list.
"""

DIVISIBILITY_NUMBER = 2


def count_even_and_odd_numbers(numbers: list[int]) -> tuple[int, int]:
    """
    Count and return total even and odd numbers.
    """

    even_count = 0
    odd_count = 0

    # Check each number and update the appropriate counter.
    for current_number in numbers:
        if current_number % DIVISIBILITY_NUMBER == 0:
            even_count += 1
        else:
            odd_count += 1

    return even_count, odd_count


def main() -> None:
    """Here I Create a sample list and display counts."""

    number_list = [10, 15, 20, 25, 30, 35, 40]

    even_count, odd_count = count_even_and_odd_numbers(number_list)

    print(f"Even Numbers: {even_count}")
    print(f"Odd Numbers: {odd_count}")


if __name__ == "__main__":
    main()
