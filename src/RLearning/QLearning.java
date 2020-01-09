package RLearning;

import GameTree.State;
import View.Line;

import java.util.*;

public class QLearning {

    int numberOfState;
    int numberOfMoves;

    protected QValue q;
    /**
     * @param numberOfState number of state available after the current state
     * @param numberOfMoves number of moves possibles
     */

    public QLearning(int numberOfState, int numberOfMoves) {
        this.numberOfState = numberOfState;
        this.numberOfMoves = numberOfMoves;
    }

    /**
     * @param availableMoves contains all the available moves
     * @return the best move using the Q values
     */
    public Line selectMove(ArrayList<Line> availableMoves) {

        Random rand = new Random();
        return availableMoves.get(rand.nextInt(availableMoves.size()));
    }
    //Makes all int to favourize the memory consumption
    public void learnModel(int currentStateID, int nextStateID, int filledLineID, int reward, List<Integer> actionAfterColoring){
        double currentQ = this.getQValue(currentStateID, filledLineID);
        //calculates the alpha value
        double alpha = 0;
        //calculates the gamme value
        double gamma = 0;
        //Gets the best out of all the q values for the next state
        //actionAfterColoring is a set of integers which contains all the id of the possible lines
        double bestvalue = getBestQ(nextStateID,actionAfterColoring);
        currentQ +=  alpha * (reward + gamma * bestvalue - currentQ);
        this.setQValue(currentStateID, filledLineID, currentQ);
    }

    /**
     * @param nextStateID the id of the state after the move was made
     * @param actionAfterColoring list of id's containing all the lines after the move was made
     * @return best Q out of all the next moves
     */
    private double getBestQ(int nextStateID, List<Integer> actionAfterColoring) {
        return 0;
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

    /**
     * @param stateId the id of the current state
     * @param moveId the id of the line which is going to be colored
     * @return
     */
    int getQValue(int stateId, int moveId){
        //Returns the Q value
        return 0;
    }

}
