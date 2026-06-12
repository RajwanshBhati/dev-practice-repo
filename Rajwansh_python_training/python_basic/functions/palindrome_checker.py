"""
Question 18:
Write a function to check palindrome for number and string.
"""


def is_palindrome(value: str) -> bool:
    """
    Check whether the given value is a palindrome.
    Returns:
        bool: True if the value is a palindrome, otherwise False.
    """

    reversed_value = ""

    # Build the reversed value by taking characters from the end.
    for character in value:
        reversed_value = character + reversed_value

    # Compare the original value with the reversed value.
    return value == reversed_value


def main() -> None:
    """
    Take input from the user and check whether it is a palindrome.

    Returns:
        None: This function only handles input and output.
    """

    user_value = input("Enter a string or number: ")

    if is_palindrome(user_value):
        print(f"{user_value} is a Palindrome.")
    else:
        print(f"{user_value} is not a Palindrome.")


if __name__ == "__main__":
    # Run the program only when this file is executed directly.
    main()
