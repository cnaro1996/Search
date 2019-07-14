import java.util.*;

public class Search {

    public static void main(String[] args){
        int[][] grid = genGrid(10, 0.25f, true);
        Comparator<Node> nodeComparator = new NodeComparator();
        PriorityQueue<Node> openList = new PriorityQueue<Node>(nodeComparator);
        ArrayList<Node> closedList = new ArrayList<Node>();
        SearchTracker regular = new SearchTracker(null, false, null, 0);

        printGrid(grid);

        Node result = repeatedAStarSearch(grid, Direction.BACKWARD, Type.CHEBYSHEV);

        //Node result = aStarSearch(grid, openList, closedList, Type.CHEBYSHEV,
        //      regular);
        printGrid(grid, result);
        System.out.print(result.toString() + ", Heuristic: " + Type.CHEBYSHEV.toString());
    }

    /**
     * Generates a gridworld object of size dim x dim. index 0,0 is the start
     * state and index dim-1, dim-1 is the goal state. gridworld coordinates are
     * indexed as gridworld[x][y]: x is the horizontal axis and y is the vertical.
     * Blocked spaces are set to -1; open spaces are either infinity or 0 and
     * represent that space's g value.
     *
     * @param dim The dimensions of the gridworld.
     * @param p The probability of any generated cell initializing as blocked.
     * @param visibility Whether or not the gridworld is fully visible to the agent.
     * @return The 2D boolean array gridworld object.
     */
    public static int[][] genGrid(int dim, float p, boolean visibility){
        int[][] gridworld = new int[dim][dim];
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
    public static void printGrid(int[][] gridworld){
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
     * Prints a character graphic representation of the specified gridworld and
     * draws the path of the agent Node traveled (marked with X).
     */
    public static void printGrid(int[][] gridworld, Node agent){
        if(agent.x < 0 || agent.y < 0) {
            return;
        }

        // Create a copy of the gridworld so we don't modify the original.
        int[][] gridworld1 = new int[gridworld.length][gridworld.length];
        for (int i=0; i<gridworld1.length; i++) {
            for(int j=0; j<gridworld1.length; j++) {
                gridworld1[j][i] = gridworld[j][i];
            }
        }

        // Mark the path the agent traveled.
        Node ptr = agent;
        gridworld1[ptr.x][ptr.y] = -2;
        while(ptr.tree != null) {
            gridworld1[ptr.tree.x][ptr.tree.y] = -2;
            ptr = ptr.tree;
        }

        // Print the resultant gridworld.
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
     * Assumes the agent cannot see the underlying gridworld's spaces not directly
     * adjacent to it, but can see the goal and start states. Uses the A* search
     * algorithm to search along a best-case path, researching whenever the agent
     * encounters a blocked space in this path.
     *
     * @param gridworld The underlying gridworld the agent must travel through
     * @param direction Whether to perform forwards or backwards Repeated A* search.
     * @param heuristic The heuristic formula to use.
     * @return The Node with the tree path the agent can travel to the goal. If the
     * goal is impossible to reach, the path the agent traveled until it concluded
     * failure.
     */
    public static Node repeatedAStarSearch(int[][] gridworld, Direction direction,
                                           Type heuristic) {
        // Create a duplicate gridworld object with no information on blocked spaces.
        // Update this object as the agent discovers blocked spaces.
        int goal;
        int dim = gridworld.length-1;
        int[][] agentWorld = new int[gridworld.length][gridworld.length];
        Comparator<Node> nodeComparator = new NodeComparator();
        PriorityQueue<Node> openList = new PriorityQueue<Node>(nodeComparator);
        ArrayList<Node> closedList = new ArrayList<Node>();
        SearchTracker t = new SearchTracker(direction, true, null, 0);
        Node presumedPath;

        // Initialize values based on the direction the agent will travel.
        if(direction == Direction.FORWARD) {
            t.agent = new Node(0, 0, 0,
                    heuristicCalc(heuristic, 0, 0, dim, dim), null);
            t.path = new Node(0, 0, 0,
                    heuristicCalc(heuristic, 0, 0, dim, dim), null);
            goal = dim;
        } else { // BACKWARD
            t.agent = new Node(dim, dim, 0,
                    heuristicCalc(heuristic, dim, dim, 0, 0), null);
            t.path = new Node(dim, dim, 0,
                    heuristicCalc(heuristic, dim, dim, 0, 0), null);
            goal = 0;
        }

        Node temp;
        while(!t.finished) {
            t.counter ++;
            t.agent.search = t.counter;
            t.agent.g = 0;
            agentWorld[t.agent.x][t.agent.y] = 0;
            agentWorld[goal][goal] = Integer.MAX_VALUE;
            closedList.clear();
            openList.clear();
            temp = aStarSearch(agentWorld,
                    openList, closedList, heuristic, t);
            temp.search = t.counter;
            //System.out.println(temp); // Debugging statement.
            presumedPath = reversePath(temp);
            if(openList.isEmpty()) { // Failure.
                break;
            }
            t.agent = moveAgent(t, presumedPath, agentWorld, gridworld);
            System.out.println("Current counter: " + t.counter);
            //System.out.println("Current agent tree: " + t.agent.toString());
            System.out.println("------------------------------------");

        }
        return (t.agent.x == goal && t.agent.y == goal)? t.path :
                new Node(-1,-1, -1, -1, null);
    }

    /**
     * Moves the agent along the presumed path until it encounters a blocked space
     * or the goal. Updates the agent's view of the gridworld (agentWorld) if it
     * encounters a blocked space.
     *
     * Updates the agent's position to the space before the block on the presumed
     * path if a blocked space is found, or to the goal if no blocked spaces are found.
     *
     * @param presumedPath
     * @param agentWorld The gridworld the agent sees.
     * @param gridworld The fully visible gridworld.
     */
    public static Node moveAgent(SearchTracker tracker, Node presumedPath, int[][] agentWorld,
                                 int[][] gridworld) {
        boolean finished = true;
        System.out.println("Move agent, Presumed Path " + presumedPath.toString());
        Node ptr = presumedPath;
        while(ptr.tree != null){
            if(gridworld[ptr.tree.x][ptr.tree.y] == -1) {
                agentWorld[ptr.tree.x][ptr.tree.y] = -1;
                finished = false;
                ptr.tree = null;
                break;
            } else {
                ptr = ptr.tree;
            }
        }

        // Update whether the agent has reached the goal.
        if(finished) tracker.finished = true;

        // Debugging statement.
        System.out.println("Current agent travel path: " + tracker.path.toString());
        System.out.println("Current new path segment: " + presumedPath.toString());

        // Update agent's current path if it's not the same spot.
        //if(presumedPath.x == tracker.agent.x &&
        //        presumedPath.y == tracker.agent.y &&
        //        presumedPath.tree != null) {
            Node pptr = tracker.path;
            while (pptr.tree != null &&
                    !(pptr.x == presumedPath.x && pptr.y == presumedPath.y)){
                pptr = pptr.tree;
            }
            //tracker.path.tree = presumedPath.tree;
            pptr.tree = presumedPath.tree;
        //}

        // Debugging statement.
        System.out.println("New agent travel path: " + tracker.path.toString());

        // Update agent's new position.
        tracker.agent = ptr;

        // Debugging while loop. Remove when code is working.
        /*
        ptr = tracker.path;
        while(ptr.tree != null) {
            System.out.println("(" + ptr.x + "," + ptr.y + ")");
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return null;
            }
            ptr = ptr.tree;
        }
        System.out.println("(" + ptr.x + "," + ptr.y + ")");
        System.out.println(); // Debugging statement.
        */
        return tracker.agent;


        /*
        System.out.println("Moved agent.");
        ptr = tracker.agent;
        while(ptr.tree != null) {
            ptr = ptr.tree;
        }
        ptr.tree = presumedPath.tree;
        System.out.println("_____________________");
        System.out.println("counter = " + tracker.counter );//+ ", agent path = " + tracker.agent.toString());
        */

        /*
        return reversePath(tracker.agent);
        */
    }

    /**
     * Returns a tree path in reverse order.
     *
     * If the path tree is null, the aStarSearch algorithm failed and this method will return
     * the path as is.
     *
     * @param path
     * @return
     */
    public static Node reversePath(Node path) {
        if(path == null) { // Null pointer agent
            return null;
        } else {
            //Reverse the path order.
            Stack<Node> temp = new Stack<Node>();
            temp.push(path);
            Node ptr = path.tree;
            if(ptr != null){
                while(ptr.tree != null) {
                    temp.push(ptr);
                    ptr = ptr.tree;
                }
                temp.push(ptr);
            } else {
                return temp.pop();
            }

            // Reconstruct the path.
            ptr = temp.pop();
            path = ptr;
            while(!temp.empty()) {
                ptr.tree = temp.pop();
                ptr = ptr.tree;
            }
            ptr.tree = null;
            return path;
        }
    }


    /**
     * An A* search algorithm implemented utilizing a min binary heap structure.
     *
     * param repeated Whether this method is being used in A* repeated or not (deprecated).
     * @param gridworld The gridworld to apply the algorithm to.
     * @param heuristic The heuristic formula to be used when searching.
     * @return Goal Node with path tree if solvable, start Node if not (with tree = null).
     */
    public static Node aStarSearch(int[][] gridworld, PriorityQueue<Node> openList,
                                   ArrayList<Node> closedList, Type heuristic,
                                   SearchTracker tracker){
        int dim = gridworld.length-1;
        Node start;
        boolean condition;
        int goal;

        // If running A* repeated, we want the algorithm to start at the agent's current position.
        // If running A* repeated, we want to set goal coordinates based on it's direction.
        // The while loop condition depends on whether we're running repeated A* search or not.
        if(tracker.repeated) {
            start = tracker.agent;
            goal = tracker.direction == Direction.FORWARD ? dim : 0;
            condition = gridworld[goal][goal] > start.f;
        } else {
            start = new Node(0, 0, 0, heuristicCalc(heuristic, 0,0,
                    dim, dim), null);
            goal = dim;
            condition = true; // The first iteration of the loop always happens.

        }

        Node temp;
        openList.add(start);
        while(condition) {
            Node curr = openList.poll();
            if (curr.x == goal && curr.y == goal) return curr; // Success.
            closedList.add(curr);
            // Check if the agent can move up, right, down, or left and wont be out of bounds
            // or blocked.
            if(curr.y-1 >= 0 && gridworld[curr.x][curr.y-1] != -1) {
                performAction(Direction.UP, heuristic, curr, openList, closedList,
                        gridworld, tracker);
            }
            if(curr.x+1 <= dim && gridworld[curr.x+1][curr.y] != -1) {
                performAction(Direction.RIGHT, heuristic, curr, openList, closedList,
                        gridworld, tracker);
            }
            if(curr.y+1 <= dim && gridworld[curr.x][curr.y+1] != -1) {
                performAction(Direction.DOWN, heuristic, curr, openList, closedList,
                        gridworld, tracker);
            }
            if(curr.x-1 >= 0 && gridworld[curr.x-1][curr.y] != -1) {
                performAction(Direction.LEFT, heuristic, curr, openList, closedList,
                        gridworld, tracker);
            }
            // Update condition statement for while loop.
            if(tracker.repeated) {
                temp = openList.peek();
                if(temp == null){
                    return start;
                } else {
                    condition = gridworld[goal][goal] > temp.f;
                }
            } else {
                condition = openList.size() != 0 ;
            }
        }
        return start; // Failure.
    }

    /**
     * Explores the direction adjacent to the agent and updates the openList accordingly.
     * @param direction The direction to explore from the agent's current position.
     * @param curr The current position of the agent.
     */
    public static void performAction(Direction direction, Type heuristic, Node curr,
                                     PriorityQueue<Node> openList, ArrayList<Node> closedList,
                                     int[][] gridworld, SearchTracker tracker) {
        int x = curr.x;
        int y = curr.y;
        int goal = tracker.direction == Direction.BACKWARD ? 0 : gridworld.length-1;

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
        child = new Node(x, y, 0,
                heuristicCalc(heuristic, x, y, goal, goal), curr);

        // A* repeated search performs actions differently than regular A* search.
        if(tracker.repeated) {
            if(closedList.contains(child)){
                return;
            }
            if(child.search < tracker.counter) {
                child.g = Integer.MAX_VALUE;
                gridworld[child.x][child.y] = Integer.MAX_VALUE;
                child.search = tracker.counter;
            }
            if(gridworld[child.x][child.y] > curr.g+1) {
                child.g = curr.g+1;
                gridworld[child.x][child.y] = curr.g+1;
                child.tree = curr;
                if(openList.contains(child) &&
                        (temp = nodeSearch(openList, child)).f > child.f) {
                    temp.f = child.f;
                } else {
                    openList.add(child);
                }
            }
            //System.out.println("(" + x + "," + y + ")");
        } else {
            // Not being used in repeated A* search.
            gridworld[x][y] = curr.g + 1;
            child.g = curr.g+1;
            // Check if the child is in the openList or the closedList.
            if(!closedList.contains(child) && !openList.contains(child)) {
                openList.add(child);
                // Check if the child is in the openList with a higher path cost.
            } else if(openList.contains(child) &&
                    (temp = nodeSearch(openList, child)).f > child.f) {
                temp.f = child.f;
            }
        }
    }

    /**
     * Searches a PriorityQueue<Node> for the given Node via an Iterator object.
     * Returns the node upon successful search or the original key if the Queue
     * does not contain the node.
     * @param heap The PriorityQueue<Node> to search.
     * @param key The Node to find.
     * @return The Node if found, key if not found.
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
        RIGHT,
        FORWARD,
        BACKWARD;
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
            // CHEBYSHEV
            return Math.max(Math.abs(x1-x2), Math.abs(y1-y2));
        }
    }
}
