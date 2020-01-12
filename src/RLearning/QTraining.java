package RLearning;

import GameTree.State;
import View.Board;
import View.Line;
import View.Player;

import java.util.ArrayList;

import static Controller.Simulator.setPlayers;
/*
public class QTraining {

    static QLearning agentToBeTrained;
    static RLSolver trainedBot;
    static RandomAgent agent;
    static ArrayList<Player> players = new ArrayList();

    public static QLearning train(State state, int numberOfIterations){

        // Calculate the number of states and move at each game
        int numberOfState = (int)Math.pow(3, state.getAvailableMoves().size());
        int numberOfMoves = state.getAvailableMoves().size();

        // this is what we are going to return
        agentToBeTrained = new QLearning(numberOfState, numberOfMoves);

        // the first bot is the one we want to train
        trainedBot = new RLSolver(1, state, agentToBeTrained);

        agent = new RandomAgent(2, state);

        //TRAINING PART

        for(int i = 0 ; i < numberOfIterations; i++){
            trainedBot.reset();
            agent.reset();
            state.setPlayable();
            /**
             * SIMULATES A GAME , STILL NEEDS THE MOVE() FUNCTION TO BE CORRECT
             *
            while(state.getAvailableMoves().size()!=0){
                trainedBot.move();
                agent.move();
            }
            /**
             * AFTER THE GAME THE AGENT NEEDS TO CALCULATE THE Q VALUES OF THE GAME
             *
            System.out.println("TRAINING THE BEAST");
            trainedBot.learn();
        }
        return agentToBeTrained;
    }

    public static void main(String[] args){
        /**resets the state to 0
         * as if we had another board
         *
        int height = 3;
        int width = 3;
        //TRAINING PART
        players.add(trainedBot);
        players.add(agent);
        //Set the current state to a new game , with two agents
        State.setCurrentState(new State(players, 0));
        // Makes the grid , to allow the coloring of the lines
        Board.makeGrid(height, width);
        // Sets all the line to empty
        for (Line line : State.currentState().getLines()) {
            line.setEmpty(true);
        }
        // sets the states with line and player (could be a clone)
        State state = new State(State.currentState().getLines(),players);

        // training will represent an agent which will have been trained
        QLearning training = train(state, 10000);


        //TESTING PART
        /**
         * write a method which test the 'training' by making it play against other player
         *

    }
}
*/