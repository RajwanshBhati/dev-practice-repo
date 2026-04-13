package session1.multithreading;

/*
 * I create two threads (Task1 and Task2) that run at the same time.
 * Each thread prints numbers from 1 to 5.
 */

// First thread class
class Task1 extends Thread {

    // run() method contains the code that will be executed by thread
    public void run() {

        // loop to print numbers
        for (int i = 1; i <= 5; i++) {
            System.out.println("Task 1 -> " + i);
        }
    }
}

// I create Second thread class that extends Thread and overrides the run() method to print numbers from 1 to 5, similar to Task1 but with a different message to distinguish between the two threads when they run concurrently.
class Task2 extends Thread {

    public void run() {

        for (int i = 1; i <= 5; i++) {
            System.out.println("Task 2 -> " + i);
        }
    }
}

public class MultiThread {

    public static void main(String[] args) {

        // creating objects of both threads
        Task1 t1 = new Task1();
        Task2 t2 = new Task2();

        // start() is used to run threads concurrently
        // it internally calls run() method of respective thread class
        t1.start();
        t2.start();
    }
}