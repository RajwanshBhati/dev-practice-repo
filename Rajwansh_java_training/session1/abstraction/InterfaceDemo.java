package session1.abstraction;


// I created an interface named VehicleRules which contains two abstract methods horn and stop. These methods do not have any implementation and any class that implements this interface will be required to provide the implementation for these methods
interface VehicleRules {

    void horn();
    void stop();
}

// I created a child class implementing the interface VehicleRules and providing the implementation of horn and stop methods which are declared as abstract in VehicleRules interface
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

        System.out.println("INTERFACE EXPLANATION ");

        // Here I am calling the horn and stop methods which are implemented in Car class
        car.horn();
        car.stop();
    }
}