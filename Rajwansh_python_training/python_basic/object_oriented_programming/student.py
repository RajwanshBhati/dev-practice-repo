"""
Question 40:
Create a Student class with attributes and display details.
"""


class Student:
    """Store student details and display them."""

    def __init__(self, name: str, age: int, course: str) -> None:
        # Store student information inside the object.
        self.name = name
        self.age = age
        self.course = course

    def display_details(self) -> None:
        """Display student details."""

        print(f"Name: {self.name}")
        print(f"Age: {self.age}")
        print(f"Course: {self.course}")


def main() -> None:
    """Program entry point."""

    student = Student("Rajwansh", 22, "Python")
    student.display_details()


if __name__ == "__main__":
    main()
