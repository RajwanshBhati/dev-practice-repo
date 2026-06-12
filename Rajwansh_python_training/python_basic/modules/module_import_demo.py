"""
Question 24:
Create your own module and import it.
"""

from calculator import add_numbers


def main() -> None:
    """Demonstrate custom module import."""

    addition_result = add_numbers(10, 20)

    print(f"Addition Result: {addition_result}")


if __name__ == "__main__":
    main()
