package AI;
import GameTree.*;
import View.*;
import java.util.ArrayList;
import java.util.Random;

public class Mcts extends AISolver{
    private Tree tree;
    private long timeLimit;
    private Random rand = new Random();
    private boolean firstTurn= true;
    public int minScore;

    public Line nextMove(State state, int color) {
        minScore = (Launcher.getChosenN()*Launcher.getChosenM())/2 +1;
        if (firstTurn) {
            tree = new Tree(new Node(state, null));
            firstTurn=false;
        }

        System.out.println("new Move");
        System.out.println();

        Node findBestMove =null;
        timeLimit= System.currentTimeMillis()+1000; //1000 = 1 sec
        while (System.currentTimeMillis() < timeLimit) {
            findBestMove=selection(tree.getRoot());
            expansion(findBestMove);
            simulateRandomPlayOut(findBestMove);

        }

        double currentBest=-100;
        for (Node child:tree.getRoot().getChildren()) {
            //System.out.println("all scores :"+child.getScore()/child.getVisitNb());
            if (child.getScore()/child.getVisitNb() >= currentBest) {
                currentBest=child.getScore()/child.getVisitNb();
                findBestMove=child;
            }
        }

        System.out.println("root children size: "+tree.getRoot().getChildren().size());

        //System.out.println("current best : "+currentBest);
        //System.out.println("best move avg: "+findBestMove.getScore()/findBestMove.getVisitNb());
        Line bestLine=(State.findLine(findBestMove.getState().getLines()));
        System.out.println("Best line is : "+bestLine.getid());

        bestLine.fill();
        tree.setRoot(findBestMove);
        System.out.println(tree.getRoot());
        return bestLine;
    }

    //this method return a child node of a node that it is fed, based on the highest UCT score
    public Node selection(Node rootNode) {
        if (rootNode.getChildren().size()==0) {
            return rootNode;
        }else{
            Node node = rootNode;
            while (node.getChildren().size() != 0) {                  //while loop just doesn't make sense here
                node = maxUctNode(node.getChildren());               //of course you need a while loop!
            }
            return node;
        }
    }

    //This method is ancilliary to the selection method
    public Node maxUctNode (ArrayList<Node> nodes){
        Node maxUctNode = nodes.get(nodes.size()-1);
        for(Node aNode: nodes){
            if (aNode.getUctScore() > maxUctNode.getUctScore()){
                maxUctNode = aNode;
            }
        }
        return maxUctNode;
    }

    public Node expansion(Node toExpand) {
        toExpand.getState().computeChildren();
        for (State state: toExpand.getState().getChildren()) {
            toExpand.addChild(new Node(new State(state.cloneLines()),toExpand));
        }
        System.out.println("children size : "+toExpand.getChildren().size());
        return toExpand.getChildren().get(rand.nextInt(toExpand.getChildren().size()));

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
            if (node.getParent() == null) {
                node.addVisit();
            } else {
                node.addVisit();
                backPropagation(node.getParent(), score);
            }

        }else {
            if (node.getParent() == null) {
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

    public void setNewRoot(State newRootState) {
        for (Node node: this.tree.getRoot().getChildren()){
            if (node.getState() == newRootState)
                this.tree.setRoot(node);

        }
    }

}
