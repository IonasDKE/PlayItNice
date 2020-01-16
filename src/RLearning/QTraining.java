package RLearning;

import Controller.*;
import GameTree.State;
import View.Board;
import View.Player;
import javafx.scene.paint.Color;
import AI.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


import java.util.concurrent.TimeUnit;

public class QTraining {

    static QLearning agentToBeTrained;
    static ArrayList<Player> players = new ArrayList();
    static Player trainedBot;
    static Player agent;
    public static int height = 2;
    public static int width = 2;
    static int countTrainedBot=0;
    static int countRandomBot=0;
    static int countDraws=0;

    public static QLearning train(State state, int numberOfIterations) throws IOException {

        // Calculate the number of states and move at each game
        // this is what we are going to return
        long startingTime = System.currentTimeMillis();
        //TRAINING PART
        for(int i = 0 ; i < numberOfIterations; i++){
            State.currentState().setPlayable();
            trainedBot.setScore(0);
            agent.setScore(0);
            /*
             * SIMULATES A GAME , STILL NEEDS THE MOVE() FUNCTION TO BE CORRECT
            */

            while(!Run.checkEnd()){
                for (int j = 0; j < players.size(); j++){
                    if (State.getCurrentActualPlayer() == trainedBot){
                        trainedBot.move();
                    }
                    else {
                        int line = agent.aiPlay2();
                        Controller.updateTurn(line, State.currentState());
                    }
                }
                /*
                //Selects the move that the AI is goint to make
                trainedBot.move();
                // System.out.println("agent score " +trainedBot.getScore());
                //Selects the move that the random solver will pick
                agent.move();
               // System.out.println("line size:   "+State.currentState().getLines().size());
                System.out.println("empty lines: " + state.getAvailableMoves());

                 */
            }
            /*
             * AFTER THE GAME THE AGENT NEEDS TO CALCULATE THE Q VALUES OF THE GAME

             */
            trainedBot.learn(width, height);
            checkWinners(State.currentState());
            state.setLines(GridController.getLinesIds());
            State.currentState().setLines(GridController.getLinesIds());
            System.out.println("Game "+i);
            System.out.println();
        }

        System.out.println("Trained Bot: " + countTrainedBot);
        System.out.println(agent.getName() + " Bot: " + countRandomBot);
        System.out.println("Draws: " + countDraws);
        long endingTime = (System.currentTimeMillis() - startingTime) / 1000;
        System.out.println(("Time taken: " + endingTime));
        return agentToBeTrained;
    }

    private static void checkWinners(State state) {
        if(state.getScore(trainedBot)>state.getScore(agent)){
            System.out.println("The bot-in-training Won!");
            countTrainedBot++;
        }
        else if(state.getScore(trainedBot)==state.getScore(agent)){
            System.out.println("Draw");
            countDraws++;
        }
        else{
            System.out.println("The other AI won!");
            countRandomBot++;
        }
    }

    public static void main(String[] args) throws IOException {
        /**resets the state to 0
         * as if we had another board

         */
        // this is what we are going to return
        agentToBeTrained = new QLearning(100, 1000, 0.1D, 0.7D, 0.5);

        // the first bot is the one we want to train
        trainedBot = new Player("Trained Bot", agentToBeTrained);
        agent = new Player(Color.RED,"Rule Based AI","Rule Based");
        //agent = new Player("Random Bot", null);
        agent.setSolver();

        //TRAINING PART
        players.add(trainedBot);
        players.add(agent);

        //Set the current state to a new game , with two agents
        State.setCurrentState(new State(players, 0));
        // Makes the grid , to allow the coloring of the lines
        Board.makeGrid(width, height);

        // Sets all the line to empty
        //State.currentState().setLines(GridController.getLinesIds());
        // sets the states with line and player (could be a clone)
        State state = new State(State.currentState().getLines(),players);

        // training will represent an agent which will have been trained
        QLearning training = train(state, 2);

        //TESTING PART
        /**
         * write a method which test the 'training' by making it play against other player
         */

    }
}
