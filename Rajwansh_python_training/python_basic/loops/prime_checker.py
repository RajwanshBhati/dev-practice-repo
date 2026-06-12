"""
Question 16:
Check whether a number is prime.
"""

MINIMUM_PRIME_NUMBER = 2


def is_prime(number: int) -> bool:
    """
    Check whether the given number is prime.

    A prime number is divisible only by
    1 and itself.

    Returns:
        bool: True if the number is prime, otherwise False.
    """

    # Numbers less than 2 are not considered prime.
    if number < MINIMUM_PRIME_NUMBER:
        return False

    # Check if any number between 2 and number-1 divides it exactly.
    for divisor in range(2, number):
        if number % divisor == 0:

            # If a divisor is found, the number is not prime.
            return False

    # No divisors were found, so the number is prime.
    return True


def main() -> None:
    """
    Take input from the user and check whether it is prime.

    Returns:
        None: This function only handles input and output.
    """

    user_number = int(input("Enter a number: "))

    if is_prime(user_number):
        print(f"{user_number} is a Prime Number.")
    else:
        print(f"{user_number} is not a Prime Number.")


if __name__ == "__main__":
    # Run the program only when this file is executed directly.
    main()
