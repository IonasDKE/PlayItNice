package RLearning;

import GameTree.State;
import View.Line;

import java.util.*;

public class QLearning {

    private ActionSelectionStrategy actionSelectionStrategy;

    int numberOfState;
    int numberOfMoves;

    protected QValue q;
    protected QValue alpha;
    protected setQ mod;
    /**
     * @param numberOfState number of state available after the current state
     * @param numberOfMoves number of moves possibles
     */

    public QLearning(int numberOfState, int numberOfMoves, double alpha, double gamma, double Qinit) {
        this.numberOfState = numberOfState;
        this.numberOfMoves = numberOfMoves;
        this.mod = new setQ(numberOfState, numberOfMoves, Qinit);
        this.mod.setA(alpha);
        this.mod.setG(gamma);
        this.actionSelectionStrategy = new EpsilonGreedyActionSelectionStrategy();

    }

    /**
     * @param availableMoves contains all the available moves
     * @return the best move using the Q values
     */
    public Line selectMove(ArrayList<Line> availableMoves) {

        Random rand = new Random();
        Line line = availableMoves.get(rand.nextInt(availableMoves.size()));
        return line;
    }
    //Makes all int to favourize the memory consumption
    public void learnModel(int currentStateID, int nextStateID, int filledLineID, int reward, List<Integer> actionAfterColoring){
        double currentQ = this.mod.getQValue(currentStateID, filledLineID);
        //calculates the alpha value
        double alpha = this.mod.computeAlpha(currentStateID, filledLineID);
        //calculates the gamme value
        double gamma = this.mod.computeGamma();
        //Gets the best out of all the q values for the next state
        //actionAfterColoring is a set of integers which contains all the id of the possible lines
        double bestvalue = this.getBestQ(nextStateID,actionAfterColoring);
        currentQ +=  alpha * (reward + gamma * bestvalue - currentQ);
        this.setQValue(currentStateID, filledLineID, currentQ);
    }



    /**
     * @param nextStateID the id of the state after the move was made
     * @param actionAfterColoring list of id's containing all the lines after the move was made
     * @return best Q out of all the next moves
     */
    private double getBestQ(int nextStateID, List<Integer> actionAfterColoring) {
        IndexValue iv = this.mod.actionWithMaxQAtState(nextStateID, actionAfterColoring);
        return iv.getValue();
    }

    /**
     *
     * @param currentStateID the id of the current state
     * @param filledLineID the id of the move
     * @param currentQ the current q value we have calculated
     */
    private void setQValue(int currentStateID, int filledLineID, double currentQ) {
        this.q.insert(currentStateID, filledLineID, currentQ);
    }


}
