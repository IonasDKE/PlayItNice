package AI;

import GameTree.State;
import View.Line;
import View.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class MiniMax extends AISolver {
    private static int maxDepth;
    final static int MIN = -1000000000, MAX = 1000000000;
    private long startMoveTime = 1000000000;
    private long moveTime = 1900000000;
    private long startTime;

    /**
     * IMPORTANT : WITH MINIMAX WITH THIS AMOUNT OF TIME WE CAN REACH DEPTH OF 5
     * @param board the state
     * @param turn represents the turn of the player which is playing (0 or 1)
     * @param st not used here
     * @return the best move using minimax search algo
     */
    @Override
<<<<<<< HEAD
    public Line nextMove(State board, int turn, String st) {
        startTime = System.nanoTime();
        maxDepth = 1;
        Line line = null;
=======
    public int nextMove(State board, int turn, String st) {
        startTime = System.nanoTime();
        maxDepth = 1;
        Integer line = null;
>>>>>>> 2d0ddf53d7dc7c70935bee3733646ba4cb787d00
        //Starts at depth 0
        while (maxDepth <= board.numberOfAvailableMoves()) {
            WeightedEdge weight = startAI(board, turn, 0);
            if((System.nanoTime() - startTime) < moveTime) {
                line = weight.getLine();
            }
            else{
                break;
            }
            maxDepth++;

        }
        //State.findLine(line.getid()).fill();
        //line.fill();
        return line;
    }

<<<<<<< HEAD
        System.out.println("MiniMax Choose line number" + line.getid());
        return line;
    }

    public WeightedEdge startAI(State state, int turn, int depth) {
        System.out.println(depth);
        if ((depth < maxDepth && (System.nanoTime() - startTime) < moveTime)) {
            ArrayList<Line> moves = state.getAvailableMoves();
=======
    public WeightedEdge startAI(State state, int turn, int depth) {
        System.out.println(depth);
        if ((depth < maxDepth && (System.nanoTime() - startTime) < moveTime)) {
            ArrayList<Integer> moves = state.getAvailableMoves();
>>>>>>> 2d0ddf53d7dc7c70935bee3733646ba4cb787d00
            int availableMoves = state.numberOfAvailableMoves();
            int movesLength = moves.size();

            if (availableMoves == 0) {
                return new WeightedEdge(null, evaluationFunction(state, turn));
            }
            //Collections.shuffle(moves);

            /**
             * resets the state
             */

            ArrayList<State> childrenState = state.computeAndGetChildren();
            WeightedEdge[] newEdges = new WeightedEdge[childrenState.size()];

            for (int i = 0; i < childrenState.size(); i++) {
                State newBoard = childrenState.get(i);
<<<<<<< HEAD
                ArrayList<Line> stateLine = state.getLines();
                Line line = State.findDiffLineMinMax(stateLine, childrenState.get(i).getLines());
=======

                int line = State.findDiffLine(state, childrenState.get(i));
>>>>>>> 2d0ddf53d7dc7c70935bee3733646ba4cb787d00
                newEdges[i] = new WeightedEdge(line, evaluationFunction(newBoard, (newBoard.getScore(turn) > state.getScore(turn) ? turn : State.inverseTurn(turn))));
            }

            Arrays.sort(newEdges);
<<<<<<< HEAD
            moves = new ArrayList<Line>();
=======
            moves = new ArrayList<>();
>>>>>>> 2d0ddf53d7dc7c70935bee3733646ba4cb787d00
            if (playerColor != turn) {
                for (int i = 0; i < childrenState.size(); i++) {
                    moves.add(newEdges[i].getLine());
                }
            }
            else{
                for (int i = childrenState.size() - 1; i >= 0; i--)
                    moves.add(newEdges[i].getLine());
            }
            // IF TURN = AI
            if (turn == playerColor) {
                // This is the edge we will return
                WeightedEdge newEdge = new WeightedEdge(null, MIN);

                //computes the children if they do not exist
                //for (State child : state.computeAndGetChildren()) {
                for(int i = 0; i < moves.size(); i ++){
                    State child = state.computeAChild(moves.get(i));
                    //Training edge
                    WeightedEdge wedge;

                    int childScore = child.getScore(state.getActualPlayer());
                    int actualScore = state.getScore(state.getActualPlayer());
                    boolean found = false;
                    if (childScore == actualScore) {
<<<<<<< HEAD
                        wedge = startAI(child, state.getNextTurn(turn), depth + 1);
=======
                        wedge = startAI(child, state.inverseTurn(turn), depth + 1);
>>>>>>> 2d0ddf53d7dc7c70935bee3733646ba4cb787d00
                        found = true;
                    } else {
                        wedge = startAI(child, turn, depth + 1);
                    }

                    int getScore = wedge.getWeight();
                    // Backtracks
                    if (newEdge.getWeight() < getScore) {
                        newEdge.setWeight(getScore);
<<<<<<< HEAD
                        newEdge.setLine(State.findDiffLineMinMax(state.getLines(), child.getLines()));
=======
                        newEdge.setLine(State.findDiffLine(state.getLines(), child.getLines()));
>>>>>>> 2d0ddf53d7dc7c70935bee3733646ba4cb787d00
                    }
                    //if (found)
                    //    if (getScore >= beta)
                    //        return newEdge;

                    //alpha = Math.max(alpha, newEdge.getWeight());

                }
                //System.out.println(newEdge.getLine().getid());
                //State.findLine(newEdge.getLine().getid()).fill();
                return newEdge;
            } else {
                /*WeightedEdge newEdge = new WeightedEdge(null, MAX);

                for (int i = 0; i < moves.size(); i++) {

                    State child = state.computeAndGetChildren().get(i);
                    WeightedEdge pair;
                    int childScore = child.getScore(turn), currentScore = state.getScore(turn);
                    boolean flag = false;
                    if (childScore == currentScore) {
                        pair = startAI(child, State.inverseTurn(turn), depth+1, alpha, beta);
                        flag = true;
                    } else
                        pair = startAI(child, turn, depth+1, alpha, beta);

                    int childUtility = pair.getWeight();
                    if (newEdge.getWeight() > childUtility) {
                        newEdge.setWeight(childUtility);
                        newEdge.setLine(State.findDiffLine(state.getLines(), child.getLines()));
                    }
                    if (flag)
                        if (childUtility <= alpha)
                            return newEdge;

                    beta = Math.min(beta, newEdge.getWeight());
                }
                return newEdge;
            }
                 */
                WeightedEdge newEdge = new WeightedEdge(null, MAX);

                //System.out.println("is not ai");

                //computes the children if they do not exist
                // for (State child : state.computeAndGetChildren()) {
                for(int i = 0; i < moves.size(); i ++) {
                    State child = state.computeAChild(moves.get(i));

                    //State child = state.updateState(moves.get(i), color);
                    //Training edge
                    WeightedEdge wedge;

                    Player p = state.getActualPlayer();
                    int childScore = child.getScore(p);
                    int actualScore = state.getScore(p);
                    boolean found = false;


                    if (childScore == actualScore) {
<<<<<<< HEAD
                        wedge = startAI(child, state.getNextTurn(turn), depth + 1);
=======
                        wedge = startAI(child, state.inverseTurn(turn), depth + 1);
>>>>>>> 2d0ddf53d7dc7c70935bee3733646ba4cb787d00
                        found = true;
                    } else {
                        wedge = startAI(child, turn, depth + 1);
                    }

                    int getScore = wedge.getWeight();
                    // Backtracks
                    if (newEdge.getWeight() > getScore) {
                        newEdge.setWeight(getScore);
<<<<<<< HEAD
                        newEdge.setLine(State.findDiffLineMinMax(state.getLines(), child.getLines()));
=======
                        newEdge.setLine(State.findDiffLine(state, child));
>>>>>>> 2d0ddf53d7dc7c70935bee3733646ba4cb787d00
                    }
                }

                //System.out.println(newEdge.getLine().getid());

                //State.findLine(newEdge.getLine().getid()).fill();
                return newEdge;


            }
        }

        else{
            return new WeightedEdge(null, evaluationFunction(state, turn));
        }
    }

}
