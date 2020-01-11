package RLearning;

import GameTree.State;
import View.Board;
import View.Line;
import View.Player;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static Controller.Simulator.setPlayers;

public class QTraining {

    static QLearning agentToBeTrained;
  //  static RLSolver trainedBot;
  //  static RandomAgent agent;
    static ArrayList<Player> players = new ArrayList();
    static Player trainedBot;
    static Player agent;
    final static int height = 3;
    final static int width = 3;

    public static QLearning train(State state, int numberOfIterations){

        // Calculate the number of states and move at each game
        int numberOfState = (int)Math.pow(3, state.getAvailableMoves().size());
        int numberOfMoves = state.getAvailableMoves().size();

        // this is what we are going to return

        //TRAINING PART

        for(int i = 0 ; i < numberOfIterations; i++){
            state.setPlayable();
            trainedBot.setScore(0);
            agent.setScore(0);
            /**
             * SIMULATES A GAME , STILL NEEDS THE MOVE() FUNCTION TO BE CORRECT
             */
            while(state.getAvailableMoves().size()!=0){
                trainedBot.move();
                agent.move();
            }
            /**
             * AFTER THE GAME THE AGENT NEEDS TO CALCULATE THE Q VALUES OF THE GAME
             */
            System.out.println(state.getScore(0) + " " + i);
            trainedBot.learn(width, height);
        }
        return agentToBeTrained;
    }

    public static void main(String[] args){
        /**resets the state to 0
         * as if we had another board
         */

        // this is what we are going to return
        agentToBeTrained = new QLearning(100, 1000, 0.1D, 0.7D, 0.1D);

        // the first bot is the one we want to train
        trainedBot = new Player(agentToBeTrained);
        agent = new Player(null);

        //TRAINING PART
        players.add(trainedBot);
        players.add(agent);
        //Set the current state to a new game , with two agents

        State.setCurrentState(new State(players, 0));
        System.out.println(State.getCurrentActualPlayer());
        // Makes the grid , to allow the coloring of the lines
        Board.makeGrid(width, height);
        // Sets all the line to empty
        for (Line line : State.currentState().getLines()) {
            line.setEmpty(true);
        }
        // sets the states with line and player (could be a clone)
        State state = new State(State.currentState().getLines(),players);
        System.out.println(state.getActualPlayer());

        // training will represent an agent which will have been trained
        QLearning training = train(state, 1000000);


        //TESTING PART
        /**
         * write a method which test the 'training' by making it play against other player
         */

    }
}
