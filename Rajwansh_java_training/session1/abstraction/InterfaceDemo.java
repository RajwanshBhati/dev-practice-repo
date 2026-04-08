package session1.advanced;

interface VehicleRules {

    void horn();
    void stop();
}

// Child class implementing interface
class Car implements VehicleRules {

    public void horn() {
        System.out.println("Car horn: Beep Beep!");
    }

    public void stop() {
        System.out.println("Car stops using brake system");
    }
}

public class InterfaceDemo {

    public static void main(String[] args) {

        Car car = new Car();

        System.out.println("=== INTERFACE DEMO ===");

        car.horn();
        car.stop();
    }
}