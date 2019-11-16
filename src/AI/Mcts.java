package AI;

import View.GraphicLine;

import java.util.ArrayList;

public class Mcts {
    public int player;
    private Tree tree;
    private long timeLimit;

    public void mcts(ArrayList<GraphicLine> state) {
        tree = new Tree(state);
        timeLimit= System.currentTimeMillis()+1000; //1000 = 1 sec
        while (System.currentTimeMillis() < timeLimit) {
            selection(tree.getRootNode());
        }

    }

    public void selection(Node currentNode) {

    }

    public void expansion(Node toExpand) {
        for (ArrayList<GraphicLine> state: toExpand.getNextStates()) {
            toExpand.getChildren().add(new Node(toExpand, state));
        }

        //simulate(toExpand.getChildren().get((int) Math.random()*toExpand.getChildren().size()), "random");
    }

    public void simulate() {

    }


    public void backPropagation(Node node, int score) {
        if (node==tree.getRootNode()) {
            node.increaseVisit();
            node.updateScore(score);
        }else {
            node.increaseVisit();
            node.updateScore(score);
            backPropagation(node.getParent(), score);
        }
    }
}
