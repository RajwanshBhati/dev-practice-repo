"""
Question 14:
Find the factorial of a number.
"""


def calculate_factorial(number: int) -> int:
    """
    Calculate and return the factorial of a non-negative integer.

    Returns:
        int because Product of all integers from 1 to number.
    """

    # Start with 1 because it is the multiplicative identity.
    # This also ensures that 0! correctly evaluates to 1.
    factorial_result = 1

    # Multiply every number from 1 up to the given number.
    for current_number in range(1, number + 1):
        factorial_result *= current_number

    return factorial_result  # Return the final factorial value.


def main() -> None:
    """
    Read a number from the user and display its factorial.

    Returns:
        None: This function only handles input/output and
        does not need to return a value.
    """

    user_number = int(input("Enter a number: "))

    factorial_result = calculate_factorial(user_number)

    print(f"Factorial of {user_number} is {factorial_result}")


if __name__ == "__main__":
    # Ensures main() runs only when this file is executed directly,
    # not when imported into another Python program.
    main()
