package session1.basic;

import java.util.Scanner;

public class FactorialProgram {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter a number: ");
        int n = sc.nextInt();

        //  Edge case if negative number I Print message and return
        if (n < 0) {
            System.out.println("Factorial is not defined for negative numbers");
            return;
        }

        long factorial = 1;
        

        // Here I am calculating factorial using a for loop, multiplying the current value of factorial with the loop variable i which runs from 1 to n
        for (int i = 1; i <= n; i++) {
            factorial = factorial * i;
        }

        System.out.println("Factorial of " + n + " is: " + factorial);

        sc.close();
    }
}