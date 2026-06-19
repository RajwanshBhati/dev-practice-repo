"""
Assignment 1: NumPy Basics

Task:
Create a 3x3 matrix using NumPy.
"""

import numpy as np


class MatrixCreator:
    """
    Create a NumPy matrix.

    This class is responsible only for creating and returning matrix data.
    """

    def __init__(self, matrix_values: list[list[int]]) -> None:
        """
        Initialize matrix values.

        A two-dimensional list is converted into a NumPy array to create
        a matrix-like structure.
        """

        self.matrix = np.array(matrix_values)

    def get_matrix(self) -> np.ndarray:
        """
        Return the created matrix.

        Returning the matrix allows this class to be reused in other files.
        """

        return self.matrix
