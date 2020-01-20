package Controller;

import AI.Mcts;
import GameTree.State;
import View.Board;
import View.*;
import View.Player;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static Controller.Controller.countClaimedSquare;
import static java.lang.System.out;

/**
 * This class is made to test our AI's
 * Choose two AIs that will play against each other over n many games
 *
 * You can choose which AI is going to play in the setPLayers class
 * And the number of game in the for loop in simulate
 */

public class Run {
    //simulation parameters
    static final int HEIGHT = 5;
    static final int WIDTH = 5;
    static final int NUM_GAMES = 50;


    // this ArrayList collects the score of player 0
    public static ArrayList<Integer> scores = new ArrayList<>();
    // this ArrayList collects the wins of player 1
    public static ArrayList<Integer> wins = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Line.simulation=true;
        Line.runTesting=false;
        //set gridsize here
        GridController.setGridHeightWidth(HEIGHT,WIDTH);

        try {
            State.setCurrentState(new State(setPlayers(), 0));
            Board.makeGrid(GridController.gridWidth,GridController.gridHeight);
            simulate();

            out.println("score size " + scores.size());
            writeOnTxt(scores, wins);

        } catch (OutOfMemoryError e) {
            out.println("out of memory");
            writeOnTxt(scores, wins);
        }

    }

    public static ArrayList<Player> setPlayers() {
        ArrayList<Color> players = new ArrayList<>();
        players.add(Color.BLUE);
        players.add(Color.CHOCOLATE);

        ArrayList<Player> currentPlayers = new ArrayList<>();
        Player a = new Player(Color.CHOCOLATE, Integer.toString(1), "Rule BasedNoDD");
        currentPlayers.add(a);
        a.setSolver();

        Player b = new Player(Color.RED, Integer.toString(2), "Rule Based");
        currentPlayers.add(b);
        b.setSolver();

        return currentPlayers;
    }

    public static String simulate() throws IOException {

        for (int i = 1; i < NUM_GAMES + 1; i++) {
            out.println();
            out.println("New Simulation #"+i +" : " + State.getCurrentPlayers().get(0).getAiType() + " vs. " +  State.getCurrentPlayers().get(1).getAiType() );
            //State.currentState().display();
            for (Line line : GridController.getLines()) {
                line.setEmpty(true);
            }
            State.setCurrentState(new State(GridController.getLinesIds(), setPlayers()));
            State.currentState().setPlayers(setPlayers(), 0);
            Mcts.resetMcts();

            Controller.checkAiPlay();

            int linesFilledAllPlayers = 0;

            for (Player aPlayer : State.getCurrentPlayers()){
                linesFilledAllPlayers += aPlayer.getLinesFilled();
            }

            System.out.println("Lines filled by all players:" + linesFilledAllPlayers);

        }

        //System.out.println("Lines in Game: " + State.getLines().getSize());
        return "Simulation completed.";
    }

    //check if the game has ended
    public static boolean checkEnd() throws IOException {
        if (countClaimedSquare() == (GridController.gridHeight * GridController.gridHeight)) {
            return true;
        } else {
            return false;
        }

    }

    // write on a file the result of the game. For experimentation.
    public static void writeOnTxt(ArrayList<Integer> score, ArrayList<Integer> theWins) throws FileNotFoundException {
        out.println("writing ");
        int nbSquares = GridController.gridHeight * GridController.gridWidth;
        PrintWriter writer = new PrintWriter(new File("experiment.csv"));

        StringBuilder sb = new StringBuilder();

        String Player0 = State.getCurrentPlayers().get(0).getAiType();
        String Player1 = State.getCurrentPlayers().get(1).getAiType();

        sb.append("scores: " + Player0 );
        sb.append(", ");
        sb.append("scores: " + Player1 );
        sb.append(", ");
        sb.append("theWins: " + Player0);
        sb.append("\n");

        //print scores and wins
        for (int i = 0; i < score.size(); i++) {
            sb.append(score.get(i));
            sb.append(", ");
            sb.append(nbSquares - score.get(i));
            sb.append(", ");
            sb.append(theWins.get(i));
            sb.append("\n");
        }

        //print summary line headers
        sb.append("avg. score " + Player0);
        sb.append(",");
        sb.append("avg.score " + Player1);
        sb.append(",");
        sb.append("total wins " + Player0);
        sb.append(",");
        sb.append("total wins " + Player1);
        sb.append(",");
        sb.append("# of draws");
        sb.append("\n");

        //compute summary statistics
        double avgScore = 0;
        int sumWins = 0;
        int sumWinsPl2 = 0;

        for (Integer i : score){
            avgScore += score.get(i);
        }

        for (int j = 0; j < theWins.size(); j++){
            if(theWins.get(j) == null){

            } else
                sumWins += theWins.get(j);
        }

        for (int j = 0; j < theWins.size(); j++){
            if(theWins.get(j) == null){

            } else if( theWins.get(j) == 0)
                sumWinsPl2++;
        }

        //print summary statistics
        sb.append(avgScore / NUM_GAMES);
        sb.append(",");
        sb.append(nbSquares - avgScore /NUM_GAMES);
        sb.append(",");
        sb.append(sumWins + "/" + theWins.size());
        sb.append(",");
        sb.append(sumWinsPl2 + "/" + theWins.size());
        sb.append(",");
        sb.append( NUM_GAMES - sumWins - sumWinsPl2);
        sb.append("\n");


        //out.println(sb.toString());
        writer.write(sb.toString());
        writer.close();


    }
}