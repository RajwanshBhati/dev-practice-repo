# Take three numbers as input
num1 = int(input("Enter first number: "))
num2 = int(input("Enter second number: "))
num3 = int(input("Enter third number: "))

# Compare numbers using conditional statements
if num1 >= num2 and num1 >= num3:
    print("Largest Number =", num1)
elif num2 >= num1 and num2 >= num3:
    print("Largest Number =", num2)
else:
    print("Largest Number =", num3)
