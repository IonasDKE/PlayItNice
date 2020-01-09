package RLearning;

import GameTree.State;
import View.Line;
import View.Player;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;

public class RLSolver extends Player{

    protected int [] reward = new int[3];
    protected ArrayList<Line> moves = State.currentState().getAvailableMoves();
    private int turn;
    private State state;
    QLearning qLearner;

    public RLSolver(int turn, State state, QLearning learn) {
        //both player draws
        //this needs to be negative other wise q value might converge
        reward[0] = -10;
        //Qplayer loses
        reward[1] = -100;
        //QPlayer wons
        reward[2] = 100;

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

        //    GETS THE PLAYER WHO WON THE GAME
        Player winner = state.getActualPlayer();
        //
        int winnerIndex = winner.checkPlayerReward();
        int rewardValue = reward[winnerIndex];

        for(Line move : state.getAvailableMoves()){
            move.reward = rewardValue;
            //takes in the current state(before the agent makes the move)
            //             next state(after the agent makes the given move)
            //             the action of the agent (fill)
            //              the reward associated with that move
            qLearner.learnModel(move.currentState, move.nextState, move.fillId, move.reward, null);
        }

        /**
         *
         */
    }
}
