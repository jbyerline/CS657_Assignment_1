import java.util.Scanner;

/**
 * This Intelligent Systems and Control Program #1 implements an application that
 * simulates a self-driving car expert system. It takes user inputed parameters and outputs the
 * cars attempt at finding the winning position by driving around the map.
 *
 * @author  Jacob Byerine
 * @version 1.0
 * @since   2021-09-08
 */
public class Main {

    public static void main(String[] args) {
        // Try to get user input
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter Grid Dimension Ex. 10 or 40: ");
            int size = scanner.nextInt();

            System.out.println("Enter Car starting position Ex. 5,2: ");
            String sStartingPosition = scanner.next();

            System.out.println("Enter Car starting position Ex. 35,9: ");
            String sWinningPosition = scanner.next();

            System.out.println("Enter percentage of grid with obstacles: ");
            int percentageOfGridBlocked = scanner.nextInt();

            System.out.println("Enter Starting direction:\n1 - North\n2 - East\n3 - South\n4 - West");
            String carStartingDirection = "north";
            int iDirection = scanner.nextInt();
            switch (iDirection) {
                case 1 -> carStartingDirection = "north";
                case 2 -> carStartingDirection = "east";
                case 3 -> carStartingDirection = "south";
                case 4 -> carStartingDirection = "west";
            }

            Coordinate carStartingPosition = new Coordinate(Integer.parseInt(sStartingPosition.split(",")[0]), Integer.parseInt(sStartingPosition.split(",")[1]));
            Coordinate carWinningPosition = new Coordinate(Integer.parseInt(sWinningPosition.split(",")[0]), Integer.parseInt(sWinningPosition.split(",")[1]));

            if (carStartingPosition.x >= size || carStartingPosition.y >= size || carWinningPosition.x >= size || carWinningPosition.y >= size) {
                System.err.println("Starting or Winning position is out of bounds of grid.\nPlease re-run and enter valid input");
                System.exit(2);
            }

            System.out.println("\nMap Number Key: ");
            System.out.println("1 - Blockade");
            System.out.println("2 - Car's current position");
            System.out.println("3 - Block's car has visited");
            System.out.println("7 - Blocks unknown to car's memory");
            System.out.println("9 - Winning block");

            // Create new game grid
            Grid gameGrid = new Grid(size, carStartingPosition, carWinningPosition);
            gameGrid.randomizeGrid(percentageOfGridBlocked);

            // Print out current key for easier viewing
            System.out.println("Map Key:");
            gameGrid.printCurrentGrid();

            // Create new car. Give it starting coordinates and direction
            Car car = new Car(carStartingPosition, carStartingDirection, size, gameGrid);
            System.out.println("\n\n\nPlease wait... calculating\n\n\n");
            System.out.println("Car's memory on last move:");

            // Tell car to figure things out
            car.runCourse();
        }
        // Catch any errors in parsing the user input
        catch (ArrayIndexOutOfBoundsException a) {
            System.err.println("Please enter the position input as x,y where x and y are integers.\nPlease re-run and enter valid input");
            System.exit(2);
        }
    }
}
