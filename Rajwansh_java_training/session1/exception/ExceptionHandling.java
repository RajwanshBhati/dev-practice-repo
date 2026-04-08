package session1.exception;

import java.util.Scanner;

public class ExceptionHandling {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter a number: ");
            int num = sc.nextInt();
            int result = 100 / num;           // may throw ArithmeticException
            System.out.println("Result = " + result);

            String str = null;
            System.out.println(str.length()); // will throw NullPointerException

        } catch (ArithmeticException e) {
            System.out.println("Cannot divide by zero! " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Null value found! " + e.getMessage());
        } catch (Exception e) {               // catch-all for any other error
            System.out.println("Some other error: " + e.getMessage());
        } finally {
            System.out.println("Finally block always runs - cleanup done");
            sc.close();
        }
    }
}