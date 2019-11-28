package AI;
import GameTree.*;
import View.*;
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
            tree = new Tree();
            //trees.add(tree);
            firstTurn=false;
            minScore = (Launcher.getChosenN()*Launcher.getChosenM())/2 +1;
        }

        System.out.println();
        System.out.println("root Move");
        //tree.getRoot().getState().display();

        Node findBestMove =null;
        timeLimit= System.currentTimeMillis()+1000; //1000 = 1 sec
        while (System.currentTimeMillis() < timeLimit) {
            findBestMove=selection(tree.getRoot());
            expansion(findBestMove);

        }

        double currentBest=-1000000;

        for (Node child : tree.getRoot().getChildren()) {
            if (child.getScore()/child.getVisitNb() > currentBest) {
                currentBest=child.getScore()/child.getVisitNb();
                findBestMove=child;
                System.out.println("current best : "+currentBest);
            }
        }
        //System.out.println(tree.getRoot().getChildren().get(0).getScore()/tree.getRoot().getChildren().get(0).getVisitNb());

        Line bestLine=(State.findMove(findBestMove.getState().getLines()));
        System.out.println("Best line is : "+bestLine.getid());
        bestLine.fill();
        tree.setRoot(findBestMove);
        tree.deleteParents();
        System.out.println(findBestMove.getParent());
        return bestLine;
    }

    //this method return a child node of a node that it is fed, based on the highest UCT score
    public Node selection(Node rootNode) {
        Node node = rootNode;
        if (node.computeAndGetChildren().size() != 0){                  //while loop just doesn't make sense here
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
        //System.out.println("parent: "+toExpand.getParent()+" score: "+toExpand.getScore()+" visit nb: "+toExpand.getVisitNb()+" children size: "+toExpand.getChildren().size());
        toExpand.getState().computeChildren();
        //System.out.println("state children size "+toExpand.getState().getChildren().size());
        for (State state: toExpand.getState().getChildren()) {
            //System.out.println("in loops");
           toExpand.addChild(new Node(new State(state.cloneLines()),toExpand));
        }
        //System.out.println("children size "+ toExpand.getChildren().size());
        simulateRandomPlayOut(toExpand.getChildren().get(rand.nextInt(toExpand.getChildren().size())));

    }

    public void simulateRandomPlayOut(Node selectedNode) {
        int score=0;
        Line randomLine;
        boolean ourTurn=true;

        State stateCopy = new State(selectedNode.getState().cloned(selectedNode.getState().getLines()));
        ArrayList<Line> lines = stateCopy.getEmptyLines();

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
        //System.out.println("score to add: "+score);
        if (score <minScore) {
            backPropagation(selectedNode, -1);
        }else if (score >=minScore){
            backPropagation(selectedNode, 1);
        }else {
            backPropagation(selectedNode, 0);
        }
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
        //System.out.println("visit : "+node.getVisitNb() +" score : "+node.getScore());
    }

    public boolean isNotComplete(State currentState) {
        boolean allLinesEmpty=false;
        for (Line l: currentState.getLines()){
            if (l.isEmpty()) {
                allLinesEmpty = true;
            }
        }
        return allLinesEmpty;
    }

    public int checkSquare(Line line, boolean ourTurn) {
        int score=0;
        for (Square square: line.getSquares()) {
            //System.out.println("square valence : "+square.getValence());
            if (ourTurn && square.getValence()==1) {
                score++;
                //System.out.println("increase score");
            }
        }
        //System.out.println("return score : "+score);
        return score;
    }


    public static void setNewRoots(){
        for(Tree t : trees){
            t.setNewRoot();
        }
    }
}
