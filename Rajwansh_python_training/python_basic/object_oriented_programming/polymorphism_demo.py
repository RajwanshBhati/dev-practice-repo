"""
Question 44:
Demonstrate polymorphism using different classes with the same method name.
"""


class Dog:
    """Represent dog sound behavior."""

    def make_sound(self) -> None:
        """Display dog sound."""

        print("Dog says: Bark")


class Cat:
    """Represent cat sound behavior."""

    def make_sound(self) -> None:
        """Display cat sound."""

        print("Cat says: Meow")


class Cow:
    """Represent cow sound behavior."""

    def make_sound(self) -> None:
        """Display cow sound."""

        print("Cow says: Moo")


def main() -> None:
    """Program entry point."""

    animals = [Dog(), Cat(), Cow()]

    # Same method name behaves differently for different objects.
    for animal in animals:
        animal.make_sound()


if __name__ == "__main__":
    main()
