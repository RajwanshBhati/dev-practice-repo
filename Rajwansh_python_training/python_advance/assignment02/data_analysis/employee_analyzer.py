"""
Assignment 4:
Perform employee data analysis using GroupBy.
"""

import pandas as pd


class EmployeeAnalyzer:
    """
    Analyze employee data.

    This class is responsible only for
    groupby-based analysis.
    """

    def __init__(
        self,
        employee_dataframe: pd.DataFrame,
    ) -> None:
        """
        Store employee DataFrame.
        """

        self.employee_dataframe = employee_dataframe

    def calculate_average_salary_by_department(
        self,
        department_column_name: str,
        salary_column_name: str,
    ) -> pd.Series:
        """
        Calculate average salary department-wise.

        groupby() groups records by department and mean()
        calculates average salary.
        """

        return (
            self.employee_dataframe
            .groupby(department_column_name)[
                salary_column_name
            ]
            .mean()
        )

    def calculate_maximum_salary_by_department(
        self,
        department_column_name: str,
        salary_column_name: str,
    ) -> pd.Series:
        """
        Calculate maximum salary department-wise.

        max() returns the highest salary
        from each department.
        """

        return (
            self.employee_dataframe
            .groupby(department_column_name)[
                salary_column_name
            ]
            .max()
        )

    def calculate_employee_count_by_department(
        self,
        department_column_name: str,
        name_column_name: str,
    ) -> pd.Series:
        """
        Count employees in each department.

        count() returns number of employees
        present in each group.
        """

        return (
            self.employee_dataframe
            .groupby(department_column_name)[
                name_column_name
            ]
            .count()
        )
