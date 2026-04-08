package session1.basic;

import java.util.Scanner;

public class FactorialProgram {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter a number: ");
        int n = sc.nextInt();

        //  Edge case if negative number
        if (n < 0) {
            System.out.println("Factorial is not defined for negative numbers");
            return;
        }

        long factorial = 1;

        for (int i = 1; i <= n; i++) {
            factorial = factorial * i;
        }

        System.out.println("Factorial of " + n + " is: " + factorial);

        sc.close();
    }
}