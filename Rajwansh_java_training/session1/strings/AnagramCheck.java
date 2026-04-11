package session1.strings;

import java.util.Scanner;

public class AnagramCheck {

    public static boolean isAnagram(String s1, String s2) {

        // I check Length before proceeding with frequency counting, as anagrams must be of the same length
        if (s1.length() != s2.length()) return false;

        // Frequency array for a-z
        int[] freq = new int[26];

        // Count characters of s1 by incrementing the corresponding index in the frequency array. I convert characters to lowercase to ensure case insensitivity.
        for (char ch : s1.toLowerCase().toCharArray()) {
            freq[ch - 'a']++;
        }

        // Subtract using s2 because anagrams will have the same characters in the same frequency, so I decrement the counts for s2. If they are anagrams, all counts should return to zero.
        for (char ch : s2.toLowerCase().toCharArray()) {
            freq[ch - 'a']--;
        }

        // Check all values are 0
        for (int count : freq) {
            if (count != 0) return false;
        }

        return true;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter first string: ");
        String s1 = sc.nextLine();

        System.out.print("Enter second string: ");
        String s2 = sc.nextLine();

        if (isAnagram(s1, s2)) {
            System.out.println("Strings are Anagrams ");
        } else {
            System.out.println("Strings are NOT Anagrams ");
        }

        sc.close();
    }
}