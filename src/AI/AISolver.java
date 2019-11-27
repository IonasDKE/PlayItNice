package AI;

import GameTree.State;
import View.*;



public abstract class AISolver {

    protected int playerColor;
    protected Player getActualPlayer;
    private final static int cScore = 20;
    private final static int cThree = 15;
    private final static int cTwo = 1;

    public int evaluationFunction(State board, int color){
        int score;
        if(playerColor==0){
            score = cScore * Player.getPlayers().get(0).getScore() - cScore * Player.getPlayers().get(1).getScore();
        }
        else{
            score = cScore * Player.getPlayers().get(1).getScore() - cScore * Player.getPlayers().get(0).getScore();
        }
        if(playerColor == color){
            score += cThree * Math.random()*3 - cTwo * Math.random()*2;
        }
        else{
            score -= cThree * Math.random()*3 - cTwo * Math.random()*2;

        }
        return score;
    }

    public abstract Line nextMove(State board, int color);

    public abstract void setNewRoot(State state);

}
