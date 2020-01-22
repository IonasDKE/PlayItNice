package Controller;

import AI.*;
import GameTree.State;
import View.Board;
import View.*;
import View.Player;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;

import static Controller.Controller.countClaimedSquare;
import static java.lang.System.*;

public class Testing {
    public static ArrayList<Integer> scores= new ArrayList<>();
    public static ArrayList<Integer> wins = new ArrayList<>();

    public static void main(String [] args) throws IOException {
        Line.simulation=true;
        ArrayList<ArrayList<String>> simulation = getAllCombination();

        GridController.setGridHeightWidth(3,3);

        try {
            for (int i=0;i<simulation.size();i++) {
                out.println("new simulation ");
                ArrayList<Player> currentPlayer = setPlayers(simulation.get(i).get(0), simulation.get(i).get(1));
                State.setCurrentState(new State(currentPlayer, 0));
                Board.makeGrid(GridController.gridWidth, GridController.gridHeight);

                simulate(currentPlayer);
            }
            writeOnTxt(simulation);

        } catch (OutOfMemoryError e) {
            out.println("out of memory");
            writeOnTxt(simulation);
        }
    }

    public static ArrayList<Player> setPlayers(String playerA, String playerB){
        ArrayList<Color> players = new ArrayList<>();
        players.add(Color.BLUE);
        players.add(Color.CHOCOLATE);

        ArrayList<Player> currentPlayers = new ArrayList<>();
        int playerNumber=0;
        Player a = new Player(Color.CHOCOLATE, Integer.toString(1), playerA);
        currentPlayers.add(a);
        a.setSolver();
        playerNumber++;

        Player b = new Player(Color.RED, Integer.toString(2), playerB);
        currentPlayers.add(b);
        b.setSolver();
        playerNumber++;
        return currentPlayers;
    }

    public static String simulate(ArrayList<Player> currentPlayer) throws IOException {
        for(int i = 0; i < 15; i++) {
            out.println("new simulation "+i);
            //State.currentState().display();
            for (Line line : GridController.getLines()) {
                line.setEmpty(true);
            }

            State.setCurrentState(new State(setPlayers(currentPlayer.get(0).getAiType(), currentPlayer.get(1).getAiType()),0));
            State.currentState().setLines(GridController.getLinesIds());
            Mcts.resetMcts();
            Controller.checkAiPlay();
        }

        return "Done";
    }

    //check if the game has ended
    public static boolean checkEnd() throws IOException {
        if (countClaimedSquare() == (GridController.gridHeight * GridController.gridWidth)) {
            return true;
        } else {

            return false;
        }

    }

    // write on a file the result of the game. For experimentation.
    public static void writeOnTxt(ArrayList<ArrayList<String>> allPlayers) throws FileNotFoundException {
        out.println("writing ");
        int nbSquares = GridController.gridHeight * GridController.gridWidth;
        PrintWriter writer = new PrintWriter(new File("experimentBig.csv"));

        StringBuilder sb = new StringBuilder();

        for (int j = 0; j< allPlayers.size(); j++) {
            ArrayList<String> players =allPlayers.get(j);
            sb.append(players.get(0)+" scores: ");
            sb.append(players.get(1)+" scores: ");
            sb.append(players.get(0)+" wins: ");
            sb.append("\n");

            for (int i = 0; i < scores.size(); i++) {
                sb.append(scores.get(i));
                sb.append(", ");
                sb.append(nbSquares - scores.get(i));
                sb.append(", ");
                sb.append(wins.get(i));
                sb.append("\n");
            }
            out.println(scores.size());
            scores.remove(25);
            wins.remove(25);
        }
        //out.println(sb.toString());
        writer.write(sb.toString());
        writer.close();
    }

    public static ArrayList<ArrayList<String>> getAllCombination() {
        ArrayList<ArrayList<String>> toReturn=new ArrayList<>();
        String[] allAi= {"Mcts Tree","Rule Based","Alpha Beta", "MiniMax"};
        int index=0;

        for (int i=0;i<allAi.length;i++) {
            for (int j=0;j<allAi.length;j++) {
                toReturn.add(new ArrayList<>());

                toReturn.get(index).add(allAi[i]);
                toReturn.get(index).add(allAi[j]);
                index++;
            }
        }
        return toReturn;
    }
}

