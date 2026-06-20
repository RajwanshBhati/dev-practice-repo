"""
Assignment 4:
Data Analysis Using GroupBy.

Tasks:
1. Find average salary by department.
2. Find maximum salary by department.
3. Count employees per department.
"""

from constants import (
    DEPARTMENT_COLUMN_NAME,
    EMPLOYEE_DATA,
    NAME_COLUMN_NAME,
    SALARY_COLUMN_NAME,
)
from employee_analyzer import EmployeeAnalyzer
from employee_dataframe_creator import (
    EmployeeDataFrameCreator,
)


def display_average_salary(
    employee_analyzer: EmployeeAnalyzer,
) -> None:
    """
    Display average salary department-wise.

    This function is responsible only for
    average salary output.
    """

    print("Average Salary By Department")

    print(
        employee_analyzer
        .calculate_average_salary_by_department(
            DEPARTMENT_COLUMN_NAME,
            SALARY_COLUMN_NAME,
        )
    )


def display_maximum_salary(
    employee_analyzer: EmployeeAnalyzer,
) -> None:
    """
    Display maximum salary department-wise.

    This function is responsible only for
    maximum salary output.
    """

    print("\nMaximum Salary By Department")

    print(
        employee_analyzer
        .calculate_maximum_salary_by_department(
            DEPARTMENT_COLUMN_NAME,
            SALARY_COLUMN_NAME,
        )
    )


def display_employee_count(
    employee_analyzer: EmployeeAnalyzer,
) -> None:
    """
    Display employee count department-wise.

    This function is responsible only for
    employee count output.
    """

    print("\nEmployee Count By Department")

    print(
        employee_analyzer
        .calculate_employee_count_by_department(
            DEPARTMENT_COLUMN_NAME,
            NAME_COLUMN_NAME,
        )
    )


def main() -> None:
    """
    Execute all data analysis tasks.

    Logic is separated into small functions
    to follow SRP and avoid monolithic code.
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

    display_average_salary(employee_analyzer)

    display_maximum_salary(employee_analyzer)

    display_employee_count(employee_analyzer)


if __name__ == "__main__":
    main()
