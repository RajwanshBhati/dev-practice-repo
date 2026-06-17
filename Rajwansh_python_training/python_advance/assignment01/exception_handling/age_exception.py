"""
Question 7:
Create a custom exception called AgeException and raise it
if age is less than 18.
"""

MINIMUM_AGE = 18


class AgeException(Exception):
    """
    Custom exception for invalid age.

    This class is used when the age validation rule fails.
    """


def validate_age(age: int) -> None:
    """
    Validate user age.

    AgeException is raised if the age is less than the required minimum age.
    """

    if age < MINIMUM_AGE:
        raise AgeException(f"Age must be at least {MINIMUM_AGE} years.")


def main() -> None:
    """
    Take age input from user and validate it.
    """

    try:
        user_age = int(input("Enter age: "))
        validate_age(user_age)

        print("Age is valid.")

    except ValueError:
        print("Please enter a valid age.")

    except AgeException as error:
        print(error)


if __name__ == "__main__":
    main()
