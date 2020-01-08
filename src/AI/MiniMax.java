package AI;

import GameTree.Node;
import GameTree.State;
import View.Line;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class MiniMax extends AISolver{

    @Override
    public int nextMove(State board, int color, String str) {

        LinkedList<Node> getPossibleMoves = new LinkedList<>();
        LinkedList<Node> backtrack = new LinkedList<>() ;

        //Array List of lines which considers all the possible moves
        ArrayList<Integer> moves;
        State state;
        //takes in the current
        int turn;
        int currentScore;
        long startTime = System.nanoTime();
        long timeOut = 990000000;

        // Creates the root (a move) with the actual player (AI or human)
        Node root = new Node(board, color, null, null);
        getPossibleMoves.add(root);
       // queue.add(null);
        playerColor = color;

        //Do while there are elements on the queue
        do {
            //Checks if the time has passed
            if ((System.nanoTime() - startTime) > timeOut)
                break;
            //Gets the first element on the queue (the root)
            Node current = getPossibleMoves.remove();
            if (current != null) {
                //Adds a new node on the stack (current node)
                backtrack.add(current);
                //Gets the state of the current node
                state = current.getState();
                //Gets the turn of the current node(either 0 or 1)
                turn = current.getTurn();

                //gets the score of the currentState ( current node )
                currentScore = state.getScore(turn);

                //gets the all the available moves from the node(children)
                moves = state.getAvailableMoves();

                //Randomly permutes all the moves
                Collections.shuffle(moves);

                //GEts all the possible lines in the move pull
                for (int i : moves) {
                    //gets the state which is given after every line coloring
                    //We clone the state and invest its children
                    State child = state.computeAChild(i);
                    //gets the score associated with the given move
                    int newScore = child.getScore(turn);
                    //Checks if the score is equal to the score previously computed
                    if (newScore == currentScore) {
                        //Do BFS for the opponent if the score of the child = the current score
                        getPossibleMoves.add(new Node(child, State.inverseTurn(turn), current, i));
                    } else {
                        //Else try the next node for the same player!
                        getPossibleMoves.add(new Node(child, turn, current, i));
                    }
                }

            } else {
                //else return null
                getPossibleMoves.add(null);
            }
        }while(getPossibleMoves.size()!=0);

        //while there are elements on the queue
        //This is where we are evaluating each move
        while(getPossibleMoves.size()!=0) {
            //current is the move we are investigating
            Node current = getPossibleMoves.remove();
            if (current != null)
                //We add the current node to the stack
                backtrack.add(current);
        }

        do{
            //Get the current node and its parent (backtracking more or less)
            Node current = backtrack.removeLast();
            Node parentNode = current.getParent() ;
            //Gets the weight (evaluation function) of the current node
            int weight = current.getWeight();

            //Checks if it is the first iteration
            //And then update the weight of the state
            if(Node.MIN == weight)
                //EVALUATION FUNCTION!!
                current.setWeigth(evaluationFunction(current.getState(), current.getTurn()));
            //Get the weight we have just computed to compare it with the parent's after
            weight = current.getWeight();

            /**
             * IN THIS SECTION I TRY TO BACKTTRACK THE BEST RESULT TO THE ROOT
             * SO I GO TO THE LEAF AND EACH TIME WE GET A BETTER RESULT, THEN THE PARENT BECOMES THIS MOVE
             */
            //If the parent is the AI
            if(parentNode.getTurn()==playerColor){
                //If the parent has an weaker score than the child
                if(parentNode.getWeight()< weight){
                    //Set the parent with the weight of the child
                    parentNode.setWeigth(weight);
                    //If the parent is the root we set the root with this score
                    if(parentNode == root)
                        root.setLine(current.getLine());
                }
            }
            //IF the parent is the human player
            else{
                if(parentNode.getWeight()> weight)
                    //Since we try to MAXIMIZE the win
                    parentNode.setWeigth(weight);
            }
        //DO THIS UNTIL WE REACH THE ROOT!!
        //Which is why we stop at 1
        }   while(backtrack.size()!=1);
        //System.out.println(root.getLine().getid());
        return root.getLine();

    }
}
