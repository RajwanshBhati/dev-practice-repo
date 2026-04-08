package session1.arrays;

import java.util.Scanner;

public class ArrayAverage {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter size of array: ");
        int n = sc.nextInt();

        // I added this check to prevent creating an array of size 0 or negative, which would cause an error. If n is less than or equal to 0, we print a message and exit the program.
        if (n <= 0) {
            System.out.println("Array size must be greater than 0");
            return;
        }

        int[] arr = new int[n];
        int sum = 0;

        System.out.println("Enter array elements:");
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
            sum += arr[i];
        }

       // Double is used for average because average can be a decimal value
        double average = (double) sum / n;

        System.out.println("Average = " + average);

        sc.close();
    }
}