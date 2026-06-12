"""
Question 28:
Create a tuple and access elements.
"""


def display_tuple_elements() -> None:
    """
    Create a tuple and display its elements.
    """

    student_details = ("Rajwansh", 22, "Python")

    # Access individual elements using their position.
    print(f"Name: {student_details[0]}")
    print(f"Age: {student_details[1]}")
    print(f"Course: {student_details[2]}")


def main() -> None:
    """Program entry point."""

    display_tuple_elements()


if __name__ == "__main__":
    main()
