package AI;

import GameTree.State;
import View.Board;
import View.GraphicLine;
import View.Line;
import View.Player;

public abstract class AISolver {

    protected int playerColor;
    protected Player getActualPlayer;
    private final static int cScore = 20;
    private final static int cThree = 15;
    private final static int cTwo = 1;

    protected int evaluationFunction(State board, int color){
       //TODO
        return 0;
    }

    public abstract Line nextMove(State board, int color);
}
