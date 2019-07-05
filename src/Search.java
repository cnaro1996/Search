import java.util.*;

public class Search {

    public static void main(String[] args){
        Integer[][] grid = genGrid(100, 0.33f, true);
        printGrid(grid);
        Node result = aStarSearch(grid, Type.CHEBYSHEV);
        printGrid(grid, result);
        System.out.print(result.toString() + Type.CHEBYSHEV.toString());
    }

    /**
     * Generates a gridworld object of size dim x dim. index 0,0 is the start
     * state and index dim-1, dim-1 is the goal state. gridworld coordinates are
     * indexed as gridworld[x][y]: x is the horizontal axis and y is the vertical.
     * Blocked spaces are set to -1; open spaces are either infinity or 0.
     *
     * @param dim The dimensions of the gridworld.
     * @param p The probability of any generated cell initializing as blocked.
     * @param visibility Whether or not the gridworld is fully visible to the agent.
     * @return The 2D boolean array gridworld object.
     */
    public static Integer[][] genGrid(int dim, float p, boolean visibility){
        Integer[][] gridworld = new Integer[dim][dim];
        for (int i=0; i<dim; i++){
            for (int j=0; j<dim; j++){
                gridworld[i][j] = new Random().nextDouble() <= p ?
                        -1 : (visibility? Integer.MAX_VALUE : 0);
            }
        }
        gridworld[0][0] = 0;
        gridworld[dim-1][dim-1] = Integer.MAX_VALUE;
        return gridworld;
    }

    /**
     * Prints a character graphic representation of the specified gridworld.
     */
    public static void printGrid(Integer[][] gridworld){
        System.out.print("Coordinates:\n  ");
        for(int i=0; i<gridworld.length-2 && i<10; i++) System.out.print(" " + i);
        System.out.println(" ...");
        for (int i=0; i<gridworld.length; i++){
            if(i<gridworld.length-3 && i<10) {
                System.out.print(" " + i + " ");
            } else if(i < 13) {
                System.out.print(" " + "." + " ");
            } else System.out.print("   ");
            for (int j=0; j<gridworld.length; j++){
                if(gridworld[j][i] == -1){
                    System.out.print("B ");
                } else{
                    System.out.print("o ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Prints a character graphic representation of the specified gridworld.
     * Draws the path of the agent Node traveled (marked with X).
     */
    public static void printGrid(Integer[][] gridworld, Node agent){

        Integer[][] gridworld1 = new Integer[gridworld.length][gridworld.length];
        for (int i=0; i<gridworld1.length; i++) {
            for(int j=0; j<gridworld1.length; j++) {
                gridworld1[j][i] = gridworld[j][i];
            }
        }

        Node ptr = agent;
        gridworld1[ptr.x][ptr.y] = -2;
        while(ptr.tree != null) {
            gridworld1[ptr.tree.x][ptr.tree.y] = -2;
            ptr = ptr.tree;
        }

        System.out.print("Coordinates:\n  ");
        for(int i=0; i<gridworld1.length-2 && i<10; i++) System.out.print(" " + i);
        System.out.println(" ...");
        for (int i=0; i<gridworld1.length; i++){
            if(i<gridworld1.length-3 && i<10) {
                System.out.print(" " + i + " ");
            } else if(i < 13) {
                System.out.print(" " + "." + " ");
            } else System.out.print("   ");
            for (int j=0; j<gridworld1.length; j++){
                if(gridworld1[j][i] == -1){
                    System.out.print("b ");
                } else if(gridworld1[j][i] == -2){
                    System.out.print("X ");
                } else {
                    System.out.print("o ");
                }
            }
            System.out.println();
        }
    }

    /**
     * An A* search algorithm implemented utilizing a min binary heap structure.
     *
     * param repeated Whether this method is being used in A* repeated or not (deprecated).
     * @param gridworld The gridworld to apply the algorithm to.
     * @param heuristic The heuristic formula to be used when searching.
     * @return Goal Node with path tree if solvable, null if not.
     */
    public static Node aStarSearch(Integer[][] gridworld, Type heuristic){
        int dim = gridworld.length-1;
        Node start = new Node(0, 0, 0, heuristicCalc(heuristic, 0,0,
                dim, dim), null);
        Comparator<Node> nodeComparator = new NodeComparator();
        PriorityQueue<Node> openList = new PriorityQueue<Node>(nodeComparator);
        ArrayList<Node> closedList = new ArrayList<Node>();

        /*
        // Put this section of code into the A* repeated method and remove the repeated argument.
        if(repeated){
            //initialize g values in gridworld to 0.
            for (int i=0; i<dim+1; i++){
                for (int j=0; j<dim+1; j++){
                    gridworld[i][j] = (gridworld[i][j] == -1) ? -1 : 0;
                }
            }
        }
        */

        openList.add(start);
        while(openList.size() != 0) {
            Node curr = openList.poll();
            if (curr.x == dim && curr.y == dim) return curr; // Success.
            closedList.add(curr);
            // Check if the agent can move up, right, down, or left and wont be out of bounds
            // or blocked.
            if(curr.y-1 >= 0 && gridworld[curr.x][curr.y-1] != -1) {
                performAction(Direction.UP, heuristic, curr, openList, closedList, gridworld);
            }
            if(curr.x+1 <= dim && gridworld[curr.x+1][curr.y] != -1) {
                performAction(Direction.RIGHT, heuristic, curr, openList, closedList, gridworld);
            }
            if(curr.y+1 <= dim && gridworld[curr.x][curr.y+1] != -1) {
                performAction(Direction.DOWN, heuristic, curr, openList, closedList, gridworld);
            }
            if(curr.x-1 >= 0 && gridworld[curr.x-1][curr.y] != -1) {
                performAction(Direction.LEFT, heuristic, curr, openList, closedList, gridworld);
            }
        }
        return start;
    }

    /**
     * Explores the direction adjacent to the agent and updates the openList accordingly.
     * @param direction The direction to explore from the agent's current position.
     * @param curr The current position of the agent.
     */
    public static void performAction(Direction direction, Type heuristic, Node curr,
                                     PriorityQueue<Node> openList, ArrayList<Node> closedList,
                                     Integer[][] gridworld) {
        int x = curr.x;
        int y = curr.y;
        int dim = gridworld.length-1;

        // Initialize new coordinates based on the direction to explore.
        switch(direction) {
            case UP:
                y--;
                break;
            case RIGHT:
                x++;
                break;
            case DOWN:
                y++;
                break;
            case LEFT:
                x--;
                break;
        }

        Node child, temp;
        gridworld[x][y] = curr.g + 1;
        child = new Node(x, y, curr.g+1,
                heuristicCalc(heuristic, x, y, dim, dim), curr);
        // Check if the child is in the openList or the closedList.
        if(!closedList.contains(child) && !openList.contains(child)) {
            openList.add(child);
            // Check if the child is in the openList with a higher path cost.
        } else if(openList.contains(child) &&
                (temp = nodeSearch(openList, child)).f > child.f) {
            temp.f = child.f;
        }
    }

    /**
     * Searches a PriorityQueue<Node> for the given Node. Returns the node upon successful search,
     * null if the Queue does not contain the node.
     * @param heap The PriorityQueue<Node> to search.
     * @param key The Node to find.
     * @return The Node if found, null if not found.
     */
    public static Node nodeSearch(PriorityQueue<Node> heap, Node key) {
        Iterator<Node> it = heap.iterator();
        while(it.hasNext()) {
            Node curr = it.next();
            if(curr.equals(key)) {
                return curr;
            }
        }
        return null;
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
     * Cardinal directions used to describe the agent's movements.
     */
    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    /**
     * Calculates the heuristic between two given states.
     * @param type The type of heuristic formula to use for calculations.
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
