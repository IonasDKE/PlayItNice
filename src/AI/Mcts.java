package AI;

import Controller.GridController;
import GameTree.*;
import View.*;
import java.util.*;

/**
 * Monte Carlo Tree Search
 * 4 steps (methods):
 *  Selection: returns a node that seems promising
 *  Expansion: generates the children of the promising node/state
 *  Simulation: simulates the promising node/state until the end of the game, returns win or loose depending on the simulation
 *  Back propagation: for all the parent of the promising node, increase/decrease the score found in the simulation stage.
 *                   And increase the number of visit by 1.
 **/

public class Mcts extends AISolver {

    public Graph graph;
    public static ArrayList<Graph> graphs = new ArrayList<>();
    public Player player;
    public Random rand = new Random();
    public Node rootNode;
    public static int minScore;
    public static int time=1500;

    public int nextMove(State state, int color, String str) {
        //System.out.println("mcts new move");
        if (firstTurn) {
            this.player = State.getCurrentActualPlayer();
            graph = new Tree(new Node(state, null));

            graphs.add(graph);
            minScore = (GridController.gridHeight * GridController.gridWidth) / 2 + 1;
            this.firstTurn=false;
        }
        rootNode = this.graph.getRoot();
        long timeLimit = System.currentTimeMillis() + time; //1000 = 1 sec

        try {
            while (System.currentTimeMillis() < timeLimit) {
                Node promisingNode = selection(rootNode);
                if (!isComplete(promisingNode.getState())) {
                    expansion(promisingNode);
                    for (Node node: promisingNode.getChildren()) {
                        simulateRandomPlayOut(node);
                    }
                } else
                    backPropagation(promisingNode, promisingNode.getState().getScore(player));
            }
        } catch (StackOverflowError e) {
            return bestMove(state);
        }

        return bestMove(state);
    }

    //return the best Line to color after the limited time or if there is a stack over flow
    public int bestMove(State state) {
        Node winnerNode = getBestChild();
        int line =State.findDiffLine(state, winnerNode.getState());
        return (line);
    }

    //this method return a child node of a node that it is fed, based on the highest UCT score
    public Node selection(Node rootNode) {
        Node node = rootNode;
        while (node.getChildren()!=null && node.getChildren().size() != 0 && !isComplete(node.getState()) ) {
            node = Collections.max(node.getChildren(),          //collections.max returns the child node with largest UCT
                    Comparator.comparing(Node::getUctScore));
        }
        return node;
    }

    public void expansion(Node toExpand) {
        if (!toExpand.hasChildren()) {
            toExpand.computeChildrenPruning();
        }
    }

    /**
     * Simulation method for Mcts.
     * To run the tree search with random playout uncomment the first line is the while loop
     * To run with pseudo-random playout uncomment the second line of the while loop. Semi-random playout does not work correctly
     * @param selectedNode found during the selection phase
     */
    public void simulateRandomPlayOut(Node selectedNode) {
        State stateCopy = selectedNode.getState().cloned();
        RuleBased a = new RuleBased();
        while (!isComplete(stateCopy)) {
            //stateCopy=stateCopy.computeAndGetChildren().get((rand.nextInt(stateCopy.getChildren().size())));
            stateCopy=stateCopy.computeAChild(a.nextMove(stateCopy, 1, "")); //simulation using our rule based agent
            stateCopy.display();
        }
        int score= stateCopy.getScore(player);
        //Simulation is a draw
        if (score == minScore && minScore%2==0) {
            score =0;
        }
        //simulation is a draw
        else if (score >=minScore) {
            score=1;
        }else{ // simulation is a loos
            score = -1;
        }

        backPropagation(selectedNode, score);
    }

    public void backPropagation(Node node, int score) {
        if (node.getParent() == null) {
            node.addVisit();
            node.addScore(score);
        } else {
            node.addVisit();
            node.addScore(score);
            backPropagation(node.getParent(), score);
        }

    }

    public boolean isComplete(State state) {
       return state.getLines().size()==0;
    }

    public Node getBestChild() {
        double currentBest=Double.NEGATIVE_INFINITY;
        Node bestChild=null;
        for (Node child : this.rootNode.getChildren()) {
            if (child.getAvg() > currentBest) {
                bestChild = child;
                currentBest = child.getAvg();
            }
        }
        return bestChild;
    }

    public static void setNewRoots(){
        for (Graph graph: graphs){
            graph.setNewRoot();
        }
    }

    public static void resetMcts() {
        graphs = new ArrayList<>();

    }

}