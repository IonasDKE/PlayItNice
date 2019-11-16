package AI;

import View.GraphicLine;

import java.util.ArrayList;

public class Tree {
    private Node rootNode;
    private static ArrayList<Node> tree = new ArrayList<>();

    public Tree(ArrayList<GraphicLine> state ) {
        this.rootNode= new Node(state);
        tree.add(rootNode);
    }

    //add node to the tree list
    public void addNode(Node node) {
        tree.add(node);
    }

    public void changeRoot(Node newRoot) {
        this.rootNode=newRoot;
    }

    public void deleteParent(Node node) {
        tree.remove(node.getParent());
    }

    public Node getRootNode() {
        return this.rootNode;
    }


}
