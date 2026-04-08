package session1.string;

import java.util.Arrays;
import java.util.Scanner;

public class AnagramCheck {

    public static boolean isAnagram(String s1, String s2) {

        if (s1.length() != s2.length()) {
            return false;
        }
        
        // Convert both strings to lowercase and then to character arrays, sort the arrays, and check if they are equal. If they are equal, the strings are anagrams of each other.
        char[] arr1 = s1.toLowerCase().toCharArray();
        char[] arr2 = s2.toLowerCase().toCharArray();

        Arrays.sort(arr1);
        Arrays.sort(arr2);

        return Arrays.equals(arr1, arr2);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter first string: ");
        String s1 = sc.nextLine();

        System.out.print("Enter second string: ");
        String s2 = sc.nextLine();

        if (isAnagram(s1, s2)) {
            System.out.println("Strings are Anagrams");
        } else {
            System.out.println("Strings are NOT Anagrams");
        }

        sc.close();
    }
}