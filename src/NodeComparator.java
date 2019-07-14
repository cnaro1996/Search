import java.util.Comparator;

/**
 * Used to tell the PriorityQueue (min heap) in the search algorithms
 * how to compare Node objects (by their f values).
 */
public class NodeComparator implements Comparator<Node> {
    boolean larger;

    /**
     * @param larger if you want to break ties by favoring
     * larger g values, set this to true.
     */
    public NodeComparator(boolean larger) {
        this.larger = larger;
    }

    public int compare(Node a, Node b){
        if(a.f < b.f) {
            return -1;
        } else if(a.f == b.f) {
            if(larger){
                if(a.g > b.g) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                if(a.g > b.g) {
                    return 1;
                } else {
                    return -1;
                }
            }
        } else return 1;
    }
}