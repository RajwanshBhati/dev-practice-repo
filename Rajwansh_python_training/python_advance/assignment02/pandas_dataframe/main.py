"""
Assignment 2:
Pandas DataFrame Creation.

Tasks:
1. Create employee DataFrame.
2. Show first 2 rows.
3. Show summary statistics.
4. Display IT employees.
5. Add Bonus column.
"""

from constants import (
    BONUS_PERCENTAGE,
    EMPLOYEE_DATA,
    FIRST_TWO_ROWS,
    IT_DEPARTMENT_NAME,
)
from employee_analyzer import EmployeeAnalyzer
from employee_dataframe_creator import (
    EmployeeDataFrameCreator,
)


def display_first_rows(
    employee_analyzer: EmployeeAnalyzer,
) -> None:
    """
    Display first two rows.

    This function is responsible only for
    showing initial rows.
    """

    print("First Two Rows")

    print(
        employee_analyzer.get_first_rows(
            FIRST_TWO_ROWS
        )
    )


def display_summary_statistics(
    employee_analyzer: EmployeeAnalyzer,
) -> None:
    """
    Display DataFrame statistics.

    This function is responsible only for
    statistical output.
    """

    print("\nSummary Statistics")

    print(
        employee_analyzer.get_summary_statistics()
    )


def display_it_employees(
    employee_analyzer: EmployeeAnalyzer,
) -> None:
    """
    Display only IT employees.

    This function is responsible only for
    department filtering output.
    """

    print("\nIT Employees")

    print(
        employee_analyzer.get_department_employees(
            IT_DEPARTMENT_NAME
        )
    )


def display_bonus_data(
    employee_analyzer: EmployeeAnalyzer,
) -> None:
    """
    Display DataFrame after bonus calculation.

    This function is responsible only for
    bonus output.
    """

    print("\nEmployees With Bonus")

    print(
        employee_analyzer.add_bonus_column(
            BONUS_PERCENTAGE
        )
    )


def main() -> None:
    """
    Execute all assignment tasks.

    Logic is split into separate functions
    to avoid monolithic code.
    """

    dataframe_creator = (
        EmployeeDataFrameCreator(
            EMPLOYEE_DATA
        )
    )

    employee_dataframe = (
        dataframe_creator.create_dataframe()
    )

    employee_analyzer = EmployeeAnalyzer(
        employee_dataframe
    )

    display_first_rows(employee_analyzer)

    display_summary_statistics(
        employee_analyzer
    )

    display_it_employees(employee_analyzer)

    display_bonus_data(employee_analyzer)


if __name__ == "__main__":
    main()
