package AI;
import GameTree.*;
import View.*;

import java.util.ArrayList;
import java.util.Random;

public class Mcts extends AISolver {
    private  Tree tree;
    private Node rootNode;
    private static ArrayList<Tree> trees = new ArrayList<>();
    //private static AISolver solver;
    private static long timeLimit;
    private Random rand = new Random();
    private static int minScore;

   /* private void setMcts() {
        State clonedCurrentState = new State(State.currentState().getLines());
        System.out.println();
        System.out.println("clonedCurrentState = ");

        clonedCurrentState.display();
        if (firstTurn) {
            tree = new Tree();
            trees.add(tree);
            firstTurn=false;
        }
        //change the root at the end of human turn in the Mcts
        //solver.nextMove(state, turn);

    }
    private boolean mctsFirstIteration=true;
    public void mcts() {
        if (mctsFirstIteration) {
            solver.nextMove(State.currentState(), Integer.parseInt(name));
            mctsFirstIteration=false;
        }else {
            solver.setNewRoots(State.currentState());
            solver.nextMove(State.currentState(), Integer.parseInt(name));
        }

    }*/
   private boolean mctsFirstIteration=true;

    public Line nextMove(State state, int color) {

        if(mctsFirstIteration) {
            tree = new Tree(new Node(state, null));
            trees.add(tree);
            minScore = (Launcher.getChosenN()*Launcher.getChosenM())/2 +1;
            System.out.println("first turn");
            mctsFirstIteration = false;
        }else{
            this.setNewRoots(state);
        }

        Node.resetTotalVisit();
        rootNode=this.tree.getRoot();

        System.out.println();
        System.out.println("root Move");
        //System.out.println("root node "+tree.getRoot() +" children: "+tree.getRoot().getChildren().size());
        //tree.getRoot().getState().display();

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

            //System.out.println("score to add: "+score);
            if (score <minScore) {
                backPropagation(promisingNode, -1);
                //System.out.println("loose");
            }else if (score >=minScore) {
                //System.out.println("win");
                backPropagation(promisingNode, 1);
                promisingNode.increaseWinNb();
            }else {
                backPropagation(promisingNode, 0);
            }
        }

        this.tree.getTreeSize(rootNode);
        System.out.println("tree size "+tree.treeSize);
        System.out.println("simulation nb :" +simulationCounter);

        Node winnerNode = getBestChild();
        System.out.println("winner node size: "+winnerNode.getState().getLines().size());
        this.tree.setRoot(winnerNode);

        if (!mctsFirstIteration) {
            winnerNode.setParent(null); //in java when information is not accesible, it's deleted
            //tree.deleteRootParent();
        }

        for (Line l :winnerNode.getState().getLines())
            System.out.println(l.isEmpty()+" id: "+l.getid());

        Line bestLine=(State.findDiffLine(state.getLines(), winnerNode.getState().getLines()));
        System.out.println("best line id "+bestLine.getid());

        return bestLine;
    }

    //this method return a child node of a node that it is fed, based on the highest UCT score
    public Node selection(Node rootNode) {

       /* if (rootNode.getChildren().size()==0) {
            return rootNode;
        }*/
            Node node = rootNode;
            while (node.getChildren().size() != 0 && isNotComplete(node.getState())) {                  //while loop just doesn't make sense here
                node = maxUctNode(node.getChildren());                                            //of course you need a while loop!
            

        }
        return node;
    }

    //This method is ancilliary to the selection method
    public Node maxUctNode (Node node){
        Node maxUctNode =node.getChildren().get(0);
        for(Node aNode: node.getChildren()){
            if (aNode.getUctScore() > maxUctNode.getUctScore()){
                maxUctNode = aNode;
            }
        }
        return maxUctNode;
    }

    public void expansion(Node toExpand) {

       == if (!toExpand.hasChildren())
            toExpand.computeChildren();

    public int simulationCounter=0;
    public int simulateRandomPlayOut(Node selectedNode) {
        simulationCounter++;
        State stateCopy = new State(selectedNode.getState().cloned(selectedNode.getState().getLines()));
        ArrayList<Line> lines = stateCopy.getEmptyLines();
        for (Line line: stateCopy.getLines()){
            //System.out.println(line.getid()+ " emptiness :"+line.isEmpty());
        }
        //System.out.println();
        int lastScore=0;
        int score=0;
        Line randomLine;
        boolean ourTurn=true;

        while (!isComplete(stateCopy)) {
            lastScore=score;
            //System.out.println("lines size : "+lines.size());
            randomLine=lines.get(rand.nextInt(lines.size()));
            score += checkSquare(randomLine, ourTurn);
            randomLine.setEmpty(false);
            lines.remove(randomLine);

            if (lastScore==score) {
                if (ourTurn) {
                    ourTurn = false;
                } else
                    ourTurn = true;
            }
        }
        return score;
    }

    public void backPropagation(Node node, int score) {
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

    public boolean isComplete(State state) {
        boolean boardIsComplete=true;
        for (Line l: state.getLines()){
            if (l.isEmpty()) {
                boardIsComplete=false;
            }
        }
        return boardIsComplete;
    }

    public int checkSquare(Line line, boolean ourTurn) {
        int score=0;
        for (Square square: line.getSquares()) {
            if (ourTurn && square.getValence()==1) {
                score++;
            }
        }
        return score;
    }

    public Node getBestChild() {
        double currentBest=Double.NEGATIVE_INFINITY;
        Node bestChild=null;
        int counterDebugg=0;

        for (Node child : rootNode.getChildren()) {
            System.out.println("current best: "+child.getAvg()+" counter: "+counterDebugg++);
            if (child.getAvg() > currentBest) {
                bestChild=child;
                currentBest=child.getAvg();
                //System.out.println("current best in loop: "+currentBest);

            }
        }
        return bestChild;
    }

    public void setNewRoots(State newRootState){
        for (Node node: this.tree.getRoot().getChildren()){
            if (node.getState() == newRootState)
                this.tree.setRoot(node);

        }
    }

}
