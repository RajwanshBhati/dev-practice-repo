"""
Question 3:
Use a package with two modules.
"""

from student_package.student_details import get_student_details
from student_package.student_marks import calculate_total_marks


def main() -> None:
    """
    Import and use modules from student_package.

    Package import is used to keep related modules organized together.
    """

    student_details = get_student_details("Rajwansh", "Python")
    total_marks = calculate_total_marks(80, 90)

    print(student_details)
    print(f"Total Marks: {total_marks}")


if __name__ == "__main__":
    main()
