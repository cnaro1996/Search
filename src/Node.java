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

    public String toString(){
        return "";
    }
}
