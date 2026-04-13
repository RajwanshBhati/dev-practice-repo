package session1.basic;

import java.util.Scanner;

public class FibonacciProgram {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of terms: ");
        int n = sc.nextInt();

        //  Edge case if n is zero or negative so I printed a message and return
        if (n <= 0) {
            System.out.println("Please enter a positive number");
            return;
        }

        int a = 0, b = 1;

        System.out.println("Fibonacci Series:");

        for (int i = 1; i <= n; i++) {
            System.out.print(a + " ");

            int next = a + b;
            a = b;
            b = next;
        }

        sc.close(); // I closed the scanner to prevent resource leaks because it's a good practice to close resources when they are no longer needed
    }
}