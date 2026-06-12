"""
Question 29:
Convert tuple into list and modify it.
"""


def convert_and_modify_tuple() -> None:
    """
    Convert tuple into list and modify the data.
    """

    student_names = ("Rajwansh", "Ajay", "Rishu")

    # Convert the tuple into a list because tuples cannot be modified.
    student_names_list = list(student_names)

    student_names_list.append("Akash")

    print(student_names_list)


def main() -> None:
    """Program entry point."""

    convert_and_modify_tuple()


if __name__ == "__main__":
    main()
