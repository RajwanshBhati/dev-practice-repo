package session1.datatypes;

public class DataTypeDemo {

    public static void main(String[] args) {

        //  Primitive data type Here I am declaring two integer variables a and b. When I assign the value of a to b, it creates a copy of the value. So when I change the value of b, it does not affect the value of a because they are stored in different memory locations.
        int a = 10;
        int b = a;   // value copy

        b = 20;

        System.out.println("Primitive Data Type Output:");
        System.out.println("a = " + a); // output: 10
        System.out.println("b = " + b); // output: 20


        //  Reference data type Here I am declaring two integer array variables arr1 and arr2. When I assign the value of arr1 to arr2, it creates a reference copy. So both arr1 and arr2 point to the same memory location where the array is stored. Therefore, when I change the value of an element in arr2, it also changes the value of the corresponding element in arr1 because they are referencing the same array.
        int[] arr1 = {1, 2, 3};
        int[] arr2 = arr1; // reference copy

        arr2[0] = 99;

        System.out.println("Reference Data Type Output:");
        System.out.println("arr1[0] = " + arr1[0]); // output: 99
        System.out.println("arr2[0] = " + arr2[0]); // output: 99
    }
}