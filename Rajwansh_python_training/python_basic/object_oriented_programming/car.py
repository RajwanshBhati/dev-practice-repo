"""
Question 41:
Create a Car class with a constructor.
"""


class Car:
    """Store car details using a constructor."""

    def __init__(self, brand: str, model: str, year: int) -> None:
        # Constructor is used to initialize car details when the object is created.
        self.brand = brand
        self.model = model
        self.year = year

    def display_car_details(self) -> None:
        """Display car details."""

        print(f"Brand: {self.brand}")
        print(f"Model: {self.model}")
        print(f"Year: {self.year}")


def main() -> None:
    """Program entry point."""

    car = Car("Tata", "Nexon", 2024)
    car.display_car_details()


if __name__ == "__main__":
    main()
