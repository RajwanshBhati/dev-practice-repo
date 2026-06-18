"""
Question 7:
Convert a normal function into parallel execution using ThreadPoolExecutor.
"""

from concurrent.futures import ThreadPoolExecutor
import time

MAX_WORKERS = 3
DELAY_SECONDS = 1
FILE_NAMES = [
    "file1.txt",
    "file2.txt",
    "file3.txt",
]


def download_file(file_name: str) -> str:
    """
    Simulate file download and return status.

    ThreadPoolExecutor is useful for I/O-bound tasks like downloads.
    """

    time.sleep(DELAY_SECONDS)

    return f"{file_name} downloaded successfully."


def main() -> None:
    """
    Execute download_file function in parallel using ThreadPoolExecutor.

    executor.map() applies the function to all values in parallel.
    """

    with ThreadPoolExecutor(max_workers=MAX_WORKERS) as executor:
        results = executor.map(download_file, FILE_NAMES)

    for result in results:
        print(result)


if __name__ == "__main__":
    main()
