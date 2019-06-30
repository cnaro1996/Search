
public class Node {

    int g;
    int h;
    int f;
    Node tree;
    int search;

    public Node() {
        search = 0;
    }

    public Node(int g, int h, Node tree) {
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
