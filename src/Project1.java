import java.util.Random;
import java.math.*;

public class Project1 {

    public static void main(String[] args){
        boolean[][] grid = genGrid(5, 0.25f);
        printGrid(grid);
    }

    /**
     * Generates a gridworld object of size dim x dim. index 0,0 is the start
     * state and index dim-1, dim-1 is the goal state.
     *
     * Blocked spaces are set to true; open spaces are set to false.
     * @param dim The dimensions of the gridworld.
     * @param p The probability of any generated cell initializing as blocked.
     * @return The 2D boolean array gridworld object.
     */
    public static boolean[][] genGrid(int dim, float p){
        boolean[][] gridworld = new boolean[dim][dim];
        for (int i=0; i<dim; i++){
            for (int j=0; j<dim; j++){
                gridworld[i][j] = new Random().nextDouble() <= p;
            }
        }
        gridworld[0][0] = false;
        gridworld[dim-1][dim-1] = false;
        return gridworld;
    }

    /**
     * Prints a character graphic representation of the specified gridworld.
     * @param gridworld
     */
    public static void printGrid(boolean[][] gridworld){
        for (int i=0; i<gridworld.length; i++){
            for (int j=0; j<gridworld.length; j++){
                if(gridworld[i][j]){
                    System.out.print("B ");
                } else{
                    System.out.print("o ");
                }
            }
            System.out.println();
        }
    }

    /**
     * An A* algorithm implemented utilizing a min binary heap structure.
     * This method assumes all gridworld spaces are available to see by
     * the agent.
     * @param gridworld The gridworld to apply the algorithm to.
     * @return True if solvable, false if not.
     */
    public static boolean aStarSearch(boolean[][] gridworld){
        
    }

    /**
     * The heuristic types specified by the project.
     */
    public enum Type {
        EUCLIDIAN,
        MANHATTAN,
        CHEBYSHEV;
    }

    /**
     * Calculates the heuristic between two given states.
     * @param type The type of heuristic formula to use for calculations.
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @return The specified heuristic between two states
     */
    public static double heuristicCalc(Type type, int x1, int y1, int x2, int y2){
        if(type == Type.EUCLIDIAN) {
            return Math.sqrt((x1-x2)^2 + (y1-y2)^2);
        } else if(type == Type.MANHATTAN) {
            return Math.abs(x1-x2) + Math.abs(y1-y2);
        } else {
            //CHEBYSHEV
            return Math.max(Math.abs(x1-x2), Math.abs(y1-y2));
        }
    }
}
