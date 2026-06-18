"""
Test cases for addition.py.

pytest automatically detects functions whose names start with test_.
"""

from src.addition import add_numbers


def test_add_positive_numbers() -> None:
    """
    Test addition of two positive numbers.

    This test checks normal positive input scenario.
    """

    assert add_numbers(10, 20) == 30


def test_add_negative_numbers() -> None:
    """
    Test addition of two negative numbers.

    This test checks whether function handles negative values correctly.
    """

    assert add_numbers(-10, -5) == -15


def test_add_positive_and_negative_number() -> None:
    """
    Test addition of one positive and one negative number.

    This test checks mixed sign input scenario.
    """

    assert add_numbers(10, -5) == 5
