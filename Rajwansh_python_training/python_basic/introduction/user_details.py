"""
Question 3:
Take user input (name and age) and print a formatted message.'..
"""


# input() is used to take input from the user
name = input("Enter your name: ")

# int() converts the input string into an integer
age = int(input("Enter your age: "))

# f-string is used to insert variable values into a string
print(f"Hello {name}! You are {age} years old.")
