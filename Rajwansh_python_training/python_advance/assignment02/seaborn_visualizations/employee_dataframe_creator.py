"""
Assignment 6:
Create employee DataFrame for Seaborn visualizations.
"""

import pandas as pd


class EmployeeDataFrameCreator:
    """
    Create employee DataFrame.

    This class is responsible only for converting employee data
    into a Pandas DataFrame.
    """

    def __init__(self, employee_data: dict) -> None:
        """
        Store employee data.

        Data is passed from constants to avoid hardcoded values.
        """

        self.employee_data = employee_data

    def create_dataframe(self) -> pd.DataFrame:
        """
        Create and return employee DataFrame.

        pd.DataFrame() is used to convert dictionary data
        into tabular format.
        """

        return pd.DataFrame(self.employee_data)
