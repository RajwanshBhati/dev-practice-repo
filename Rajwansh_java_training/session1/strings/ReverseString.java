package session1.strings;

import java.util.Scanner;

public class ReverseString {

    public static String reverse(String str) {
    // I used StringBuilder to efficiently build the reversed string. I iterate through the original string from the end to the beginning, appending each character to the StringBuilder. and Lastly, I convert it back to a String and return it.
    StringBuilder rev = new StringBuilder();

      for (int i = str.length() - 1; i >= 0; i--) {
        rev.append(str.charAt(i));
      }

    return rev.toString();
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter string: ");
        String str = sc.nextLine();

        System.out.println("Reversed String: " + reverse(str));

        sc.close();
    }
}