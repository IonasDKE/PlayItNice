package AI;
import GameTree.*;
import View.*;
import java.util.ArrayList;
import java.util.Random;

public class Mcts extends AISolver {
    private Tree tree;
    private Node rootNode;
    private static ArrayList<Tree> trees = new ArrayList<>();
    private static long timeLimit;
    private Random rand = new Random();
    private static int minScore;
    private boolean firstTurn=true;
    private Player player;

    public Line nextMove(State state, int color) {

        if(firstTurn) {
            this.player= Player.getActualPlayer();
            tree = new Tree(new Node(state, null));
            trees.add(tree);
            minScore = (Launcher.getChosenN()*Launcher.getChosenM())/2 +1;
            System.out.println("first turn");
        }else{
            this.setNewRoots();
        }

        this.tree.getRoot().getState().display();

        Node.resetTotalVisit();
        rootNode=this.tree.getRoot();

        System.out.println();
        System.out.println("root node "+tree.getRoot() +" children: "+tree.getRoot().getChildren().size());

        timeLimit= System.currentTimeMillis()+2000; //1000 = 1 sec
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
        //System.out.println("winner node size: "+winnerNode.getState().getLines().size());
        this.tree.setNewRoot(winnerNode.getState());
        System.out.println("new root after winner node found: "+tree.getRoot());

        winnerNode.deleteParent(firstTurn); //in java when information is not accesible, it's deleted
        this.firstTurn=false;

        Line bestLine=(State.findDiffLine(state.getLines(), winnerNode.getState().getLines()));
        System.out.println("best line id "+bestLine.getid());

        return bestLine;
    }

    //this method return a child node of a node that it is fed, based on the highest UCT score
    private Node selection(Node rootNode) {

        Node node = rootNode;
        while (node.getChildren().size()!=0 && !isComplete(node.getState())){
            node = maxUctNode(node);
        }
        return node;
    }

    //This method is ancilliary to the selection method
    private Node maxUctNode (Node node){
        Node maxUctNode =node.getChildren().get(0);
        for(Node aNode: node.getChildren()){
            if (aNode.getUctScore() > maxUctNode.getUctScore()){
                maxUctNode = aNode;
            }
        }
        return maxUctNode;
    }

    private void expansion(Node toExpand) {
        if (toExpand.hasChildren())
            toExpand.computeChildren();
    }

    private int simulateRandomPlayOut(Node selectedNode) {
        State stateCopy = new State(selectedNode.getState().cloned(selectedNode.getState().getLines()),selectedNode.getState().getPlayerToPlay());

        while (!isComplete(stateCopy)) {
            stateCopy=stateCopy.computeAndGetChildren().get((rand.nextInt(stateCopy.getChildren().size())));
        }
        int score=stateCopy.getScore(player);

        //System.out.println("score to add: "+score);
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
        Node bestChild=null;

        for (Node child : rootNode.getChildren()) {
            //System.out.println("current best: "+child.getAvg());
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
