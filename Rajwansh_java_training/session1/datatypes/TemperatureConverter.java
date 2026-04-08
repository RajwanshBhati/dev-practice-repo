package session1.datatypes;

import java.util.Scanner;

public class TemperatureConverter {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);


        // I used while loop because to allow multiple conversions
        // Without while loop, program will run only once and exit
        while (true) {

            System.out.println("\n=== Temperature Converter ===");
            System.out.println("1. Celsius to Fahrenheit");
            System.out.println("2. Fahrenheit to Celsius");
            System.out.println("3. Exit");

            System.out.print("Choose option: ");
            int choice = sc.nextInt();

          // I used double for temperature because temperature can have decimal values like 36.5°C or 98.6°F
            double temp, result;

            switch (choice) {

                case 1:
                    System.out.print("Enter Celsius: ");
                    temp = sc.nextDouble();

                    result = (temp * 9 / 5) + 32;
                    System.out.println("Fahrenheit: " + result);
                    break;

                case 2:
                    System.out.print("Enter Fahrenheit: ");
                    temp = sc.nextDouble();

                    result = (temp - 32) * 5 / 9;
                    System.out.println("Celsius: " + result);
                    break;

                case 3:
                    System.out.println("Program exited successfully.");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid option. Try again!");
            }
        }
    }
}