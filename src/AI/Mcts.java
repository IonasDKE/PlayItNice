package AI;
import GameTree.*;

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

    public void selection(Node currentNode) {

    }

    public void expansion(Node toExpand) {
        for (State state: toExpand.getState().getChildren()) {
            toExpand.getChildren().add(new Node(state,toExpand));
        }

        //simulate(toExpand.getChildren().get((int) Math.random()*toExpand.getChildren().size()), "random");
    }

    public void simulate() {

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


}
