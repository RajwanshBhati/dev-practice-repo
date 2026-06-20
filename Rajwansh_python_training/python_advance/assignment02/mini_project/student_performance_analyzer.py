"""
Assignment 7:
Analyze student performance.
"""

import pandas as pd


class StudentPerformanceAnalyzer:
    """
    Analyze student performance.

    This class handles performance-related calculations.
    """

    def __init__(
        self,
        student_dataframe: pd.DataFrame,
    ) -> None:
        """
        Store student DataFrame.
        """

        self.student_dataframe = student_dataframe

    def add_performance_column(
        self,
        marks_column_name: str,
        performance_column_name: str,
        passing_marks: int,
        pass_status: str,
        fail_status: str,
    ) -> pd.DataFrame:
        """
        Add Performance column.

        Students scoring above passing marks are marked as Pass,
        otherwise Fail.
        """

        self.student_dataframe[
            performance_column_name
        ] = self.student_dataframe[
            marks_column_name
        ].apply(
            lambda marks:
            pass_status
            if marks > passing_marks
            else fail_status
        )

        return self.student_dataframe
