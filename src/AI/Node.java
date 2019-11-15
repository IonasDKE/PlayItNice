package AI;

import View.GraphicLine;

import java.util.ArrayList;

public class Node {
    private int score;
    private int visitCounter;
    private Node parent;
    private ArrayList<GraphicLine> state;
    private ArrayList<Node> children;
    private ArrayList<ArrayList<GraphicLine>> nextStates;

    public Node(Node parent, ArrayList<GraphicLine> state) {
        this.score=0;
        this.visitCounter=0;
        this.parent=parent;
        this.state=state;
        this.children=new ArrayList<>();
        this.generateNextStates(state);
    }
    // Root node constructor (no parent)
    public Node(ArrayList<GraphicLine> state) {
        this.state=state;
        this.score=0;
        this.visitCounter=0;
        this.children=new ArrayList<>();
        this.generateNextStates(state);
    }

    public void generateNextStates(ArrayList<GraphicLine> state) {
        //Lines  should be up to date
        ArrayList<ArrayList<GraphicLine>> generatedState = new ArrayList<>();
        for (GraphicLine line:state) {
            if (line.isEmpty()) {

            }
        }
        this.nextStates=generatedState;
    }

    public ArrayList<ArrayList<GraphicLine>> getNextStates() {
        return this.nextStates;
    }

    public ArrayList<Node> getChildren() {
        return this.children;
    }

    public void updateScore(int toAdd) {
        this.score += toAdd;
    }

    public void increaseVisit() {
        this.visitCounter++;
    }

    public void setParent(Node parent) {
        this.parent=parent;
    }

    public Node getParent() {
        return this.parent;
    }

}
