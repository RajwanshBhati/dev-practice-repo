"""
Question 2:
Write pytest test cases for a function that checks whether a number is prime.
"""

MINIMUM_PRIME_NUMBER = 2


def is_prime(number: int) -> bool:
    """
    Check whether a number is prime.

    A prime number is greater than 1 and divisible only by 1 and itself.
    """

    if number < MINIMUM_PRIME_NUMBER:
        return False

    for divisor in range(2, number):
        if number % divisor == 0:
            return False

    return True
