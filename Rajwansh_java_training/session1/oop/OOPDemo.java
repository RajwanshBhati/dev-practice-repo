package session1.oop;

public class OOPDemo {

    public static void main(String[] args) {

        // Parent class object is created using the Student class constructor to initialize the student details.
        Student s1 = new Student("Rahul", 101, 85.5);

        System.out.println("=== Student Details ===");
        s1.displayInfo();
        s1.show();
        s1.show("Hello");

        System.out.println("\n======================\n");

        // Child class object is created using the GraduateStudent class constructor to initialize the graduate student details.
        GraduateStudent gs1 = new GraduateStudent("Amit", 201, 90.0, "Computer Science");

        System.out.println("=== Graduate Student Details ===");
        gs1.displayInfo();
        gs1.show("Graduate Student Info", 2025);
    }
}