package Controller;

import AI.Mcts;
import GameTree.State;
import View.Board;
import View.*;
import View.Player;
import javafx.scene.paint.Color;
import GameTree.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

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
    public static ArrayList<Integer> scores = new ArrayList<>();
    public static ArrayList<Integer> wins = new ArrayList<>();
    public static ArrayList<Float> coefficient=new ArrayList<>();
    public static float j;

    public static void main(String[] args) throws IOException {
        Line.simulation=true;
        Line.runTesting=false;
        GridController.setGridHeightWidth(3,3);
        try {

                    State.setCurrentState(new State(setPlayers(), 0));
                    Board.makeGrid(GridController.gridWidth,GridController.gridHeight);
                    simulate();

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
        Player a = new Player(Color.CHOCOLATE, Integer.toString(1), "Alpha Beta");
        currentPlayers.add(a);
        a.setSolver();

        Player b = new Player(Color.RED, Integer.toString(2), "Rule Based");
        currentPlayers.add(b);
        b.setSolver();

        return currentPlayers;
    }

    public static int counter =0;
    public static String simulate() throws IOException {

        for (int i = 0; i < 10; i++) {

            counter++;
            out.println("new simulation "+counter);
            for (Line line : GridController.getLines()) {
                line.setEmpty(true);
            }
            State.setCurrentState(new State(GridController.getLinesIds(), setPlayers()));
            State.currentState().setPlayers(setPlayers(), 0);
            Mcts.resetMcts();

            Controller.checkAiPlay();
        }

        return "Done";
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
    public static void writeOnTxt(ArrayList<Integer> score, ArrayList<Integer> wins) throws FileNotFoundException {
        out.println("writing ");
        int nbSquares = GridController.gridHeight * GridController.gridWidth;
        PrintWriter writer = new PrintWriter(new File("experimentMctsFirst.csv"));

        StringBuilder sb = new StringBuilder();
        out.println("score size: "+score.size());
        out.println("win size: "+wins.size());

        sb.append("scores: ");
        sb.append("scores: ");
        sb.append("\n");

        try {
            for (int i = 0; i < score.size(); i++) {
                sb.append(score.get(i));
                sb.append(", ");
                sb.append(nbSquares - score.get(i));
                sb.append(", ");
                sb.append("\n");
            }
        } catch (IndexOutOfBoundsException e) {
            writer.write(sb.toString());
            writer.close();
        }

        //out.println(sb.toString());
        writer.write(sb.toString());
        writer.close();

    }
}