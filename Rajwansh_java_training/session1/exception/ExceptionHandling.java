package session1.exception;

import java.util.Scanner;

public class ExceptionHandling {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
       
       // Here I am demonstrating the use of try-catch blocks to handle exceptions that may occur during the execution of the code. The try block contains code that may throw exceptions, and the catch blocks handle specific types of exceptions such as ArithmeticException and NullPointerException. The finally block is used to execute code that should run regardless of whether an exception was thrown or caught, such as closing the scanner resource.

        try {
            System.out.print("Enter a number: ");
            int num = sc.nextInt();
            int result = 100 / num;           // may throw ArithmeticException because of division by zero
            System.out.println("Result = " + result);

            String str = null;
            System.out.println(str.length()); // will throw NullPointerException because str is null and we are trying to access its length

        } catch (ArithmeticException e) {
            System.out.println("Cannot divide by zero! " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Null value found! " + e.getMessage());
        } catch (Exception e) {               // catch-all for any other error that may occur

            System.out.println("Some other error: " + e.getMessage());
        } finally {
            System.out.println("Finally block executed."); // This block will always execute regardless of whether an exception was thrown or caught
            sc.close();
        }
    }
}