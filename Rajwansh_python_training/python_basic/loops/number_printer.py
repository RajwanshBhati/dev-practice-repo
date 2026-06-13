"""
Question 12:
Print numbers from 1 to 100 using loop.
"""

START_NUMBER = 1
END_NUMBER = 100


class NumberPrinter:
    """Print numbers within a specified range."""

    def print_numbers(self) -> None:
        """
        Print numbers from START_NUMBER to END_NUMBER.

        I use a for loop because we know exactly
        how many times the loop should run.
        """
        for number in range(START_NUMBER, END_NUMBER + 1):
            print(number)


def main() -> None:
    """Program entry point."""
    number_printer = NumberPrinter()
    number_printer.print_numbers()


if __name__ == "__main__":
    main()
