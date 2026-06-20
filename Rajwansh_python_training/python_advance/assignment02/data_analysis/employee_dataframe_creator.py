"""
Assignment 4:
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
        """

        self.employee_data = employee_data

    def create_dataframe(self) -> pd.DataFrame:
        """
        Create and return DataFrame.

        pd.DataFrame() converts dictionary data into tabular format.
        """

        return pd.DataFrame(self.employee_data)
