package session1.datatypes;

public class DataTypeDemo {

    public static void main(String[] args) {

        //  Primitive data type
        int a = 10;
        int b = a;   // value copy

        b = 20;

        System.out.println("Primitive Data Type Output:");
        System.out.println("a = " + a); // 10
        System.out.println("b = " + b); // 20


        //  Reference data type
        int[] arr1 = {1, 2, 3};
        int[] arr2 = arr1; // reference copy

        arr2[0] = 99;

        System.out.println("\nReference Data Type Output:");
        System.out.println("arr1[0] = " + arr1[0]); // 99
        System.out.println("arr2[0] = " + arr2[0]); // 99
    }
}