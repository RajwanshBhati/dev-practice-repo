"""
Constants for data cleaning assignment.

Constants are stored separately to avoid hardcoded values.
"""

import numpy as np

EMPLOYEE_DATA = {
    "Name": [
        "Rahul",
        "Priya",
        "Anuj",
    ],
    "Age": [
        25,
        np.nan,
        29,
    ],
    "Salary": [
        30000,
        40000,
        np.nan,
    ],
}

AGE_COLUMN_NAME = "Age"

SALARY_COLUMN_NAME = "Salary"

SALARY_DEFAULT_VALUE = 0
