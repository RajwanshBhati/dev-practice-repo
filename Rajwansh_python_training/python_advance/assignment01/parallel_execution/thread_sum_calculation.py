"""
Question 2:
Create a thread that calculates the sum of numbers from 1 to 100.
"""

import threading

START_NUMBER = 1
END_NUMBER = 100


def calculate_sum() -> None:
    """
    Calculate and print sum from 1 to 100.

    This function runs inside a separate thread.
    """

    total_sum = sum(range(START_NUMBER, END_NUMBER + 1))

    print(f"Total sum: {total_sum}")


def main() -> None:
    """Create one thread for sum calculation."""

    sum_thread = threading.Thread(target=calculate_sum)

    sum_thread.start()
    sum_thread.join()

    print("Sum thread completed.")


if __name__ == "__main__":
    main()
