"""
Question 23:
Generate random numbers using random module.
"""

import random

MINIMUM_RANDOM_NUMBER = 1
MAXIMUM_RANDOM_NUMBER = 100


def generate_random_number() -> int:
    """
    Generate and return a random number.
    """

    # Generate a random number within the specified range.
    return random.randint(
        MINIMUM_RANDOM_NUMBER,
        MAXIMUM_RANDOM_NUMBER
    )


def main() -> None:
    """Generate and display a random number."""

    random_number = generate_random_number()

    print(f"Generated Random Number: {random_number}")


if __name__ == "__main__":
    main()
