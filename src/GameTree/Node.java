package GameTree;

import java.util.ArrayList;

/*
TODO:
- We could add a general class to list parameters, such as the exploration-exploitation coefficient.
 */
public class Node {
    private static int totalVisit=0;
    private State state;
    private ArrayList<Node> children=new ArrayList<>();
    private Node parent;
    private double score = 0;
    private int numberOfWin=0;
    private int visitNb = 0;
    private double uctScore = Double.NEGATIVE_INFINITY;

    public Node(State state, Node parent) {
        this.state = state;
        this.parent = parent;
    }

    public ArrayList<Node> computeChildren( ) {
        ArrayList<State> stateChildren = this.state.computeAndGetChildren();
        ArrayList<Node> nodeNewChildren = new ArrayList<>();
        for (State t : stateChildren) {
            nodeNewChildren.add(new Node(t, this));
        }
        this.children = nodeNewChildren;
        return nodeNewChildren;
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

    public ArrayList<Node> getSafeChildren() {
        if (!this.hasChildren())
            this.computeChildren();
        return this.children;
    }

    public boolean hasChildren(){
        //System.out.println("children = " + children);
        return this.children != null;
    }

    public ArrayList<Node> computeAndGetChildren(){
        if(children==null) {
            computeChildren();
        }
        return children;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public ArrayList<Node> safeGetChildren(){
        //addVisit();
        if (children == null) {
            children = computeChildren();
        }
        return children;
    }

    public double getUctScore(){
        final double COEFFICIENT = 0.1; //this coefficient balances exploration and exploitation in the UCT
        double toReturn=0;
        if (this.visitNb==0) {
            toReturn=Double.MAX_VALUE;
        } else if (this.uctScore==Double.NEGATIVE_INFINITY) {
            this.uctScore = (this.numberOfWin /(double)this.visitNb)+ COEFFICIENT * Math.sqrt(Math.log(totalVisit)/(double) this.visitNb);
            toReturn=this.uctScore;
        }
        return toReturn;
    }

    public void setParent(Node newParent) {
        this.parent=newParent;
    }

    public int getNumberOfWin() {
        return numberOfWin;
    }

    public void increaseWinNb() {
        this.numberOfWin ++;
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

    public double getAvg() {
        return this.score/this.visitNb;
    }

    public void addVisit() {
        this.visitNb++;
        totalVisit++;
    }

    public void deleteParent(Boolean firstTurn) {
        if (!firstTurn)
            this.parent=null;
    }

    public static void resetTotalVisit() {
        totalVisit=0;
    }

}
