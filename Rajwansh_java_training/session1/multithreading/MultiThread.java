package session1.advanced;

/*
 * This program demonstrates basic multithreading in Java.
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

// Second thread class
class Task2 extends Thread {

    public void run() {

        for (int i = 1; i <= 5; i++) {
            System.out.println("Task 2 -> " + i);
        }
    }
}

public class MultiThreadDemo {

    public static void main(String[] args) {

        // creating objects of both threads
        Task1 t1 = new Task1();
        Task2 t2 = new Task2();

        // start() is used to run threads concurrently
        // it internally calls run()
        t1.start();
        t2.start();
    }
}