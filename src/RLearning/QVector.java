package RLearning;

import GameTree.State;

import java.util.*;


public class QVector {
    private int state;
    private int move;

    /**
     * This class allows to make dictionaries -> to store a state with a move
     * @param state represents the id of a STATE
     * @param move represents the id of a move (a given line)
     */
    public QVector(int state, int move){
        this.state = state;
        this.move = move;
    }

    /**
     * @return a move (a line)
     */
    public int getMove() {
        return move;
    }

    /**
     * @param move a line
     */
    public void setMove(int move) {
        this.move = move;
    }

    /**
     * @return the state of the game
     */
    public int getState() {
        return state;
    }
    /**
     * @param state the state of the game
     */
    public void setState(int state) {
        this.state = state;
    }
}
