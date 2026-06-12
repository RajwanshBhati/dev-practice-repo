TABLE_LIMIT = 10


def print_multiplication_table(number: int) -> None:
    """
    Print multiplication table for the given number.
    """

    # Run the loop from 1 to TABLE_LIMIT.
    for multiplier in range(1, TABLE_LIMIT + 1):
        result = number * multiplier

        print(f"{number} x {multiplier} = {result}")


def main() -> None:
    """Take input and display multiplication table."""

    user_number = int(input("Enter a number: "))

    print_multiplication_table(user_number)


if __name__ == "__main__":
    main()
