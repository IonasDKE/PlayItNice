package  View;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Player {
    private Color color;
    private int moves;
    private int score;
    private String name;
    private static int turn = 0;
    private static ArrayList<Player> players = new ArrayList<>();

    public Player(Color color, String name) {
        this.color = color;
        this.name = name;
        this.moves = 1;
        this.score = 0;
        players.add(this);
    }
/*
    public void addPlayerToList() {
    players.add(this);
    }
*/
    public static ArrayList<Player> getPlayers() {
        return players;
    }

    public void addScore(int toAdd) {
        this.score += toAdd;
        //System.out.println("score changed " + this.score + " " + this.name);
    }

    public void addMoves() {
        this.moves += 1;
        //System.out.println("move updated " + this.moves + " " + this.name);
        //System.out.println();
    }

    public static void changeTurn() {
        Player.getPlayers().get(turn).addMoves();
        if (turn < Player.getPlayers().size()-1) { turn ++; }
        else { turn = 0; }

        System.out.println("turn = " + turn);;
    }

    public static Player getActualPlayer(){
        return players.get(turn);
    }

    public int getScore(){
        return this.score;
    }

    public Color getColor() {
        return this.color;
    }

    public int getMoves() {
        return this.moves;
    }

    public String getName() {
        return this.name;
    }

    public void decreaseMoves() {
        this.moves -= 1;
    }


}
