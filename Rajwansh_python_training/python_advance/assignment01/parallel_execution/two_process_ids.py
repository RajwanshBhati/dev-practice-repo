"""
Question 5:
Write a program to create two processes that print their Process IDs.
"""

import multiprocessing
import os


def print_process_id(process_name: str) -> None:
    """
    Print current process ID.

    os.getpid() is used to get the ID of the running process.
    """

    print(f"{process_name} ID: {os.getpid()}")


def main() -> None:
    """
    Create two separate processes.

    multiprocessing.Process is used because each process runs independently.
    """

    first_process = multiprocessing.Process(
        target=print_process_id,
        args=("Process 1",),
    )

    second_process = multiprocessing.Process(
        target=print_process_id,
        args=("Process 2",),
    )

    first_process.start()
    second_process.start()

    first_process.join()
    second_process.join()

    print(f"Main Process ID: {os.getpid()}")


if __name__ == "__main__":
    main()
