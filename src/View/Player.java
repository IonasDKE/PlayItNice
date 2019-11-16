package View;
import GameTree.State;
import javafx.scene.paint.Color;
import Controller.Controller;
import java.util.ArrayList;

public class Player {
    private Color color;
    private int moves;
    private int score;
    private String name;
    private static ArrayList<Player> players = new ArrayList<>();
    private String ai;

    public Player(Color color, String name, String ai) {
        this.color = color;
        this.name = name;
        this.moves = 1;
        this.score = 0;
        this.ai= ai;
        players.add(this);
    }

    public static ArrayList<Player> getPlayers() {
        return players;
    }

    public void addScore(int toAdd) {
        this.score += toAdd;
    }

    public void addMoves() {
        this.moves += 1;
    }

    public boolean isAi() {
        if (ai == "Human"){
            return false;
        }else{
            return true;
        }
    }

    public String getAiType(){
        return ai;
    }


    public static Player getActualPlayer(){
        return players.get(Controller.turn);
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


    public void endSquarePlay(){
        Controller.completeSquare(State.currentState().getSquares());
        Controller.colorRandomLine(State.currentState().getLines());
    }
}
