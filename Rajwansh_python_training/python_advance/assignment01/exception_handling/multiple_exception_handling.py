"""
Question 4:
Handle multiple exceptions in a single program.
"""


def process_number() -> None:
    """
    Take input and divide 100 by the entered number.

    ValueError handles invalid input.
    ZeroDivisionError handles division by zero.
    """

    try:
        user_number = int(input("Enter a number: "))
        result = 100 / user_number

        print(f"Result: {result}")

    except ValueError:
        print("Invalid integer entered.")

    except ZeroDivisionError:
        print("Cannot divide by zero.")


if __name__ == "__main__":
    process_number()
