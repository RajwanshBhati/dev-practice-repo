package session1.controlflow;

import java.util.Scanner;

public class MultiplicationTable {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number: ");
        int n = sc.nextInt();

        System.out.println("Multiplication Table of " + n);

        //  Here I used a for loop to iterate from 1 to 10 and print the multiplication table of the given number n. The loop runs 10 times, multiplying n by the loop variable i and printing the result in a formatted way.
        for (int i = 1; i <= 10; i++) {
            System.out.println(n + " x " + i + " = " + (n * i));
        }

        sc.close();
    }
}