package Controller;

import AI.Mcts;
import GameTree.State;
import View.Board;
import View.*;
import View.Player;
import View.Square;
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
    public static void main(String[] args) throws IOException {
        Line.simulation=true;
        Simulator.scores.add(new ArrayList<>());
        Simulator.wins.add(new ArrayList<>());

        ArrayList<Integer> scores = Simulator.scores.get(0);
        ArrayList<Integer> wins = Simulator.wins.get(0);

        try {
            State.setCurrentState(new State(setPlayers(), 0));
            Board.makeGrid(width, height);
            simulate();

            out.println("score size " + scores.size());
            writeOnTxt(scores, wins);

        } catch (OutOfMemoryError e) {
            out.println("out of memory");
            writeOnTxt(scores, wins);
        }

    }

    public static int height = 3;
    public static int width = 3;

    public static ArrayList<Player> setPlayers() {
        ArrayList<Color> players = new ArrayList<>();
        players.add(Color.BLUE);
        players.add(Color.CHOCOLATE);

        ArrayList<Player> currentPlayers = new ArrayList<>();
        Player a = new Player(Color.CHOCOLATE, Integer.toString(1), "Mcts Tree");
        currentPlayers.add(a);
        a.setSolver();

        Player b = new Player(Color.RED, Integer.toString(2), "Mcts Tree");
        currentPlayers.add(b);
        b.setSolver();
        return currentPlayers;
    }

    public static String simulate() throws IOException {

        for (int i = 0; i < 5; i++) {
            out.println("new simulation " + i);
            //State.currentState().display();
            for (Line line: GridController.getLines())
                line.setEmpty(true);

            State.currentState().setPlayers(setPlayers(), 0);
            Mcts.resetMcts();

            while (!checkEnd())
                Controller.checkAiPlay();

        }

        return "Done";
    }

    //check if the game has ended
    public static boolean checkEnd() throws IOException {
        if (countClaimedSquare() == (height * width)) {
            return true;
        } else {
            return false;
        }

    }

    // write on a file the result of the game. For experimentation.
    public static void writeOnTxt(ArrayList<Integer> score, ArrayList<Integer> wins) throws FileNotFoundException {
        out.println("writing ");
        int nbSquares = height * width;
        PrintWriter writer = new PrintWriter(new File("experiment.csv"));

        StringBuilder sb = new StringBuilder();

        sb.append("scores: ");
        sb.append("scores: ");
        sb.append("wins: ");
        sb.append("\n");

        for (int i = 0; i < score.size(); i++) {
            sb.append(score.get(i));
            sb.append(", ");
            sb.append(nbSquares - score.get(i));
            sb.append(", ");
            sb.append(wins.get(i));
            sb.append("\n");
        }

        //out.println(sb.toString());
        writer.write(sb.toString());
        writer.close();

    }
}