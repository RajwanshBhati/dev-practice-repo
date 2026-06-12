"""
Question 32:
Create a student dictionary and access values.
"""


def display_student_details() -> None:
    """
    Create a dictionary and display values.
    """

    student_details = {
        "name": "Rajwansh",
        "age": 22,
        "course": "Python"
    }

    # Access values using their corresponding keys.
    print(f"Name: {student_details['name']}")
    print(f"Age: {student_details['age']}")
    print(f"Course: {student_details['course']}")


def main() -> None:
    """Program entry point."""

    display_student_details()


if __name__ == "__main__":
    main()
