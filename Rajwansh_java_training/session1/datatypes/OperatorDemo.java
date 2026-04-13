package session1.datatypes;

public class OperatorDemo {

    public static void main(String[] args) {

        int a = 10;
        int b = 5;

        // Arithmetic Operators Demonstration Here I am demonstrating the use of basic arithmetic operators such as addition, subtraction, multiplication, and division by performing operations on two integer variables a and b and printing the results to the console
        System.out.println("Addition: " + (a + b));
        System.out.println("Subtraction: " + (a - b));
        System.out.println("Multiplication: " + (a * b));
        System.out.println("Division: " + (a / b));

        // Relational Operators Demonstration  Here I am demonstrating the use of relational operators such as greater than, less than, and equality by comparing the values of two integer variables a and b and printing the results to the console
        System.out.println("a > b: " + (a > b));
        System.out.println("a < b: " + (a < b));
        System.out.println("a == b: " + (a == b));

        //  Logical Operators Demonstration Here I am demonstrating the use of logical operators such as AND, OR, and NOT by applying them to boolean variables x and y and printing the results to the console
        boolean x = true;
        boolean y = false;

        System.out.println("x && y: " + (x && y));
        System.out.println("x || y: " + (x || y));
        System.out.println("!x: " + (!x));
    }
}