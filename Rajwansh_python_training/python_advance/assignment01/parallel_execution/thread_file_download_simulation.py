"""
Question 4:
Create multiple threads to simulate file downloading using time.sleep().
"""

import threading
import time

DOWNLOAD_DELAY_SECONDS = 2

FILE_NAMES = [
    "report.pdf",
    "image.png",
    "video.mp4",
]


def download_file(file_name: str) -> None:
    """
    Simulate file download.

    time.sleep() is used to represent download waiting time.
    """

    print(f"Downloading started: {file_name}")
    time.sleep(DOWNLOAD_DELAY_SECONDS)
    print(f"Downloading completed: {file_name}")


def main() -> None:
    """
    Create multiple threads for file downloads.

    Multiple threads are useful here because downloading is an I/O-like task.
    """

    download_threads = []

    for file_name in FILE_NAMES:
        thread = threading.Thread(
            target=download_file,
            args=(file_name,),
        )

        download_threads.append(thread)
        thread.start()

    for thread in download_threads:
        thread.join()

    print("All files downloaded.")


if __name__ == "__main__":
    main()
