package AI;

import GameTree.State;

import java.util.ArrayList;
import java.util.Random;

public class StupidAI extends AISolver {

public ArrayList<State> children;
    public int nextMove(State board, int color, String str) {
        Random random = new Random();
        children=board.getChildrenStupid();
        System.out.println("children size: "+children.size());
        return State.findDiffLine(board, children.get(random.nextInt(children.size())));
    }
}
