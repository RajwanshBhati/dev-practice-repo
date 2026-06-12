"""
Question 36:
Read a file and count words, lines, and characters.
"""

FILE_NAME = "student.txt"


def analyze_file() -> None:
    """
    Read the file and display word, line,
    and character counts.
    """

    # Read the complete file content so it can be analyzed.
    with open(FILE_NAME, "r", encoding="utf-8") as file:
        file_content = file.read()

    word_count = len(file_content.split())
    line_count = len(file_content.splitlines())
    character_count = len(file_content)

    print(f"Words: {word_count}")
    print(f"Lines: {line_count}")
    print(f"Characters: {character_count}")


def main() -> None:
    """Program entry point."""

    analyze_file()


if __name__ == "__main__":
    main()
