package session1.abstraction;


abstract class Vehicle {

    // I created abstract method here with no implementation and these methods I Implemented in child class Bike
    abstract void start();

    // normal method with implementation
    void fuelInfo() {
        System.out.println("Fuel: Petrol / Diesel / Electric");
    }
}

// Here I am creating a child class implementing the abstract method from parent class Vehicle and providing the implementation of start method which is declared as abstract in Vehicle class
class Bike extends Vehicle {

    @Override
    void start() {
        System.out.println("Bike starts with self start or kick start");
    }
}

public class AbstractClass {

    public static void main(String[] args) {

        Bike bike = new Bike();

        System.out.println("ABSTRACT CLASS EXPLANATION");

        // Here I am calling the start method which is implemented in Bike class and fuelInfo method which is inherited from Vehicle class
        bike.start();
        bike.fuelInfo();
    }
}