package session1.advanced;


abstract class Vehicle {

    // abstract method (no implementation)
    abstract void start();

    // normal method
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

        System.out.println("=== ABSTRACT CLASS DEMO ===");

        bike.start();
        bike.fuelInfo();
    }
}