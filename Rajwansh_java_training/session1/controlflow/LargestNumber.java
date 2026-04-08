package session1.controlflow;

import java.util.Scanner;

public class LargestNumber {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter first number: ");
        int a = sc.nextInt();

        System.out.print("Enter second number: ");
        int b = sc.nextInt();

        System.out.print("Enter third number: ");
        int c = sc.nextInt();

        int largest;

        //  I used if-else statements to compare the three numbers and find the largest among them
        if (a >= b && a >= c) {
            largest = a;
        } else if (b >= a && b >= c) {
            largest = b;
        } else {
            largest = c;
        }

        System.out.println("Largest number is: " + largest);

       // Closing the scanner to prevent resource leak
        sc.close();
    }
}