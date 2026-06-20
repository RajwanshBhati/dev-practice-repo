"""
Assignment 7:
Create Student DataFrame.
"""

import pandas as pd


class StudentDataFrameCreator:
    """
    Create student DataFrame.

    This class is responsible only for DataFrame creation.
    """

    def __init__(self, student_data: dict) -> None:
        """
        Store student data.
        """

        self.student_data = student_data

    def create_dataframe(self) -> pd.DataFrame:
        """
        Create and return student DataFrame.
        """

        return pd.DataFrame(self.student_data)
