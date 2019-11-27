package Model;

import GameTree.State;
import View.Board;
import View.GraphicLine;
import Controller.*;
import View.Line;
import View.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AlphaBeta extends AISolver {

    private static int maxDepth;
    final static int MIN = -1000000000, MAX = 1000000000;
    private long moveTime = 1900000000;
    private long startTime;


    public Line nextMove(State board, int turn) {
        startTime = System.nanoTime();
        maxDepth = 1;
        Line line = null;
        //board.display();
        while (maxDepth <= board.numberOfAvailableMoves()) {

            //Starts at depth 0
            WeightedEdge weight = startAI(board, turn, 0, MIN, MAX);
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

    public WeightedEdge startAI(State state, int turn, int depth, int alpha, int beta) {

        if ((depth < maxDepth && (System.nanoTime() - startTime) < moveTime)) {
            int availableMoves = state.numberOfAvailableMoves();
            ArrayList<Line> moves = state.getAvailableMoves();
            //int movesLength = moves.size();

            if (availableMoves == 0) {
                return new WeightedEdge(null, evaluationFunction(state, turn));

            }
            //Collections.shuffle(moves);

            // IF TURN = AI
            if (Player.getActualPlayer().isAlpha()) {
                //System.out.println("is AI");
                // This is the edge we will return
                WeightedEdge newEdge = new WeightedEdge(null, MIN);



                    for (State child : state.getChildren()) {
                        //State child = state.updateState(moves.get(i), color);
                        //Training edge
                        WeightedEdge wedge;

                        int childScore = child.getScore(turn);
                        int actualScore = state.getScore(turn);
                        boolean found = false;
                        if (childScore == actualScore) {
                            wedge = startAI(child, 0, depth + 1, alpha, beta);
                            found = true;
                        } else {
                            wedge = startAI(child, turn, depth + 1, alpha, beta);
                        }

                        int getScore = wedge.getWeight();
                        // Backtracks
                        if (newEdge.getWeight() < getScore) {
                            newEdge.setWeight(getScore);
                            newEdge.setLine(State.findMove(state, child));
                        }
                        if (found)
                            if (getScore >= beta)
                                return newEdge;

                        alpha = Math.max(alpha, newEdge.getWeight());

                }
                //System.out.println(newEdge.getLine().getid());
                //State.findLine(newEdge.getLine().getid()).fill();
                return newEdge;
            } else {

            WeightedEdge newEdge = new WeightedEdge(null, MAX);

                System.out.println("is not ai");

                for (State child : state.getChildren()) {


                    //State child = state.updateState(moves.get(i), color);
                    //Training edge
                    WeightedEdge wedge;

                    int childScore = child.getScore(turn);
                    int actualScore = state.getScore(turn);
                    boolean found = false;
                    if (childScore == actualScore) {
                        wedge = startAI(child, 0, depth + 1, alpha, beta);
                        found = true;
                    } else {
                        wedge = startAI(child, turn, depth + 1, alpha, beta);
                    }

                    int getScore = wedge.getWeight();
                    // Backtracks
                    if (newEdge.getWeight() > getScore) {
                        newEdge.setWeight(getScore);
                        newEdge.setLine(State.findMove(state, child));
                    }
                    if (found)
                        if (getScore <= alpha)
                            return newEdge;

                    beta = Math.min(beta, newEdge.getWeight());
                }

            //System.out.println(newEdge.getLine().getid());

            //State.findLine(newEdge.getLine().getid()).fill();
            return newEdge;

         }
        }

     else {
        // System.out.println("blablabla");
        return new WeightedEdge(null, evaluationFunction(state, turn));
        }
    }
}
