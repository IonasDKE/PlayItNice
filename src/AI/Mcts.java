package AI;
import GameTree.*;
import View.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * Monte Carlo Tree Search
 * 4 steps (methods):
 *  Selection: returns a node that seems promising
 *  Expansion: generates the children of the promising node/state
 *  Simulation: simulates the promising node/state until the end of the game, returns win or loose depending on the simulation
 *  Back propagation: for all the parent of the promising node, increase/decrease the score found in the simulation stage.
 *                   And increase the number of visit by 1.
 */

public class Mcts extends AISolver {
    private Tree tree;
    private Node rootNode;
    private static ArrayList<Tree> trees = new ArrayList<>();
    private static int minScore;
    private boolean firstTurn=true;
    private Player player;
    private Random rand = new Random();

    public Line nextMove(State state, int color) {

        if(firstTurn) {
            this.player= Player.getActualPlayer();
            tree = new Tree(new Node(state, null));
            trees.add(tree);
            minScore = (Launcher.getChosenN()*Launcher.getChosenM())/2 +1;
            System.out.println("first turn");
        }else{
            this.setNewRoots();
            if (tree.getRoot().getState() != State.currentState())
                    tree.setRoot(new Node(State.currentState(), null));
        }

        //this.tree.getRoot().getState().display();
        rootNode=this.tree.getRoot();

        System.out.println();
        //System.out.println("root node "+tree.getRoot() +" children: "+tree.getRoot().getChildren().size());

        long timeLimit = System.currentTimeMillis() + 5000; //1000 = 1 sec
        while (System.currentTimeMillis() < timeLimit) {
            Node promisingNode=selection(rootNode);
            if (!isComplete(promisingNode.getState())) {
                expansion(promisingNode);
            }
            if (promisingNode.getChildren().size()>0) {
                promisingNode=promisingNode.getChildren().get(rand.nextInt(promisingNode.getChildren().size()));
            }
            int score = simulateRandomPlayOut(promisingNode);
            backPropagation(promisingNode,score);
        }
        Node winnerNode = getBestChild();
        //System.out.println("winner node: "+winnerNode);

        Line bestLine=(State.findDiffLine(state.getLines(), winnerNode.getState().getLines()));
        System.out.println("best line id "+bestLine.getid()+" emptiness:"+bestLine.isEmpty());

        this.tree.setNewRoot(winnerNode.getState());
        System.out.println("simulation counter: "+simulationCounter);

        this.firstTurn=false;
        return bestLine;
    }

    //this method return a child node of a node that it is fed, based on the highest UCT score
    private Node selection(Node rootNode) {
        Node node = rootNode;
        while (node.getChildren().size()!=0 && !isComplete(node.getState())){
            node = Collections.max(node.getChildren(),          //collections.max returns the child node with largest UCT
                    Comparator.comparing(Node::getUctScore));
        }
        return node;
    }

    private void expansion(Node toExpand) {
        if (toExpand.hasChildren())
            toExpand.computeChildren();
    }

    public int simulationCounter=0; //for debug
    private int simulateRandomPlayOut(Node selectedNode) {
        State stateCopy = selectedNode.getState().cloned();
        simulationCounter++;
        while (!isComplete(stateCopy)) {
            stateCopy=stateCopy.computeAndGetChildren().get((rand.nextInt(stateCopy.getChildren().size())));
        }
        int score=stateCopy.getScore(player);


        if (score < minScore) {
            score=-1;
            //System.out.println("loose");
        }else if (score > minScore) {
            //System.out.println("win");
            score=1;
            selectedNode.increaseWinNb();
        }else {
            score=0;
        }

        return score;
    }

    private void backPropagation(Node node, int score) {
        if (score==0) { // no need to update score, but still need to increase number of visits
            if (node == tree.getRoot()) {
                node.addVisit();
            } else {
                node.addVisit();
                backPropagation(node.getParent(), score);
            }

        }else {
            if (node == tree.getRoot()) {
                node.addVisit();
                node.addScore(score);
            } else {
                node.addVisit();
                node.addScore(score);
                backPropagation(node.getParent(), score);
            }
        }
    }

    private boolean isComplete(State state) {
        boolean boardIsComplete=true;
        for (Line l: state.getLines()){
            if (l.isEmpty()) {
                boardIsComplete=false;
            }
        }
        return boardIsComplete;
    }

    public Node getBestChild() {
        double currentBest=Double.NEGATIVE_INFINITY;
        Node bestChild=rootNode.getChildren().get(0);
        //System.out.println("root visit number: "+rootNode.getVisitNb());
        for (Node child : rootNode.getChildren()) {
            //System.out.println("current best: "+child.getAvg()+" score: "+child.getScore()+" visit nb: "+child.getVisitNb());
            if (child.getAvg() > currentBest) {
                bestChild=child;
                currentBest=child.getAvg();
                //System.out.println("current best in loop: "+currentBest);

            }
        }
        return bestChild;
    }

    public void setNewRoots(){
        for (Tree tree:trees){
            tree.setNewRoot(State.currentState());
            System.out.println("new root: "+tree.getRoot());
        }
    }

}
