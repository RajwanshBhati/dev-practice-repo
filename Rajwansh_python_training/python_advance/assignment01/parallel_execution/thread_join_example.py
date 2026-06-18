"""
Question 3:
Demonstrate the use of join() method in threading.
"""

import threading
import time

DELAY_SECONDS = 3


def prepare_report() -> None:
    """
    Simulate report preparation.

    time.sleep() is used to represent a time-consuming task.
    """

    print("Report preparation started.")
    time.sleep(DELAY_SECONDS)
    print("Report preparation completed.")


def main() -> None:
    """
    Start a thread and wait for it using join().

    join() is used so the main program waits until the thread finishes.
    """

    report_thread = threading.Thread(target=prepare_report)

    report_thread.start()

    print("Main program is waiting for report thread.")

    report_thread.join()

    print("Main program continues after thread completion.")


if __name__ == "__main__":
    main()
