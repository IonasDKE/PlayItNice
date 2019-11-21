package GameTree;

import java.util.ArrayList;

/*
TODO:
- We could add a general class to list parameters, such as the exploration-exploitation coefficient.
 */

public class Node {
    private State state;
    private ArrayList<Node> children;
    private Node parent;
    private double score = 0;
    private int visitNb = 0;
    private double uctScore = 0;
    private final double COEFFICIENT = 0.5; //this coefficient balances exploration and exploitation in the UCT

    public Node(State state, Node parent) {
        this.state = state;
        this.parent = parent;
    }

    public State getState() {
        return state;
    }

    public double getScore() {
        return score;
    }

    public int getVisitNb() {
        return visitNb;
    }

    public void addScore(double nb) {
        this.score += nb;
    }

    public void addVisit() {
        this.visitNb++;
    }
    /*
    public ArrayList<Node> visit() {

        addVisit();

        if (children == null) {
            children = computeChildren();
        }
        return children;
    }
    */
    private ArrayList<Node> computeChildren() {
        ArrayList<State> children = this.state.getChildren();
        ArrayList<Node> result = new ArrayList<>();
        for (State t : children) {
            result.add(new Node(t, this));
        }
        this.children = result;
        return result;
    }

    public boolean isRoot() {
        if (this.parent == null) {
            return true;
        } else {
            return false;
        }
    }

    public Node getParent() {
        return this.parent;
    }

    public ArrayList<Node> getChildren() {
        if (this.children==null)
            this.computeChildren();
        return this.children;
    }

    public double getUctScore(){
        this.computeUctScore();
        return this.uctScore;
    }

    public void computeUctScore(){
        this.uctScore = this.score + COEFFICIENT * Math.sqrt( Math.log( this.parent.getVisitNb() ) / this.visitNb );
    }

}
