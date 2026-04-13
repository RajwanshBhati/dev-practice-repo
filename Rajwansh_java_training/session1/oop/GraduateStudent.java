package session1.oop;

// I created a GraduateStudent class that extends the Student class, which means it inherits all the properties and methods of the Student class. The GraduateStudent class has an additional property specialization and overrides the displayInfo method to include the specialization information when displaying the student's details. It also includes a method show that demonstrates method overloading by accepting different parameters than the displayInfo method.
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

    // Here I demonstrate Method Overloading achieved in child class with same method name but different parameters
    public void show(String message, int year) {
        System.out.println(message + " | Year: " + year + " | Student: " + getName());
    }
}