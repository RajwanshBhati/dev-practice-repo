"""
Assignment 1: NumPy Basics

Task:
Create two arrays:
arr_1 = [1, 2, 3]
arr_2 = [4, 5, 6]

Perform addition and multiplication.
"""

import numpy as np


class ArrayOperationCalculator:
    """
    Perform element-wise operations on two NumPy arrays.

    This class is responsible only for array addition and multiplication.
    """

    def __init__(
        self,
        first_values: list[int],
        second_values: list[int],
    ) -> None:
        """
        Initialize two NumPy arrays.

        np.array() converts normal Python lists into NumPy arrays so that
        element-wise operations can be performed directly.
        """

        self.first_array = np.array(first_values)
        self.second_array = np.array(second_values)

    def add_arrays(self) -> np.ndarray:
        """
        Add two arrays element by element.

        NumPy automatically performs element-wise addition when using +.
        """

        return self.first_array + self.second_array

    def multiply_arrays(self) -> np.ndarray:
        """
        Multiply two arrays element by element.

        NumPy automatically performs element-wise multiplication when using *.
        """

        return self.first_array * self.second_array
