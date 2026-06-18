"""
Question 1:
Write a program to create two threads that print numbers from 1 to 5
simultaneously.
"""

import threading
import time

START_NUMBER = 1
END_NUMBER = 5
DELAY_SECONDS = 1


def print_numbers(thread_name: str) -> None:
    """
    Print numbers from start to end.

    time.sleep() is used to clearly show both threads running
    simultaneously.
    """

    for number in range(START_NUMBER, END_NUMBER + 1):
        print(f"{thread_name}: {number}")
        time.sleep(DELAY_SECONDS)


def main() -> None:
    """
    Create and start two threads.

    threading.Thread is used to run the same function in parallel.
    """

    first_thread = threading.Thread(
        target=print_numbers,
        args=("Thread 1",),
    )

    second_thread = threading.Thread(
        target=print_numbers,
        args=("Thread 2",),
    )

    first_thread.start()
    second_thread.start()

    first_thread.join()
    second_thread.join()

    print("Both threads completed.")


if __name__ == "__main__":
    main()
