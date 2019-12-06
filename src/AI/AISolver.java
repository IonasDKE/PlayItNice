package AI;

import GameTree.State;
import View.*;

import java.util.concurrent.TimeUnit;


public abstract class AISolver {

    protected int playerColor;
    protected Player getActualPlayer;
    private final static int cScore = 20;
    private final static int cThree = 15;
    private final static int cTwo = 1;
    int counter =0;

    /**
     * This should work
     * @param board
     * @param color
     * @return score of an evaluation function
     */
    public int evaluationFunction(State board, int color){
        int score;
        if(playerColor==1) {
            score = cScore * board.getScore(0) - cScore * board.getScore(1);
            //System.out.println("Red score");}
        }
        else{
                //AI HERE
                score = cScore * board.getScore(1) - cScore * board.getScore(0);
                // System.out.println("Blue score "+ score) ;}
            }
        if(playerColor == color) {
            System.out.println(counter + " board valence three: " + board.getValence(1));
            counter++;
            score += cThree * board.getValence(1) - cTwo * board.getValence(2);
        }else
            score -= cThree * board.getValence(1) - cTwo * board.getValence(2);
        return score;
    }

    /**
     * Creates a next move for the player
     * @param board the state 'next move'
     * @param color takes the turn ( which player is playing
     */
    public abstract Line nextMove(State board, int color);

    public void setNewRoot(State state) {

    }

}
