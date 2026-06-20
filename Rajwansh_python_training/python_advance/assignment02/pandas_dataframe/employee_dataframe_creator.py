"""
Assignment 2:
Create employee DataFrame.
"""

import pandas as pd


class EmployeeDataFrameCreator:
    """
    Create employee DataFrame.

    This class is responsible only for DataFrame creation.
    """

    def __init__(self, employee_data: dict) -> None:
        """
        Store employee data.

        Data is received from constants rather than hardcoding values.
        """

        self.employee_data = employee_data

    def create_dataframe(self) -> pd.DataFrame:
        """
        Create and return employee DataFrame.

        pd.DataFrame() converts dictionary data into a tabular structure.
        """

        return pd.DataFrame(self.employee_data)
