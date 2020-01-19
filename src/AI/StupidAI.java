package AI;

import GameTree.State;

import java.util.ArrayList;
import java.util.Random;
import View.Line;

public class StupidAI extends AISolver {

public ArrayList<State> children;
    public int nextMove(State board, int color, String str) {
        Random random = new Random();
        children=board.getChildrenStupid();
        System.out.println("stupid AI display: ");
        //board.display();
        State state = children.get(random.nextInt(children.size()));
        //state.display();
        int line = State.findDiffLine(board, state);
        //System.out.println("Stupid Line id: "+line);
        return line;
    }
}
