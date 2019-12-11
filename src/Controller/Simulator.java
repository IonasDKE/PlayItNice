package Controller;

import AI.Mcts;
import GameTree.State;
import View.Board;
import View.Line;
import View.Player;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;

import static Controller.Controller.countClaimedSquare;
import static java.lang.System.*;

public class Simulator {
    //AdjacencyMatrix.setMatrix(chosenM,chosenN);
    public static void main(String [] args) throws IOException {
        try {
            out.println("new simulation ");
            State.setCurrentState(new State(setPlayers(), 0));
            Board.makeGrid(height, weight);

            out.println(simulate());
            out.println("score size "+scores.size());
            writeOnTxt(scores, wins);
        }catch(OutOfMemoryError e){
            out.println("out of memory");
            writeOnTxt(scores,wins);
        }


    }

    public static int height =3;
    public static int weight =3;

    public static ArrayList<Player> setPlayers(){
        ArrayList<Color> players = new ArrayList<>();
        players.add(Color.BLUE);
        players.add(Color.CHOCOLATE);

        ArrayList<Player> currentPlayers = new ArrayList<>();
        int playerNumber=0;
        Player a = new Player(Color.CHOCOLATE, Integer.toString(1), "MiniMax");
        currentPlayers.add(a);
        a.setSolver();
        playerNumber++;

        Player b = new Player(Color.RED, Integer.toString(2), "Mcts");
        currentPlayers.add(b);
        b.setSolver();
        playerNumber++;
        return currentPlayers;
    }
    public static ArrayList<Integer> scores= new ArrayList<>();
    public static ArrayList<Integer> wins = new ArrayList<>();
    public static int counter=0;
    public static String simulate() throws IOException {

        for(int i = 0; i < 50; i++) {
            out.println("new simulation "+i);
            //State.currentState().display();
            for (Line line : State.currentState().getLines()) {
                line.setEmpty(true);
            }
            State.currentState().setPlayers(setPlayers(), 0);
            Mcts.resetMcts();
            Controller.checkAiPlay();
        }

        return "Done";
    }

    //check if the game has ended
    public static boolean checkEnd() throws IOException {
        if (countClaimedSquare() == (height * weight)) {
            return true;
        } else {

            return false;
        }

    }

    // write on a file the result of the game. For experimentation.
    public static void writeOnTxt(ArrayList<Integer> values, ArrayList<Integer> wins) throws FileNotFoundException {
        out.println("writing ");
            PrintWriter writer = new PrintWriter(new File("experiment.csv"));

            StringBuilder sb = new StringBuilder();

            sb.append("score: ");
            sb.append(" wins: ");
            sb.append("\n");

            for (int i =0;i<values.size();i++) {
                sb.append(values.get(i));
                sb.append(",");
                sb.append(wins.get(i));
                sb.append("\n");
            }

        //out.println(sb.toString());
            writer.write(sb.toString());
            writer.close();

    }
}
