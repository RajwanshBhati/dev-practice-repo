"""
Assignment 6:
Create advanced charts using Seaborn.
"""

import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns


class SeabornChartCreator:
    """
    Create Seaborn charts.

    This class follows Single Responsibility Principle because it only
    handles chart creation logic.
    """

    def create_department_salary_barplot(
        self,
        employee_dataframe: pd.DataFrame,
        department_column_name: str,
        salary_column_name: str,
        chart_title: str,
    ) -> None:
        """
        Create barplot for Department vs Salary.

        Seaborn barplot is used because it shows comparison
        between categorical and numerical data.
        """

        plt.figure()

        sns.barplot(
            data=employee_dataframe,
            x=department_column_name,
            y=salary_column_name,
        )

        plt.title(chart_title)
        plt.xlabel("Department")
        plt.ylabel("Salary")

        plt.show()

    def create_salary_boxplot(
        self,
        employee_dataframe: pd.DataFrame,
        salary_column_name: str,
        chart_title: str,
    ) -> None:
        """
        Create boxplot for salary distribution.

        Boxplot is used to understand median, spread,
        and possible outliers in salary values.
        """

        plt.figure()

        sns.boxplot(
            data=employee_dataframe,
            y=salary_column_name,
        )

        plt.title(chart_title)
        plt.ylabel("Salary")

        plt.show()

    def create_age_salary_correlation_heatmap(
        self,
        employee_dataframe: pd.DataFrame,
        age_column_name: str,
        salary_column_name: str,
        chart_title: str,
    ) -> None:
        """
        Create heatmap for Age and Salary correlation.

        corr() calculates relationship between numerical columns.
        heatmap displays that relationship visually.
        """

        correlation_dataframe = employee_dataframe[
            [
                age_column_name,
                salary_column_name,
            ]
        ].corr()

        plt.figure()

        sns.heatmap(
            correlation_dataframe,
            annot=True,
        )

        plt.title(chart_title)

        plt.show()
