package session1.oop;

// Encapsulation: private fields + public getters/setters
public class Student {

    private String name;
    private int rollNumber;
    private double marks;

    // Constructor to initialize student object
    public Student(String name, int rollNumber, double marks) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.marks = marks;
    }

    // Getters & Setters is used to access and modify private fields of the class. 
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(int rollNumber) {
        this.rollNumber = rollNumber;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    // Method to display student info is used to print the details of the student object. It accesses the private fields and displays them in a formatted way.
    public void displayInfo() {
        System.out.println("Student Name: " + name);
        System.out.println("Roll Number: " + rollNumber);
        System.out.println("Marks: " + marks);
    }

    // Polymorphism - Method Overloading: same method name with different parameters
    public void show() {
        System.out.println("Showing student details");
    }

    public void show(String message) {
        System.out.println(message + " - Student: " + name);
    }
}