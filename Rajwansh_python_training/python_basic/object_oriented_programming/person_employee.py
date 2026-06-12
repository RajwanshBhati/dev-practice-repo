"""
Question 42:
Implement inheritance using Person and Employee class.
"""


class Person:
    """Store common person details."""

    def __init__(self, name: str, age: int) -> None:
        # These details are common for every person.
        self.name = name
        self.age = age

    def display_person_details(self) -> None:
        """Display person details."""

        print(f"Name: {self.name}")
        print(f"Age: {self.age}")


class Employee(Person):
    """Store employee details by reusing Person class."""

    def __init__(self, name: str, age: int, employee_id: int) -> None:
        # Use parent class constructor to avoid writing same code again.
        super().__init__(name, age)

        self.employee_id = employee_id

    def display_employee_details(self) -> None:
        """Display employee details."""

        self.display_person_details()
        print(f"Employee ID: {self.employee_id}")


def main() -> None:
    """Program entry point."""

    employee = Employee("Rajwansh", 22, 101)
    employee.display_employee_details()


if __name__ == "__main__":
    main()
