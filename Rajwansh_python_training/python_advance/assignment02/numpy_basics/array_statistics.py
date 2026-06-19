"""
Assignment 1: NumPy Basics

Task:
Create a NumPy array [10, 20, 30, 40, 50] and perform:
mean, max, min, and sum.
"""

import numpy as np


class ArrayStatisticsCalculator:
    """
    Calculate basic statistics for a NumPy array.

    This class follows Single Responsibility Principle because it only
    handles statistical operations like mean, max, min, and sum.
    """

    def __init__(self, values: list[int]) -> None:
        """
        Initialize NumPy array using provided values.

        np.array() is used because NumPy arrays are optimized for
        numerical operations.
        """

        self.number_array = np.array(values)

    def calculate_mean(self) -> float:
        """
        Calculate mean value of the array.

        np.mean() is used because it directly calculates average
        of numerical values.
        """

        return float(np.mean(self.number_array))

    def calculate_maximum(self) -> int:
        """
        Calculate maximum value of the array.

        np.max() is used because it finds the largest element efficiently.
        """

        return int(np.max(self.number_array))

    def calculate_minimum(self) -> int:
        """
        Calculate minimum value of the array.

        np.min() is used because it finds the smallest element efficiently.
        """

        return int(np.min(self.number_array))

    def calculate_sum(self) -> int:
        """
        Calculate sum of all array elements.

        np.sum() is used because it performs optimized array summation.
        """

        return int(np.sum(self.number_array))
