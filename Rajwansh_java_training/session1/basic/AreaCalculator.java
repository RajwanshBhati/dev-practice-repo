import java.util.Scanner;

class Shape {

    // Find the Area of a Circle
    public double areaCircle(double radius) {
        return Math.PI * radius * radius;
    }

    // Find the Area of a Rectangle
    public double areaRectangle(double length, double width) {
        return length * width;
    }

    // Find the Area of a Triangle (Base & Height)
    public double areaTriangleBH(double base, double height) {
        return 0.5 * base * height;
    }

    // Find the Area of a Triangle (Heron's Formula) because Base & Height is not always available, but sides are always available
    public double areaTriangleHeron(double a, double b, double c) {
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    // Apply Triangle validation before calculating area using Heron's formula
    public boolean isValidTriangle(double a, double b, double c) {
        return (a + b > c && a + c > b && b + c > a);
    }
}

public class AreaCalculator {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Shape shape = new Shape();

        System.out.println("Choose Shape:");
        System.out.println("1. Circle");
        System.out.println("2. Rectangle");
        System.out.println("3. Triangle");

        int choice = sc.nextInt();

        switch (choice) {

            case 1:
                System.out.print("Enter radius: ");
                double radius = sc.nextDouble();

                if (radius > 0) {
                    System.out.println("Area of Circle: " + shape.areaCircle(radius));
                } else {
                    System.out.println("Invalid radius!");
                }
                break;

            case 2:
                System.out.print("Enter length: ");
                double length = sc.nextDouble();

                System.out.print("Enter width: ");
                double width = sc.nextDouble();

                if (length > 0 && width > 0) {
                    System.out.println("Area of Rectangle: " + shape.areaRectangle(length, width));
                } else {
                    System.out.println("Invalid dimensions!");
                }
                break;

            case 3:
                System.out.println("Choose Triangle Method:");
                System.out.println("1. Base & Height");
                System.out.println("2. Heron's Formula");

                int tChoice = sc.nextInt();

                switch (tChoice) {

                    case 1:
                        System.out.print("Enter base: ");
                        double base = sc.nextDouble();

                        System.out.print("Enter height: ");
                        double height = sc.nextDouble();

                        if (base > 0 && height > 0) {
                            System.out.println("Area of Triangle (Base-Height): " + 
                                shape.areaTriangleBH(base, height));
                        } else {
                            System.out.println("Invalid input!");
                        }
                        break;

                    case 2:
                        System.out.print("Enter side a: ");
                        double a = sc.nextDouble();

                        System.out.print("Enter side b: ");
                        double b = sc.nextDouble();

                        System.out.print("Enter side c: ");
                        double c = sc.nextDouble();

                        if (shape.isValidTriangle(a, b, c)) {
                            System.out.println("Area of Triangle (Heron): " + 
                                shape.areaTriangleHeron(a, b, c));
                        } else {
                            System.out.println("Invalid triangle sides!");
                        }
                        break;

                    default:
                        System.out.println("Invalid triangle choice!");
                }
                break;

            default:
                System.out.println("Invalid choice!");
        }

        sc.close();
    }
}