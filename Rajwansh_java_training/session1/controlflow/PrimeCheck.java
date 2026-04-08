package session1.controlflow;

import java.util.Scanner;

public class PrimeCheck {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter a number: ");
        int n = sc.nextInt();

        boolean isPrime = true;

        // Edge case if numbers <= 1 are not prime
        if (n <= 1) {
            isPrime = false;
        } else {

            // I used for loop to check divisibility from 2 to n/2 because a number cannot be divisible by any number greater than its half (except itself)
            for (int i = 2; i <= n / 2; i++) {
                if (n % i == 0) {
                    isPrime = false;
                    break;
                }
            }
        }

        // if-else decision if number is prime or not
        if (isPrime) {
            System.out.println(n + " is a Prime Number");
        } else {
            System.out.println(n + " is NOT a Prime Number");
        }

        sc.close();
    }
}