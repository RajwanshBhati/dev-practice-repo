"""
Question 6:
Create a function that raises a ValueError if a number is negative.
"""


def validate_positive_number(number: int) -> None:
    """
    Validate that the given number is not negative.

    ValueError is raised manually because negative numbers are not allowed
    as per the program requirement.
    """

    if number < 0:
        raise ValueError("Negative numbers are not allowed.")


def main() -> None:
    """
    Take user input and validate the number.
    """

    try:
        user_number = int(input("Enter a number: "))
        validate_positive_number(user_number)

        print("Valid number.")

    except ValueError as error:
        print(error)


if __name__ == "__main__":
    main()
