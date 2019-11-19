package AI;

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


    public Line nextMove(State board, int color) {

        maxDepth = 1;
        Line line = null;
        board.display();
        while (maxDepth <= board.numberOfAvailableMoves()) {

            //Starts at depth 0
            WeightedEdge weight = startAI(board, color, 0, MIN, MAX);
            line = weight.getLine();
            maxDepth++;

        }
        line.fill();
        return line;

    }

    public WeightedEdge startAI(State state, int color, int depth, int alpha, int beta) {

        if (depth < maxDepth) {
            int availableMoves = state.numberOfAvailableMoves();
            ArrayList<Line> moves = state.getAvailableMoves();
            int movesLength = moves.size();

            if (availableMoves == 0) {
                return new WeightedEdge(null, evaluationFunction(state, color));
            }
            Collections.shuffle(moves);

            // IF TURN = AI
            if (Player.getActualPlayer().isAlpha()) {

                // This is the edge we will return
                WeightedEdge newEdge = new WeightedEdge(null, MIN);

                for (int i = 0; i < movesLength; i++) {

                    for (State child : state.getChildren()) {
                        //State child = state.updateState(moves.get(i), color);
                        //Training edge
                        WeightedEdge wedge;

                        int childScore = child.getScore(color);
                        int actualScore = state.getScore(color);
                        boolean found = false;
                        if (childScore == actualScore) {
                            wedge = startAI(child, 0, depth + 1, alpha, beta);
                            found = true;
                        } else {
                            wedge = startAI(child, color, depth + 1, alpha, beta);
                        }

                        int getScore = wedge.getWeight();
                        // Backtracks
                        if (newEdge.getWeight() < getScore) {
                            newEdge.setWeight(getScore);
                            newEdge.setLine(moves.get(i));
                        }
                        if (found)
                            if (getScore >= beta)
                                return newEdge;

                        alpha = Math.max(alpha, newEdge.getWeight());
                    }
                }
                newEdge.getLine().fill();
                return newEdge;
            }
            else{

            }
                WeightedEdge newEdge = new WeightedEdge(null, MAX);

                for (int i = 0; i < movesLength; i++) {

                    for(State child : state.getChildren()) {


                        //State child = state.updateState(moves.get(i), color);
                        //Training edge
                        WeightedEdge wedge;

                        int childScore = child.getScore(color);
                        int actualScore = state.getScore(color);
                        boolean found = false;
                        if (childScore == actualScore) {
                            wedge = startAI(child, 0, depth + 1, alpha, beta);
                            found = true;
                        } else {
                            wedge = startAI(child, color, depth + 1, alpha, beta);
                        }

                        int getScore = wedge.getWeight();
                        // Backtracks
                        if (newEdge.getWeight() > getScore) {
                            newEdge.setWeight(getScore);
                            newEdge.setLine(moves.get(i));
                        }
                        if (found)
                            if (getScore <= alpha)
                                return newEdge;

                        beta = Math.min(beta, newEdge.getWeight());
                    }
            }
                     newEdge.getLine().fill();
                return newEdge;

        }

     else {
        return new WeightedEdge(null, evaluationFunction(state, color));
        }
    }
}
