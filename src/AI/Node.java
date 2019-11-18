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
        ArrayList<ArrayList<GraphicLine>> generatedState = new ArrayList<>();
        ArrayList<GraphicLine> allCheckedLine=new ArrayList<>(); // already picked once -> state already generated
        for (int i = 0; i < getNumberOfEmpty(state); i++) {
            int counter = 0;
            boolean check = false;
            for (GraphicLine line : state) {
                if (line.isEmpty() && !check && !inList(allCheckedLine,line)) {
                    generatedState.get(counter).add(line.cloneLine());
                    allCheckedLine.add(line);
                    check = true;
                } else {
                    generatedState.get(counter).add(line.cloneLine());
                }
            }
            counter++;
        }
        this.nextStates=generatedState;
    }

    public boolean inList(ArrayList<GraphicLine> lines, GraphicLine toCompare) {
        boolean inSet = false;
        for (GraphicLine line : lines) {
            if (toCompare==line)
                return true;
        }
        return inSet;
    }

    public int getNumberOfEmpty(ArrayList<GraphicLine> state) {
        int counter=0;
        for (GraphicLine l:state) {
            if (l.isEmpty()) {
                counter++;
            }
        }
        return counter;
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

    public Node getParent() {
        return this.parent;
    }

    public ArrayList<GraphicLine> getState() {
        return this.state;
    }

}
