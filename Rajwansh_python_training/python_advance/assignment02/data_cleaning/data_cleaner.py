"""
Assignment 3:
Handle missing values in employee data.
"""

import pandas as pd


class EmployeeDataCleaner:
    """
    Clean employee DataFrame.

    This class handles missing value detection
    and replacement operations.
    """

    def __init__(
        self,
        employee_dataframe: pd.DataFrame,
    ) -> None:
        """
        Store DataFrame for cleaning operations.
        """

        self.employee_dataframe = employee_dataframe

    def detect_missing_values(self) -> pd.DataFrame:
        """
        Detect missing values.

        isnull() returns True for missing values
        and False for available values.
        """

        return self.employee_dataframe.isnull()

    def replace_age_with_mean(
        self,
        age_column_name: str,
    ) -> pd.DataFrame:
        """
        Replace missing age with average age.

        Mean is commonly used because it preserves
        the overall age distribution.
        """

        mean_age = (
            self.employee_dataframe[
                age_column_name
            ].mean()
        )

        self.employee_dataframe[
            age_column_name
        ] = (
            self.employee_dataframe[
                age_column_name
            ].fillna(mean_age)
        )

        return self.employee_dataframe

    def replace_salary_with_zero(
        self,
        salary_column_name: str,
        default_salary: int,
    ) -> pd.DataFrame:
        """
        Replace missing salary with zero.

        fillna() is used because it replaces
        missing values in a column.
        """

        self.employee_dataframe[
            salary_column_name
        ] = (
            self.employee_dataframe[
                salary_column_name
            ].fillna(default_salary)
        )

        return self.employee_dataframe
