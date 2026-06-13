"""
Question 7:
Write a program to check whether a number is even or odd.
"""

# input() is used to take a number from the user
num = int(input("Enter a number: "))

# % (modulus) returns the remainder after division
if num % 2 == 0:
    print("Even Number")
else:
    print("Odd Number")
