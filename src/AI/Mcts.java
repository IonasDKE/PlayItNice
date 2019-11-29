package AI;
import GameTree.*;
import View.*;
import Controller.*;
import java.util.ArrayList;
import java.util.Random;

public class Mcts extends AISolver {
    private  Tree tree;
    private static ArrayList<Tree> trees = new ArrayList<>();
    //private static AISolver solver;
    private static long timeLimit;
    private Random rand = new Random();
    private boolean firstTurn= true;
    public  static int minScore;

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

    }*/

    public Line nextMove(State state, int color) {
        if(firstTurn) {
            tree = new Tree(new Node(state, null));
            trees.add(tree);
            firstTurn=false;
            minScore = (Launcher.getChosenN()*Launcher.getChosenM())/2 +1;
        }
        Node.resetTotalVisit();

        System.out.println();
        System.out.println("root Move");
        System.out.println("root node "+tree.getRoot() +" children: "+tree.getRoot().getChildren().size());
        //tree.getRoot().getState().display();

        timeLimit= System.currentTimeMillis()+1000; //1000 = 1 sec
        while (System.currentTimeMillis() < timeLimit) {
            Node promisingNode=selection(tree.getRoot());
            if (isNotComplete(promisingNode.getState())) {
                expansion(promisingNode);
            }
            if (promisingNode.getChildren().size()>0) {
                //System.out.println("in if 2");
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

        Node winnerNode = getBestChild();
        tree.setRoot(winnerNode);
        tree.deleteRootParent();
        Line bestLine=(State.findMove(winnerNode.getState().getLines()));
        //System.out.println("Best line is : "+bestLine.getid());
        bestLine.fill();
        return bestLine;
    }

    //this method return a child node of a node that it is fed, based on the highest UCT score
    public Node selection(Node rootNode) {
        Node node = rootNode;
        while (node.getChildren().size() != 0 && !isNotComplete(node.getState())){
            node = maxUctNode(node);
        }
        return node;
    }

    //This method is ancilliary to the selection method
    public Node maxUctNode (Node node){
        Node maxUctNode =null;
        for(Node aNode: node.getChildren()){
            if (aNode.getUctScore() > maxUctNode.getUctScore()){
                maxUctNode = aNode;
            }
        }
        return maxUctNode;
    }

    public void expansion(Node toExpand) {
        if (toExpand.hasChildren())
            toExpand.computeChildren();
    }

    public int simulateRandomPlayOut(Node selectedNode) {
        State stateCopy = new State(selectedNode.getState().cloned(selectedNode.getState().getLines()));
        ArrayList<Line> lines = stateCopy.getEmptyLines();

        int score=0;
        Line randomLine;
        boolean ourTurn=true;

        while (isNotComplete(stateCopy)) {
            //System.out.println("lines size : "+lines.size());
            randomLine=lines.get(rand.nextInt(lines.size()));
            score += checkSquare(randomLine, ourTurn);
            randomLine.fillNoEffect();
            lines.remove(randomLine);

            if (ourTurn){
                ourTurn=false;
            }else
                ourTurn=true;
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

    public boolean isNotComplete(State state) {
        boolean boardInProgress=false;
        for (Line l: state.getLines()){
            if (l.isEmpty()) {
                boardInProgress = true;
            }
        }
        return boardInProgress;
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

        for (Node child : this.tree.getRoot().getChildren()) {
            if (child.getAvg() > currentBest) {
                bestChild=child;
                currentBest=child.getAvg();
            }
        }
        return bestChild;
    }


    public static void setNewRoots(){
        for(Tree t : trees){
            t.setNewRoot();
        }
    }
}
