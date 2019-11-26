package AI;
import GameTree.*;
import View.*;
import java.util.ArrayList;
import java.util.Random;

public class Mcts extends AISolver{
    public int player;
    private Tree tree;
    private long timeLimit;
    private Random rand = new Random();

    public Line nextMove(State state, int color) {
        System.out.println("new Move");
        State currentState=state.currentState();
        Node tmp =null;
        tree = new Tree(new Node(state, null));
        timeLimit= System.currentTimeMillis()+1000; //1000 = 1 sec
        while (System.currentTimeMillis() < timeLimit) {
            tmp=selection(tree.getRoot());
            expansion(tmp);
            simulateRandomPlayOut(tmp);

        }
        double currentBest=0;
        Node bestNode=null;

        for (Node child:tree.getRoot().getChildren()) {
            if (child.getScore()/child.getVisitNb() > currentBest) {
                currentBest=child.getScore()/child.getVisitNb();
                bestNode=child;
            }
        }

        Line bestLine=(State.findLine(currentState.getLines(), bestNode.getState().getLines()));
        bestLine.fill();
        return bestLine;
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

    public Node expansion(Node toExpand) {
        for (State state: toExpand.getState().getChildren()) {
            toExpand.getChildren().add(new Node(state,toExpand));
        }
        return toExpand.getChildren().get(rand.nextInt(toExpand.getChildren().size()));

    }

    public void simulateRandomPlayOut(Node selectedNode) {
        int score=0;
        Line randomLine;
        boolean ourTurn=true;

        State stateCopy = new State(selectedNode.getState().cloneLines(), selectedNode.getState().getSquares());
        ArrayList<Line> lines = stateCopy.getEmptyLines();

        while (isNotComplete(stateCopy)) {
            randomLine=lines.get(rand.nextInt(lines.size()));
            stateCopy.fillLine(randomLine);
            lines.remove(randomLine);
            score +=checkSquare(randomLine, ourTurn);

            if (ourTurn){
                ourTurn=false;
            }else
                ourTurn=true;
        }

        if (score <4) {
            backPropagation(selectedNode, -1);
        }else if (score >=4){
            backPropagation(selectedNode, 1);
        }else
            backPropagation(selectedNode, 0);

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

    public boolean isNotComplete(State currentState) {
        boolean allLinesEmpty=false;
        for (Line l: currentState.getLines()){
            if (l.isEmpty())
                allLinesEmpty=true;
        }
        return allLinesEmpty;
    }

    public int checkSquare(Line line, boolean ourTurn) {
        int score=0;
        for (Square square: line.getSquares()) {
            if (square.isClaimed()&& ourTurn)
                score++;
        }
        return score;
    }

}
