package session1.oop;

// Inheritance GraduateStudent extends Student means that GraduateStudent is a subclass of Student and inherits all its properties and behaviors.
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

    // Polymorphism Method Overloading in child class same method name with different parameters
    public void show(String message, int year) {
        System.out.println(message + " | Year: " + year + " | Student: " + getName());
    }
}