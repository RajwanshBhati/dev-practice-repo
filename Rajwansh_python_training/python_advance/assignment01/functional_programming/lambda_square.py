"""
Question 1:
Write a lambda function to find the square of a number.
"""


def print_square() -> None:
    """
    Print square of a number using lambda.

    Lambda is used because the operation is small and only
    requires a single expression.
    """

    square_function = lambda number: number ** 2

    print(square_function(5))


if __name__ == "__main__":
    print_square()
