"""
Question 30:
Perform union, intersection, and difference on two sets.
"""


def perform_set_operations() -> None:
    """
    Display union, intersection, and difference of two sets.
    """

    first_set = {1, 2, 3, 4, 5}
    second_set = {4, 5, 6, 7, 8}

    # Union combines all unique values from both sets.
    print(f"Union: {first_set.union(second_set)}")

    # Intersection returns only common values.
    print(f"Intersection: {first_set.intersection(second_set)}")

    # Difference returns values present only in the first set.
    print(f"Difference: {first_set.difference(second_set)}")


def main() -> None:
    """Program entry point."""

    perform_set_operations()


if __name__ == "__main__":
    main()
