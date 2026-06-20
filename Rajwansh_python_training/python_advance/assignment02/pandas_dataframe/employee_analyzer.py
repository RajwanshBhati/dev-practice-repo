"""
Assignment 2:
Perform DataFrame operations.
"""

import pandas as pd


class EmployeeAnalyzer:
    """
    Analyze employee DataFrame.

    This class handles filtering, statistics,
    and bonus calculations.
    """

    def __init__(self, employee_dataframe: pd.DataFrame) -> None:
        """
        Store employee DataFrame.

        The DataFrame is reused by all methods.
        """

        self.employee_dataframe = employee_dataframe

    def get_first_rows(
        self,
        number_of_rows: int,
    ) -> pd.DataFrame:
        """
        Return first rows from DataFrame.

        head() is used because it efficiently returns
        top rows.
        """

        return self.employee_dataframe.head(number_of_rows)

    def get_summary_statistics(self) -> pd.DataFrame:
        """
        Return summary statistics.

        describe() provides count, mean, min,
        max, and other statistical values.
        """

        return self.employee_dataframe.describe()

    def get_department_employees(
        self,
        department_name: str,
    ) -> pd.DataFrame:
        """
        Return employees belonging to a specific department.

        Boolean filtering is used to select matching rows.
        """

        return self.employee_dataframe[
            self.employee_dataframe["Department"]
            == department_name
        ]

    def add_bonus_column(
        self,
        bonus_percentage: float,
    ) -> pd.DataFrame:
        """
        Add bonus column.

        Bonus is calculated as Salary multiplied
        by bonus percentage.
        """

        self.employee_dataframe["Bonus"] = (
            self.employee_dataframe["Salary"]
            * bonus_percentage
        )

        return self.employee_dataframe
