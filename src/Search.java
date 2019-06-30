import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.PriorityQueue;

public class Search {

    public static void main(String[] args){
        Integer[][] grid = genGrid(5, 0.25f, true);
        printGrid(grid);
    }

    /**
     * Generates a gridworld object of size dim x dim. index 0,0 is the start
     * state and index dim-1, dim-1 is the goal state.
     *
     * Blocked spaces are set to true; open spaces are set to false.
     * @param dim The dimensions of the gridworld.
     * @param p The probability of any generated cell initializing as blocked.
     * @param visibility Whether or not the gridworld is fully visible to the agent.
     * @return The 2D boolean array gridworld object.
     */
    public static Integer[][] genGrid(int dim, float p, boolean visibility){
        Integer[][] gridworld = new Integer[dim][dim];
        for (int i=0; i<dim; i++){
            for (int j=0; j<dim; j++){
                gridworld[i][j] = new Random().nextDouble() <= p ? -1 : (visibility? Integer.MAX_VALUE : 0);
            }
        }
        gridworld[0][0] = 0;
        gridworld[dim-1][dim-1] = Integer.MAX_VALUE;
        return gridworld;
    }

    /**
     * Prints a character graphic representation of the specified gridworld.
     * @param gridworld
     */
    public static void printGrid(Integer[][] gridworld){
        for (int i=0; i<gridworld.length; i++){
            for (int j=0; j<gridworld.length; j++){
                if(gridworld[i][j] == -1){
                    System.out.print("B ");
                } else{
                    System.out.print("o ");
                }
            }
            System.out.println();
        }
    }

    /**
     * An A* search algorithm implemented utilizing a min binary heap structure.
     *
     * @param gridworld The gridworld to apply the algorithm to.
     * @param heuristic The heuristic formula to be used when searching.
     * @param repeated Whether this method is being used in A* repeated or not.
     * @return True if solvable, false if not.
     */
    public static boolean aStarSearch(Integer[][] gridworld, Type heuristic, boolean repeated){
        int dim = gridworld.length-1;
        Node start = new Node(0, 0, 0, heuristicCalc(heuristic, 0,0,
                dim, dim), null);
        Comparator<Node> nodeComparator = new NodeComparator();
        PriorityQueue<Node> openList = new PriorityQueue<Node>(nodeComparator);
        ArrayList<Node> closedList = new ArrayList<Node>();

        // Put this section of code into the A* repeated method and remove the repeated argument.
        if(repeated){
            //initialize g values in gridworld to 0.
            for (int i=0; i<dim+1; i++){
                for (int j=0; j<dim+1; j++){
                    gridworld[i][j] = (gridworld[i][j] == -1) ? -1 : 0;
                }
            }
        }

        openList.add(start);
        while(openList.size() != 0) {
            Node curr = openList.poll();
            if (curr.x == dim && curr.y == dim) return true; //change to node later
            closedList.add(curr);
            // Check if agent can move up and wont be out of bounds or blocked.
            if(curr.y-1 >= 0) {
                if(gridworld[curr.x][curr.y-1] != -1) {
                    //Node child = new Node(curr.x, curr.y-1, curr.g+1);
                }
            }

        }
        return false;
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
