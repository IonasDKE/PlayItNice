package AI;

import View.GraphicLine;

import java.util.ArrayList;

public class Mcts {


    public void mcts(View.Board state ) {
        Tree tree = new Tree(state);
    }

    public void selection () {

    }

    public void expansion(Node toExpand) {
        for (ArrayList<GraphicLine> state: toExpand.getNextStates()) {
            toExpand.getChildren().add(new Node(toExpand, state));
        }
        simulate();
    }

    public void simulate() {

    }

    public void backPropagation() {

    }
}
