import java.util.ArrayList;

/**
 * The Car class contains all the logic used by the self-driving car expert system.
 * This class is used by the Main class
 *
 * @author  Jacob Byerine
 * @version 1.0
 * @since   2021-09-08
 */
public class Car {
    private Coordinate currentCoords;
    private String direction;
    private Grid memoryGrid;
    private Grid gameGrid;
    private boolean winningLocationFound = false;
    private Coordinate winningLocationCoord;
    private int moveCounter = 0;
    private int rotationCount = 0;
    private ArrayList<Coordinate> moveHistory = new ArrayList<>();

    public Car(Coordinate startingCoords, String startingDirection, int gridSize, Grid gameGrid) {
        currentCoords = startingCoords;
        moveHistory.add(startingCoords);
        direction = startingDirection;
        memoryGrid = new Grid(gridSize, startingCoords);
        // Fill memory grid with 7's - unknown
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                memoryGrid.gridArray[x][y] = 7;
            }
        }
        // Make the car's current location a "2"
        memoryGrid.gridArray[startingCoords.x][startingCoords.y] = 2;
        this.gameGrid = gameGrid;
    }

    /**
     * This public method contains the expert system logic of calling the steps to find the winning case.
     */
    public void runCourse() {
        for (int i = 0; i < Math.pow(memoryGrid.size, 3); i++) {
            // Check surroundings
            checkCurrentView(gameGrid);
            // Determine if the car can move at all.
            ArrayList<Coordinate> possibleMoves = checkPossibleMoves();
            // If no possible moves, move car in circle and try again
            if (possibleMoves.isEmpty()) {
                if (rotationCount < 4) {
                    rotationCount++;
                    rotateClockwise();
                } else {
                    memoryGrid.printCurrentGrid();
                    System.out.print("Course is unsolvable, Moves taken: " + moveCounter);
                    System.exit(1);
                }
            }
            // Otherwise, move to position 0 and try again
            else {
                if (winningLocationFound) {
                    ArrayList<Coordinate> winningPath = determineStepsToGetToWinningPosition();
                    for (Coordinate c : winningPath) {
                        move(c);
                    }
                    memoryGrid.printCurrentGrid();
                    System.out.println("Course solved, Moves taken: " + moveCounter);
                    System.exit(1);
                }
                rotationCount = 0;
                move(possibleMoves.get(0));
            }
        }
    }

    /**
     * This method moves the car to the given location on the grid
     * @param newCoords the coordinates to move to.
     */
    private void move(Coordinate newCoords) {
        moveHistory.add(newCoords);
        memoryGrid.gridArray[currentCoords.x][currentCoords.y] = 3;
        memoryGrid.gridArray[newCoords.x][newCoords.y] = 2;
        currentCoords = newCoords;
        moveCounter++;
    }

    /**
     * This method rotates the car clockwise by 90deg. (N,S,E,W)
     */
    private void rotateClockwise() {
        switch (direction) {
            case "north" -> direction = "east";
            case "east" -> direction = "south";
            case "south" -> direction = "west";
            case "west" -> direction = "north";
        }
        moveCounter++;
    }

    /**
     * This method determines surrounding positions contents by scanning the position in front of the car
     * as well as the positions +- 45deg of the car.
     */
    private void checkCurrentView(Grid gameGrid) {
        switch (direction) {
            case "north" -> {
                // North-East
                northEast:
                for (int i = 1; i < gameGrid.size * 2; i++) {
                    try {
                        if (memoryGrid.gridArray[currentCoords.x + i][currentCoords.y - i] != 3) {
                            switch (gameGrid.gridArray[currentCoords.x + i][currentCoords.y - i]) {
                                case 0:
                                    memoryGrid.gridArray[currentCoords.x + i][currentCoords.y - i] = 0;
                                    break;
                                case 1:
                                    memoryGrid.gridArray[currentCoords.x + i][currentCoords.y - i] = 1;
                                    break northEast;
                                case 9:
                                    memoryGrid.gridArray[currentCoords.x + i][currentCoords.y - i] = 9;
                                    winningLocationCoord = new Coordinate(currentCoords.x + i, currentCoords.y - i);
                                    winningLocationFound = true;
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
                // North
                north:
                for (int i = 1; i < gameGrid.size * 2; i++) {
                    try {
                        if (memoryGrid.gridArray[currentCoords.x + i][currentCoords.y] != 3) {
                            switch (gameGrid.gridArray[currentCoords.x + i][currentCoords.y]) {
                                case 0:
                                    memoryGrid.gridArray[currentCoords.x + i][currentCoords.y] = 0;
                                    break;
                                case 1:
                                    memoryGrid.gridArray[currentCoords.x + i][currentCoords.y] = 1;
                                    break north;
                                case 9:
                                    memoryGrid.gridArray[currentCoords.x + i][currentCoords.y] = 9;
                                    winningLocationCoord = new Coordinate(currentCoords.x + i, currentCoords.y);
                                    winningLocationFound = true;
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
                // North-West
                northWest:
                for (int i = 1; i < gameGrid.size * 2; i++) {
                    try {
                        if (memoryGrid.gridArray[currentCoords.x + i][currentCoords.y + i] != 3) {
                            switch (gameGrid.gridArray[currentCoords.x + i][currentCoords.y + i]) {
                                case 0:
                                    memoryGrid.gridArray[currentCoords.x + i][currentCoords.y + i] = 0;
                                    break;
                                case 1:
                                    memoryGrid.gridArray[currentCoords.x + i][currentCoords.y + i] = 1;
                                    break northWest;
                                case 9:
                                    memoryGrid.gridArray[currentCoords.x + i][currentCoords.y + i] = 9;
                                    winningLocationCoord = new Coordinate(currentCoords.x + i, currentCoords.y + i);
                                    winningLocationFound = true;
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
            }
            case "east" -> {
                // North-East
                northEast:
                for (int i = 1; i < gameGrid.size * 2; i++) {
                    try {
                        if (memoryGrid.gridArray[currentCoords.x + i][currentCoords.y - i] != 3) {
                            switch (gameGrid.gridArray[currentCoords.x + i][currentCoords.y - i]) {
                                case 0:
                                    memoryGrid.gridArray[currentCoords.x + i][currentCoords.y - i] = 0;
                                    break;
                                case 1:
                                    memoryGrid.gridArray[currentCoords.x + i][currentCoords.y - i] = 1;
                                    break northEast;
                                case 9:
                                    memoryGrid.gridArray[currentCoords.x + i][currentCoords.y - i] = 9;
                                    winningLocationCoord = new Coordinate(currentCoords.x + i, currentCoords.y - i);
                                    winningLocationFound = true;
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
                // East
                east:
                for (int i = 1; i < gameGrid.size * 2; i++) {
                    try {
                        if (memoryGrid.gridArray[currentCoords.x][currentCoords.y - i] != 3) {
                            switch (gameGrid.gridArray[currentCoords.x][currentCoords.y - i]) {
                                case 0:
                                    memoryGrid.gridArray[currentCoords.x][currentCoords.y - i] = 0;
                                    break;
                                case 1:
                                    memoryGrid.gridArray[currentCoords.x][currentCoords.y - i] = 1;
                                    break east;
                                case 9:
                                    memoryGrid.gridArray[currentCoords.x][currentCoords.y - i] = 9;
                                    winningLocationCoord = new Coordinate(currentCoords.x, currentCoords.y - i);
                                    winningLocationFound = true;
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
                // South-East
                southEast:
                for (int i = 1; i < gameGrid.size * 2; i++) {
                    try {
                        if (memoryGrid.gridArray[currentCoords.x - i][currentCoords.y - i] != 3) {
                            switch (gameGrid.gridArray[currentCoords.x - i][currentCoords.y - i]) {
                                case 0:
                                    memoryGrid.gridArray[currentCoords.x - i][currentCoords.y - i] = 0;
                                    break;
                                case 1:
                                    memoryGrid.gridArray[currentCoords.x - i][currentCoords.y - i] = 1;
                                    break southEast;
                                case 9:
                                    memoryGrid.gridArray[currentCoords.x - i][currentCoords.y - i] = 9;
                                    winningLocationCoord = new Coordinate(currentCoords.x - i, currentCoords.y - i);
                                    winningLocationFound = true;
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
            }
            case "south" -> {
                // South-West
                southWest:
                for (int i = 1; i < gameGrid.size * 2; i++) {
                    try {
                        if (memoryGrid.gridArray[currentCoords.x - i][currentCoords.y + i] != 3) {
                            switch (gameGrid.gridArray[currentCoords.x - i][currentCoords.y + i]) {
                                case 0:
                                    memoryGrid.gridArray[currentCoords.x - i][currentCoords.y + i] = 0;
                                    break;
                                case 1:
                                    memoryGrid.gridArray[currentCoords.x - i][currentCoords.y + i] = 1;
                                    break southWest;
                                case 9:
                                    memoryGrid.gridArray[currentCoords.x - i][currentCoords.y + i] = 9;
                                    winningLocationCoord = new Coordinate(currentCoords.x - i, currentCoords.y + i);
                                    winningLocationFound = true;
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
                // South
                south:
                for (int i = 1; i < gameGrid.size * 2; i++) {
                    try {
                        if (memoryGrid.gridArray[currentCoords.x - i][currentCoords.y] != 3) {
                            switch (gameGrid.gridArray[currentCoords.x - i][currentCoords.y]) {
                                case 0:
                                    memoryGrid.gridArray[currentCoords.x - i][currentCoords.y] = 0;
                                    break;
                                case 1:
                                    memoryGrid.gridArray[currentCoords.x - i][currentCoords.y] = 1;
                                    break south;
                                case 9:
                                    memoryGrid.gridArray[currentCoords.x - i][currentCoords.y] = 9;
                                    winningLocationCoord = new Coordinate(currentCoords.x - i, currentCoords.y);
                                    winningLocationFound = true;
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
                // South-East
                southEast:
                for (int i = 1; i < gameGrid.size * 2; i++) {
                    try {
                        if (memoryGrid.gridArray[currentCoords.x - i][currentCoords.y - i] != 3) {
                            switch (gameGrid.gridArray[currentCoords.x - i][currentCoords.y - i]) {
                                case 0:
                                    memoryGrid.gridArray[currentCoords.x - i][currentCoords.y - i] = 0;
                                    break;
                                case 1:
                                    memoryGrid.gridArray[currentCoords.x - i][currentCoords.y - i] = 1;
                                    break southEast;
                                case 9:
                                    memoryGrid.gridArray[currentCoords.x - i][currentCoords.y - i] = 9;
                                    winningLocationCoord = new Coordinate(currentCoords.x - i, currentCoords.y - i);
                                    winningLocationFound = true;
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
            }
            case "west" -> {
                // North-West
                northWest:
                for (int i = 1; i < gameGrid.size * 2; i++) {
                    try {
                        if (memoryGrid.gridArray[currentCoords.x + i][currentCoords.y + i] != 3) {
                            switch (gameGrid.gridArray[currentCoords.x + i][currentCoords.y + i]) {
                                case 0:
                                    memoryGrid.gridArray[currentCoords.x + i][currentCoords.y + i] = 0;
                                    break;
                                case 1:
                                    memoryGrid.gridArray[currentCoords.x + i][currentCoords.y + i] = 1;
                                    break northWest;
                                case 9:
                                    memoryGrid.gridArray[currentCoords.x + i][currentCoords.y + i] = 9;
                                    winningLocationCoord = new Coordinate(currentCoords.x + i, currentCoords.y + i);
                                    winningLocationFound = true;
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
                // West
                west:
                for (int i = 1; i < gameGrid.size * 2; i++) {
                    try {
                        if (memoryGrid.gridArray[currentCoords.x][currentCoords.y + i] != 3) {
                            switch (gameGrid.gridArray[currentCoords.x][currentCoords.y + i]) {
                                case 0:
                                    memoryGrid.gridArray[currentCoords.x][currentCoords.y + i] = 0;
                                    break;
                                case 1:
                                    memoryGrid.gridArray[currentCoords.x][currentCoords.y + i] = 1;
                                    break west;
                                case 9:
                                    memoryGrid.gridArray[currentCoords.x][currentCoords.y + i] = 9;
                                    winningLocationCoord = new Coordinate(currentCoords.x, currentCoords.y + i);
                                    winningLocationFound = true;
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
                // South-West
                southWest:
                for (int i = 1; i < gameGrid.size * 2; i++) {
                    try {
                        if (memoryGrid.gridArray[currentCoords.x - i][currentCoords.y + i] != 3) {
                            switch (gameGrid.gridArray[currentCoords.x - i][currentCoords.y + i]) {
                                case 0:
                                    memoryGrid.gridArray[currentCoords.x - i][currentCoords.y + i] = 0;
                                    break;
                                case 1:
                                    memoryGrid.gridArray[currentCoords.x - i][currentCoords.y + i] = 1;
                                    break southWest;
                                case 9:
                                    memoryGrid.gridArray[currentCoords.x - i][currentCoords.y + i] = 9;
                                    winningLocationCoord = new Coordinate(currentCoords.x - i, currentCoords.y + i);
                                    winningLocationFound = true;
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * This method determines possible moves by looking to see what is contained in surrounding
     * position contents, after they have been scanned by checkCurrentView()
     * @return ArrayList<Coordinate> containing possible moves.
     */
    private ArrayList<Coordinate> checkPossibleMoves() {
        // Given current location and memory, determine which paths the car can do down.
        ArrayList<Coordinate> possibleMoveList = new ArrayList<>();
        switch (direction) {
            case "north" -> {
                if (currentCoords.x + 1 < memoryGrid.size && currentCoords.y - 1 >= 0) {
                    if (memoryGrid.gridArray[currentCoords.x + 1][currentCoords.y - 1] == 0) {
                        possibleMoveList.add(new Coordinate(currentCoords.x + 1, currentCoords.y - 1));
                    }
                }
                if (currentCoords.x + 1 < memoryGrid.size) {
                    if (memoryGrid.gridArray[currentCoords.x + 1][currentCoords.y] == 0) {
                        possibleMoveList.add(new Coordinate(currentCoords.x + 1, currentCoords.y));
                    }
                }
                if (currentCoords.x + 1 < memoryGrid.size && currentCoords.y + 1 < memoryGrid.size) {
                    if (memoryGrid.gridArray[currentCoords.x + 1][currentCoords.y + 1] == 0) {
                        possibleMoveList.add(new Coordinate(currentCoords.x + 1, currentCoords.y + 1));
                    }
                }
                if (possibleMoveList.isEmpty()) {
                    // If no possible moves, move car in circle and try again
                    if (rotationCount < 4) {
                        rotationCount++;
                        rotateClockwise();
                    }
                    {
                        if (moveHistory.size() > 1) {
                            possibleMoveList.add(moveHistory.get(moveHistory.size() - 2));
                            if (moveHistory.get(moveHistory.size() - 1) == moveHistory.get(moveHistory.size() - 2)) {
                                moveHistory.remove(moveHistory.size() - 2);
                            }
                            moveHistory.remove(moveHistory.size() - 1);
                        } else {
                            memoryGrid.printCurrentGrid();
                            System.out.print("Course is unsolvable, Moves taken: " + moveCounter);
                            System.exit(1);
                        }
                    }
                } else {
                    rotationCount = 0;
                }
            }
            case "east" -> {
                if (currentCoords.x + 1 < memoryGrid.size && currentCoords.y - 1 >= 0) {
                    if (memoryGrid.gridArray[currentCoords.x + 1][currentCoords.y - 1] == 0) {
                        possibleMoveList.add(new Coordinate(currentCoords.x + 1, currentCoords.y - 1));
                    }
                }
                if (currentCoords.y - 1 >= 0) {
                    if (memoryGrid.gridArray[currentCoords.x][currentCoords.y - 1] == 0) {
                        possibleMoveList.add(new Coordinate(currentCoords.x, currentCoords.y - 1));
                    }
                }
                if (currentCoords.x - 1 >= 0 && currentCoords.y - 1 >= 0) {
                    if (memoryGrid.gridArray[currentCoords.x - 1][currentCoords.y - 1] == 0) {
                        possibleMoveList.add(new Coordinate(currentCoords.x - 1, currentCoords.y - 1));
                    }
                }
                if (possibleMoveList.isEmpty()) {
                    // If no possible moves, move car in circle and try again
                    if (rotationCount < 4) {
                        rotationCount++;
                        rotateClockwise();
                    }
                    {
                        if (moveHistory.size() > 1) {
                            possibleMoveList.add(moveHistory.get(moveHistory.size() - 2));
                            if (moveHistory.get(moveHistory.size() - 1) == moveHistory.get(moveHistory.size() - 2)) {
                                moveHistory.remove(moveHistory.size() - 2);
                            }
                            moveHistory.remove(moveHistory.size() - 1);
                        } else {
                            memoryGrid.printCurrentGrid();
                            System.out.print("Course is unsolvable, Moves taken: " + moveCounter);
                            System.exit(1);
                        }
                    }
                } else {
                    rotationCount = 0;
                }
            }
            case "south" -> {
                if (currentCoords.x - 1 >= 0 && currentCoords.y + 1 < memoryGrid.size) {
                    if (memoryGrid.gridArray[currentCoords.x - 1][currentCoords.y + 1] == 0) {
                        possibleMoveList.add(new Coordinate(currentCoords.x - 1, currentCoords.y + 1));
                    }
                }
                if (currentCoords.x - 1 >= 0) {
                    if (memoryGrid.gridArray[currentCoords.x - 1][currentCoords.y] == 0) {
                        possibleMoveList.add(new Coordinate(currentCoords.x - 1, currentCoords.y));
                    }
                }
                if (currentCoords.x - 1 >= 0 && currentCoords.y - 1 >= 0) {
                    if (memoryGrid.gridArray[currentCoords.x - 1][currentCoords.y - 1] == 0) {
                        possibleMoveList.add(new Coordinate(currentCoords.x - 1, currentCoords.y - 1));
                    }
                }
                if (possibleMoveList.isEmpty()) {
                    // If no possible moves, move car in circle and try again
                    if (rotationCount < 4) {
                        rotationCount++;
                        rotateClockwise();
                    } else {
                        if (moveHistory.size() > 1) {
                            possibleMoveList.add(moveHistory.get(moveHistory.size() - 2));
                            if (moveHistory.get(moveHistory.size() - 1) == moveHistory.get(moveHistory.size() - 2)) {
                                moveHistory.remove(moveHistory.size() - 2);
                            }
                            moveHistory.remove(moveHistory.size() - 1);
                        } else {
                            memoryGrid.printCurrentGrid();
                            System.out.print("Course is unsolvable, Moves taken: " + moveCounter);
                            System.exit(1);
                        }
                    }
                } else {
                    rotationCount = 0;
                }
            }
            case "west" -> {
                if (currentCoords.x + 1 < memoryGrid.size && currentCoords.y + 1 < memoryGrid.size) {
                    if (memoryGrid.gridArray[currentCoords.x + 1][currentCoords.y + 1] == 0) {
                        possibleMoveList.add(new Coordinate(currentCoords.x + 1, currentCoords.y + 1));
                    }
                }
                if (currentCoords.y + 1 < memoryGrid.size) {
                    if (memoryGrid.gridArray[currentCoords.x][currentCoords.y + 1] == 0) {
                        possibleMoveList.add(new Coordinate(currentCoords.x, currentCoords.y + 1));
                    }
                }
                if (currentCoords.x - 1 >= 0 && currentCoords.y + 1 < memoryGrid.size) {
                    if (memoryGrid.gridArray[currentCoords.x - 1][currentCoords.y + 1] == 0) {
                        possibleMoveList.add(new Coordinate(currentCoords.x - 1, currentCoords.y + 1));
                    }
                }
                if (possibleMoveList.isEmpty()) {
                    // If no possible moves, move car in circle and try again
                    if (rotationCount < 4) {
                        rotationCount++;
                        rotateClockwise();
                    }
                    {
                        if (moveHistory.size() > 1) {
                            possibleMoveList.add(moveHistory.get(moveHistory.size() - 2));
                            if (moveHistory.get(moveHistory.size() - 1) == moveHistory.get(moveHistory.size() - 2)) {
                                moveHistory.remove(moveHistory.size() - 2);
                            }
                            moveHistory.remove(moveHistory.size() - 1);
                        } else {
                            memoryGrid.printCurrentGrid();
                            System.out.print("Course is unsolvable, Moves taken: " + moveCounter);
                            System.exit(1);
                        }
                    }
                } else {
                    rotationCount = 0;
                }
            }
        }
        return possibleMoveList;
    }

    /**
     * This method determines the steps to get to the winning location once it is found.
     * @return ArrayList<Coordinate> containing necessary moves.
     */
    private ArrayList<Coordinate> determineStepsToGetToWinningPosition() {
        int numOfXSteps = -1 * (currentCoords.x - winningLocationCoord.x);
        int numOfYSteps = -1 * (currentCoords.y - winningLocationCoord.y);

        ArrayList<Coordinate> stepList = new ArrayList<>();
        int steps;
        // neg neg
        if (numOfXSteps <= 0 && numOfYSteps <= 0) {
            // Find larger of 2
            if (Math.abs(numOfXSteps) >= Math.abs(numOfYSteps)) {
                steps = Math.abs(numOfXSteps);
            } else {
                steps = Math.abs(numOfYSteps);
            }
            // Create step number of coordinates
            for (int i = 0; i < steps; i++) {
                stepList.add(new Coordinate());
            }
            // Create step number of coordinates
            for (int i = 0; i < stepList.size(); i++) {
                stepList.get(i).x = currentCoords.x - i;
                stepList.get(i).y = currentCoords.y - i;
            }
        }
        // neg pos
        else if (numOfXSteps <= 0 && numOfYSteps >= 0) {
            // Find larger of 2
            if (Math.abs(numOfXSteps) >= numOfYSteps) {
                steps = Math.abs(numOfXSteps);
            } else {
                steps = numOfYSteps;
            }
            // Create step number of coordinates
            for (int i = 0; i < steps; i++) {
                stepList.add(new Coordinate());
            }
            // Create step number of coordinates
            for (int i = 0; i < stepList.size(); i++) {
                stepList.get(i).x = currentCoords.x - i;
                stepList.get(i).y = currentCoords.y + i;
            }

        }
        // pos neg
        else if (numOfXSteps >= 0 && numOfYSteps <= 0) {
            // Find larger of 2
            if (numOfXSteps >= Math.abs(numOfYSteps)) {
                steps = numOfXSteps;
            } else {
                steps = numOfYSteps;
            }
            // Create step number of coordinates
            for (int i = 0; i < steps; i++) {
                stepList.add(new Coordinate());
            }
            // Create step number of coordinates
            for (int i = 0; i < stepList.size(); i++) {
                stepList.get(i).x = currentCoords.x + i;
                stepList.get(i).y = currentCoords.y - i;
            }
        }
        // pos pos
        else if (numOfXSteps >= 0 && numOfYSteps >= 0) {
            // Find larger of 2
            if (numOfXSteps >= numOfYSteps) {
                steps = numOfXSteps;
            } else {
                steps = numOfYSteps;
            }
            // Create step number of coordinates
            for (int i = 0; i < steps; i++) {
                stepList.add(new Coordinate());
            }
            // Create step number of coordinates
            for (int i = 0; i < stepList.size(); i++) {
                stepList.get(i).x = currentCoords.x + i;
                stepList.get(i).y = currentCoords.y + i;
            }
        }
        return stepList;
    }
}
