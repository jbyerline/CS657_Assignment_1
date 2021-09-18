import java.lang.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Grid class serves as a template for both the actual map and the "Car's Memory Map".
 * This class is used by the Main and the Car classes
 *
 * @author  Jacob Byerine
 * @version 1.0
 * @since   2021-09-08
 */
public class Grid {
    public int[][] gridArray;
    public int size;
    public Coordinate startingCoord;
    public Coordinate winningCoord;

    public Grid(int size, Coordinate startingCoord) {
        gridArray = new int[size][size];
        this.size = size;
        this.startingCoord = startingCoord;
    }

    public Grid(int size, Coordinate startingCoord, Coordinate winningCoord) {
        gridArray = new int[size][size];
        this.size = size;
        this.startingCoord = startingCoord;
        this.winningCoord = winningCoord;
    }

    /**
     * This method randomly fills the game grid with a percentage of given obstacles.
     * @param percentageBlocked the amount of spaces in the grid containing 1's by %
     */
    public void randomizeGrid(int percentageBlocked) {
        // Calculate number of spaces to block based on percentage
        int spacesToFill = (int) Math.floor((size * size) * (percentageBlocked / 100.0f));

        // Fill ArrayList with that many blockades(1's)
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < (size * size); i++) {
            if (i < spacesToFill) {
                list.add(1);
            } else {
                list.add(0);
            }
        }

        // Shuffle the list
        Collections.shuffle(list);

        // Fill the game grid with the randomized list
        int counter = 0;
        for (int i = 0; i < gridArray.length; i++) {
            for (int j = 0; j < gridArray[i].length; j++) {
                gridArray[i][j] = list.get(counter);
                counter++;
            }
        }

        // Set Car's starting position and ending position
        gridArray[startingCoord.x][startingCoord.y] = 2;
        gridArray[winningCoord.x][winningCoord.y] = 9;
    }

    /**
     * This method prints out the current contents of the given Grid
     */
    public void printCurrentGrid() {
        // Print out what the car currently sees.
        for (int x = 0; x < gridArray.length; x++) {
            for (int y = 0; y < gridArray.length; y++) {
                System.out.print(gridArray[x][y] + "  ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }
}
