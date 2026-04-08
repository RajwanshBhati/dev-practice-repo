package session1.string;

import java.util.Scanner;

public class VowelCount {

    public static int countVowels(String str) {

        int count = 0;
        str = str.toLowerCase();

        for (int i = 0; i < str.length(); i++) {

            char ch = str.charAt(i);
           // I used if condition to check if the character is a vowel (a, e, i, o, u) and increment the count variable if it is.
            if (ch == 'a' || ch == 'e' || ch == 'i' ||
                ch == 'o' || ch == 'u') {
                count++;
            }
        }

        return count;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter string: ");
        String str = sc.nextLine();

        System.out.println("Vowel Count: " + countVowels(str));

        sc.close();
    }
}