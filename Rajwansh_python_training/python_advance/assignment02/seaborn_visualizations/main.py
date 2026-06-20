"""
Assignment 6:
Seaborn Visualizations.

Tasks:
1. Create Barplot for Department vs Salary.
2. Create Boxplot for Salary distribution.
3. Create Heatmap using correlation between Age and Salary.
"""

from constants import (
    AGE_COLUMN_NAME,
    BARPLOT_TITLE,
    BOXPLOT_TITLE,
    DEPARTMENT_COLUMN_NAME,
    EMPLOYEE_DATA,
    HEATMAP_TITLE,
    SALARY_COLUMN_NAME,
)
from employee_dataframe_creator import EmployeeDataFrameCreator
from seaborn_chart_creator import SeabornChartCreator


def display_department_salary_barplot(
    chart_creator: SeabornChartCreator,
    employee_dataframe,
) -> None:
    """
    Display Department vs Salary barplot.

    This function is responsible only for barplot output.
    """

    chart_creator.create_department_salary_barplot(
        employee_dataframe,
        DEPARTMENT_COLUMN_NAME,
        SALARY_COLUMN_NAME,
        BARPLOT_TITLE,
    )


def display_salary_boxplot(
    chart_creator: SeabornChartCreator,
    employee_dataframe,
) -> None:
    """
    Display salary distribution boxplot.

    This function is responsible only for boxplot output.
    """

    chart_creator.create_salary_boxplot(
        employee_dataframe,
        SALARY_COLUMN_NAME,
        BOXPLOT_TITLE,
    )


def display_age_salary_heatmap(
    chart_creator: SeabornChartCreator,
    employee_dataframe,
) -> None:
    """
    Display Age and Salary correlation heatmap.

    This function is responsible only for heatmap output.
    """

    chart_creator.create_age_salary_correlation_heatmap(
        employee_dataframe,
        AGE_COLUMN_NAME,
        SALARY_COLUMN_NAME,
        HEATMAP_TITLE,
    )


def main() -> None:
    """
    Execute all Seaborn visualization tasks.

    Logic is separated into small functions to avoid
    monolithic script structure.
    """

    dataframe_creator = EmployeeDataFrameCreator(
        EMPLOYEE_DATA
    )

    employee_dataframe = dataframe_creator.create_dataframe()

    chart_creator = SeabornChartCreator()

    display_department_salary_barplot(
        chart_creator,
        employee_dataframe,
    )

    display_salary_boxplot(
        chart_creator,
        employee_dataframe,
    )

    display_age_salary_heatmap(
        chart_creator,
        employee_dataframe,
    )


if __name__ == "__main__":
    main()
