package session1.controlflow;

import java.util.Scanner;

public class SumEvenNumbers {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter range: ");
        int n = sc.nextInt();

        int sum = 0;

        // I used a for loop to iterate through the even numbers from 2 to n and added them to the sum variable
        for (int i = 2; i <= n; i += 2) {
            sum += i;
        }

        System.out.println("Sum = " + sum);
    }
}