package session1.abstraction;


abstract class Vehicle {

    // abstract method here with no implementation
    abstract void start();

    // normal method with implementation
    void fuelInfo() {
        System.out.println("Fuel: Petrol / Diesel / Electric");
    }
}

// Child class implementing abstract method
class Bike extends Vehicle {

    @Override
    void start() {
        System.out.println("Bike starts with self start or kick start");
    }
}

public class AbstractClass {

    public static void main(String[] args) {

        Bike bike = new Bike();

        System.out.println("=== ABSTRACT CLASS EXPLANATION ===");

        bike.start();
        bike.fuelInfo();
    }
}