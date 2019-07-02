/**
 * Used to store data about states as the search algorithm progresses.
 */
public class Node {

    int x,y;
    int g;
    double h;
    double f;
    Node tree;
    int search;

    public Node() {
        search = 0;
    }

    public Node(int x, int y, int g, double h, Node tree) {
        this.x = x;
        this.y = y;
        this.g = g;
        this.h = h;
        this.f = g + h;
        this.tree = tree;
        search = 0;
    }

    // We compare Nodes by their coordinates. The equals method must be modified
    // for proper comparisons in data structures.
    @Override
    public boolean equals(Object object) {
        boolean isEqual = false;

        if(object != null && object instanceof Node) {
            isEqual = (this.x == ((Node) object).x && this.y == ((Node) object).y);
        }
        return isEqual;
    }

    /* If I change the closedList to a hashmap, this method will need to be implemented.
    @Override
    public int hashCode() {

    }
    */


    public String toString(){
        return "";
    }
}
