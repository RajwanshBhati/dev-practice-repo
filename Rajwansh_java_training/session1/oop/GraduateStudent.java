package session1.oop;

// GraduateStudent class is a child class of Student class, it inherits all properties and methods of Student class
public class GraduateStudent extends Student {

    private String specialization;

    public GraduateStudent(String name, int rollNumber, double marks, String specialization) {
        super(name, rollNumber, marks);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    // Method Overriding Run-time polymorphism is achieved by overriding the displayInfo method in the GraduateStudent class.
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Specialization: " + specialization);
    }

    // Polymorphism Method Overloading achieved in child class with same method name but different parameters
    public void show(String message, int year) {
        System.out.println(message + " | Year: " + year + " | Student: " + getName());
    }
}