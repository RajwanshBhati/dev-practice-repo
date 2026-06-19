"""
Assignment 1: NumPy Basics

Main file to run NumPy array statistics, array operations,
and matrix creation examples.
"""

from array_operations import ArrayOperationCalculator
from array_statistics import ArrayStatisticsCalculator
from constants import (
    BASIC_ARRAY_VALUES,
    FIRST_ARRAY_VALUES,
    MATRIX_VALUES,
    SECOND_ARRAY_VALUES,
)
from matrix_creator import MatrixCreator


def display_array_statistics() -> None:
    """
    Display mean, max, min, and sum for the basic NumPy array.

    This function only handles output related to array statistics.
    """

    statistics_calculator = ArrayStatisticsCalculator(BASIC_ARRAY_VALUES)

    print("Array Statistics")
    print(f"Mean: {statistics_calculator.calculate_mean()}")
    print(f"Max: {statistics_calculator.calculate_maximum()}")
    print(f"Min: {statistics_calculator.calculate_minimum()}")
    print(f"Sum: {statistics_calculator.calculate_sum()}")


def display_array_operations() -> None:
    """
    Display addition and multiplication result of two arrays.

    This function only handles output related to array operations.
    """

    operation_calculator = ArrayOperationCalculator(
        FIRST_ARRAY_VALUES,
        SECOND_ARRAY_VALUES,
    )

    print("\nArray Operations")
    print(f"Addition: {operation_calculator.add_arrays()}")
    print(f"Multiplication: {operation_calculator.multiply_arrays()}")


def display_matrix() -> None:
    """
    Display 3x3 NumPy matrix.

    This function only handles output related to matrix creation.
    """

    matrix_creator = MatrixCreator(MATRIX_VALUES)

    print("\n3x3 Matrix")
    print(matrix_creator.get_matrix())


def main() -> None:
    """
    Run all NumPy basics assignment tasks.

    Separate functions are called here to avoid writing all logic
    directly in one place.
    """

    display_array_statistics()
    display_array_operations()
    display_matrix()


if __name__ == "__main__":
    main()
