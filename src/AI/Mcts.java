package AI;
import GameTree.*;
import View.*;
import java.util.ArrayList;
import java.util.Collections;

public class Mcts {
    public int player;
    private Tree tree;
    private long timeLimit;

    public void mcts(State state) {
        tree = new Tree(new Node(state, null));
        timeLimit= System.currentTimeMillis()+1000; //1000 = 1 sec
        while (System.currentTimeMillis() < timeLimit) {
            selection(tree.getRoot());
        }

    }

    //this method return a child node of a node that it is fed, based on the highest UCT score
    public Node selection(Node rootNode) {
        Node node = rootNode;
        if (node.getChildren().size() != 0){                  //while loop just doesn't make sense here
            node = this.maxUctNode(node.getChildren());
        }
        return node;
    }

    //This method is ancilliary to the selection method
    public Node maxUctNode (ArrayList<Node> nodes){
        Node maxUctNode = nodes.get(0);
        for( Node aNode: nodes){
            if (aNode.getUctScore() > maxUctNode.getUctScore()){
                maxUctNode = aNode;
            }
        }
        return maxUctNode;
    }

    public void expansion(Node toExpand) {
        for (State state: toExpand.getState().getChildren()) {
            toExpand.getChildren().add(new Node(state,toExpand));
        }

        //simulate(toExpand.getChildren().get((int) Math.random()*toExpand.getChildren().size()), "random");
    }

    public int simulateRandomPlayout(Node selectedNode) {
        Node node = new Node(selectedNode.getState(), selectedNode.getParent());

        State stateCopy = new State(selectedNode.getState().getLines(), selectedNode.getState().getSquares());
        while (isNotComplete(stateCopy)) {
            stateCopy.computeChildren();
            stateCopy=stateCopy.getChildren().get((int)Math.random()*stateCopy.getChildren().size());
        }

        return score;
    }


    public void backPropagation(Node node, int score) {
        if (node==tree.getRoot()) {
            node.addVisit();
            node.addScore(score);
        }else {
            node.addVisit();
            node.addScore(score);
            backPropagation(node.getParent(), score);
        }
    }

    public boolean isNotComplete(State currentState) {
        boolean allLinesEmpty=false;
        for (Line l: currentState.getLines()){
            if (l.isEmpty())
                allLinesEmpty=true;
        }
        return allLinesEmpty;
    }


}
