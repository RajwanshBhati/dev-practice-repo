package session1.controlflow;

public class SumEvenNumbers {

    public static void main(String[] args) {

        int sum = 0;
        int i = 1;

       // I used while loop to iterate from 1 to 10 and check if the number is even. If it is even, I add it to the sum variable. The loop continues until i exceeds 10.
        while (i <= 10) {

            if (i % 2 == 0) {
                sum = sum + i;
            }

            i++;
        }

        System.out.println("Sum of even numbers from 1 to 10 is: " + sum);
    }
}