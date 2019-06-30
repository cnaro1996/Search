import java.util.Comparator;

/**
 * Used to tell the PriorityQueue (min heap) in the search algorithms
 * how to compare Node objects (by their f values).
 */
public class NodeComparator implements Comparator<Node> {
    public int compare(Node a, Node b){
        if(a.f < b.f) {
            return -1;
        } else if(a.f == b.f) {
            return 0;
        } else return 1;
    }
}