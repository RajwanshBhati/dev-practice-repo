package session1.strings;

import java.util.Scanner;

public class VowelCount {

    public static int countVowels(String str) {

          int count = 0;

        for (int i = 0; i < str.length(); i++) {

            char ch = str.charAt(i);
          // I check if the character is a vowel (both uppercase and lowercase) and increment the count variable accordingly.
            if (ch == 'a' || ch == 'e' || ch == 'i' ||
                ch == 'o' || ch == 'u' ||
                ch == 'A' || ch == 'E' || ch == 'I' ||
                ch == 'O' || ch == 'U') {
                count++;
            }
        }

        return count;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter string: ");
        String str = sc.nextLine();

        System.out.println("Vowel Count: " + countVowels(str)); // I call the countVowels method with the input string and print the result, which is the total number of vowels present in the string.

        sc.close();
    }
}