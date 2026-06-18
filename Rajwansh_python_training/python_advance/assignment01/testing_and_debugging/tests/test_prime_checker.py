"""
Test cases for prime_checker.py.

Multiple scenarios are tested to make sure prime checking works correctly.
"""

from src.prime_checker import is_prime


def test_prime_number() -> None:
    """
    Test a valid prime number.

    7 is prime because it is divisible only by 1 and 7.
    """

    assert is_prime(7) is True


def test_non_prime_number() -> None:
    """
    Test a non-prime number.

    10 is not prime because it is divisible by 2 and 5.
    """

    assert is_prime(10) is False


def test_one_is_not_prime() -> None:
    """
    Test number 1.

    1 is not considered a prime number.
    """

    assert is_prime(1) is False


def test_negative_number_is_not_prime() -> None:
    """
    Test negative number.

    Negative numbers are not prime.
    """

    assert is_prime(-5) is False
