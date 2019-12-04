package AI;

import GameTree.Node;
import GameTree.State;
import GameTree.Tree;
import View.Board;
import View.GraphicLine;
import View.Line;
import View.Player;

import java.util.ArrayList;
import java.util.Random;

//import static Controller.Controller.completeSquareID;

public abstract class AISolver {

    protected int playerColor;
    protected Player getActualPlayer;
    private final static int cScore = 20;
    private final static int cThree = 15;
    private final static int cTwo = 1;

    protected int evaluationFunction(State board, int color){

        //return completeSquareID(State.currentState().getSquares());
        Random r = new Random();
        return r.nextInt(100-10)+10;


    }
    public abstract Line nextMove(State board, int color);
}
