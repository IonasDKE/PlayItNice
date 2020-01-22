package GameTree;

import java.util.ArrayList;

public class Node {
    private State state;
    private ArrayList<Node> children;
    private Node parent;
    private ArrayList<Node> parents;
    private int depth;
    private double score = 0;
    private int numberOfWin=0;
    private int turn;
    private int visitNb = 0;
    private int weight;
    private Integer line;
    private double uctScore = Double.NEGATIVE_INFINITY;
    public final static int MIN = -1000000000;

    public Node(State state, Node parent) {
        this.state = state;
        this.parent = parent;
    }

    public Node(State state, int turn, Node parent, Integer line){
        this.state = state;
        this.turn = turn;
        this.parent = parent;
        this.weight = MIN;
        this.line = line;

    }

    public ArrayList<Node> computeChildren() {
        ArrayList<State> stateChildren = this.state.computeAndGetChildren();
        ArrayList<Node> newChildren = new ArrayList<>();

        for (State state : stateChildren) {
            newChildren.add(new Node(state, this));
        }
        this.children = newChildren;
        return newChildren;
    }

    public ArrayList<Node> computeChildrenPruning() {
        ArrayList<State> stateChildren = this.state.computeAndGetChildrenPruning();
        ArrayList<Node> newChildren = new ArrayList<>();

        for (State state : stateChildren) {
            newChildren.add(new Node(state, this));
        }
        this.children = newChildren;
        return newChildren;
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

    /**
     * Compute the UCT value of a node, for the selection method in Monte Carlo Tree Search
     * COEFFICIENT is a global value so that we can change it from outside to mae the experiments
     */
    public static float COEFFICIENT = (float) 1.41; //this coefficient balances exploration and exploitation in the UCT
    public double getUctScore(){

        if (this.visitNb==0) {
            return Integer.MAX_VALUE;
        }else {
            this.uctScore = (this.score / (double) this.visitNb) +
                    COEFFICIENT * Math.sqrt(Math.log(this.getParent().getVisitNb()) / (double) this.visitNb);
            return this.uctScore;

        }
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
    }

    public void setLine(Integer line){
        this.line = line;
    }

    public Integer getLine() {
        return this.line;
    }

}