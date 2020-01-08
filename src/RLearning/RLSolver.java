package RLearning;

import GameTree.State;
import View.Line;
import View.Player;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;

public class RLSolver extends Player{

    protected ArrayList<Line> moves = State.currentState().getAvailableMoves();
    private int turn;
    private State state;
    QLearning qLearner;

    public RLSolver(int turn, State state, QLearning learn) {
        this.turn = turn;
        this.state = state;
        qLearner = learn;
    }

    public void reset() {
        state.setPlayable();
    }

    public void move(){
        while (state.getAvailableMoves().size() != 0) {
            System.out.println(state.getAvailableMoves().size());

            System.out.println("filled");
            Line line= qLearner.selectMove(state.getAvailableMoves());

            try {
                State.findLine(line.getid(), State.currentState().getLines()).fill();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void learn() {

    }
}
