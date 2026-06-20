"""
Assignment 3:
Data Cleaning.

Tasks:
1. Detect missing values.
2. Replace missing Age with mean.
3. Replace missing Salary with 0.
"""

from constants import (
    AGE_COLUMN_NAME,
    EMPLOYEE_DATA,
    SALARY_COLUMN_NAME,
    SALARY_DEFAULT_VALUE,
)
from data_cleaner import EmployeeDataCleaner
from employee_dataframe_creator import (
    EmployeeDataFrameCreator,
)


def display_missing_values(
    data_cleaner: EmployeeDataCleaner,
) -> None:
    """
    Display missing value information.

    This function is responsible only for
    missing value output.
    """

    print("Missing Values")

    print(
        data_cleaner.detect_missing_values()
    )


def display_age_cleaning(
    data_cleaner: EmployeeDataCleaner,
) -> None:
    """
    Display DataFrame after replacing age values.

    This function is responsible only for
    age cleaning output.
    """

    print("\nAge Column After Mean Replacement")

    print(
        data_cleaner.replace_age_with_mean(
            AGE_COLUMN_NAME
        )
    )


def display_salary_cleaning(
    data_cleaner: EmployeeDataCleaner,
) -> None:
    """
    Display DataFrame after replacing salary values.

    This function is responsible only for
    salary cleaning output.
    """

    print("\nSalary Column After Replacement")

    print(
        data_cleaner.replace_salary_with_zero(
            SALARY_COLUMN_NAME,
            SALARY_DEFAULT_VALUE,
        )
    )


def main() -> None:
    """
    Execute all data cleaning tasks.

    Logic is separated into small functions
    to follow SRP.
    """

    dataframe_creator = (
        EmployeeDataFrameCreator(
            EMPLOYEE_DATA
        )
    )

    employee_dataframe = (
        dataframe_creator.create_dataframe()
    )

    data_cleaner = EmployeeDataCleaner(
        employee_dataframe
    )

    display_missing_values(data_cleaner)

    display_age_cleaning(data_cleaner)

    display_salary_cleaning(data_cleaner)


if __name__ == "__main__":
    main()
