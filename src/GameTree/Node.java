package GameTree;

import java.util.ArrayList;

public class Node {
    private State state;
    private ArrayList<Node> children;
    private Node parent;
    private double score=0;
    private int visitNb =0;

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

    public void addScore(double nb){
        this.score+=nb;
    }

    public void addScore(){
      addScore(1);
    }

    private void addVisit(){
        this.visitNb++;
    }

    public ArrayList<Node> visit(){

        addVisit();

        if(children==null) {
            ArrayList<State> children = this.state.getChildren();
            ArrayList<Node> result = new ArrayList<>();
            for (State t : children) {
                result.add(new Node(t, this));
            }
            this.children=result;
            return result;
        }else{
            return children;
        }
    }

    public boolean isRoot(){
        if(this.parent==null){
            return true;
        }else{
            return false;
        }
    }

}
