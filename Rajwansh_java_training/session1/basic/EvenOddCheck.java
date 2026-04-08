import java.util.Scanner;

class NumberCheck {

    public String checkEvenOdd(double num) {

        // Check if number is integer
        if (num % 1 != 0) {
            return "Not an integer, cannot determine even/odd";
        }

        int n = (int) num;

        if (n % 2 == 0) {
            return "Even";
        } else {
            return "Odd";
        }
    }
}

public class EvenOddCheck {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        NumberCheck obj = new NumberCheck();

        try {
            System.out.print("Enter a number: ");
            double num = sc.nextDouble();   

            String result = obj.checkEvenOdd(num);
            System.out.println("Result: " + result);

        } catch (Exception e) {
            System.out.println("Invalid input! Please enter a number.");
        }

        sc.close();
    }
}