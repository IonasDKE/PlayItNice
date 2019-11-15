package AI;

public class Node {
    private int score;
    private int visitCounter;
    private Node parent;

    public Node(Node parent) {
        this.score=0;
        this.visitCounter=0;
        this.parent=parent;
    }

    public void setParent(Node parent) {
        this.parent=parent;
    }
}
