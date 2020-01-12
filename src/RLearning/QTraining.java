package RLearning;

import Controller.Controller;
import Controller.GridController;
import GameTree.State;
import View.Board;
import View.Player;

import java.util.ArrayList;

import java.util.concurrent.TimeUnit;

public class QTraining {

    static QLearning agentToBeTrained;
    static ArrayList<Player> players = new ArrayList();
    static Player trainedBot;
    static Player agent;
    public static int height = 3;
    public static int width = 3;
    static int countTrainedBot=0;
    static int countRandomBot=0;
    static int countDraws=0;

    public static QLearning train(State state, int numberOfIterations){

        // Calculate the number of states and move at each game
        // this is what we are going to return

        //TRAINING PART
        for(int i = 0 ; i < numberOfIterations; i++){
            State.currentState().setPlayable();
            trainedBot.setScore(0);
            agent.setScore(0);
            /*
             * SIMULATES A GAME , STILL NEEDS THE MOVE() FUNCTION TO BE CORRECT
            */
            while(State.currentState().getAvailableMoves().size()!=0){
                //Selects the move that the AI is goint to make
                trainedBot.move();
                 System.out.println(trainedBot.getScore());
                //Selects the move that the random solver will pick
                agent.move();
                System.out.println(State.currentState().getLines().size());
            }
            /*
             * AFTER THE GAME THE AGENT NEEDS TO CALCULATE THE Q VALUES OF THE GAME

             */
            trainedBot.learn(width, height);
        }
        System.out.println("Trained Bot: " + countTrainedBot);
        System.out.println("Random Bot: " + countRandomBot);
        System.out.println("Draws: " + countDraws);
        return agentToBeTrained;
    }

    private static void checkWinners(State state) {
        if(state.getScore(trainedBot)>state.getScore(agent)){
            System.out.println("Trained Bot Won");
            countTrainedBot++;
        }
        else if(state.getScore(trainedBot)==state.getScore(agent)){
            System.out.println("Draw");
            countDraws++;
        }
        else{
            System.out.println("Random Won!");
            countRandomBot++;
        }
    }

    public static void main(String[] args){
        /**resets the state to 0
         * as if we had another board

         */
        // this is what we are going to return
        agentToBeTrained = new QLearning(100, 1000, 0.1D, 0.7D, 0.5);

        // the first bot is the one we want to train
        trainedBot = new Player("Trained Bot", agentToBeTrained);
        agent = new Player("Random Bot", null);

        //TRAINING PART
        players.add(trainedBot);
        players.add(agent);
        //Set the current state to a new game , with two agents
        State.setCurrentState(new State(players, 0));
        // Makes the grid , to allow the coloring of the lines
        Board.makeGrid(width, height);

        // Sets all the line to empty
        //State.currentState().setLines(GridController.getLinesIDs());
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