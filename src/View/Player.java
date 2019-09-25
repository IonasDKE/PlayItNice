package  View;
import javafx.scene.paint.Color;

public class Player {
    private Color color;
    private int player_number;
    private int moves;
    private int score;
    private int playerCounter =0;
    private String name;

    public Player(Color color, String name) {
        this.player_number = playerCounter;
        this.color = color;
        this.name = name;
        this.moves = 0;
        playerCounter++;
    }

    public void addScore(int toAdd) {
        this.score =+ toAdd;
        //System.out.println("score changed " + this.score + " " + this.name);
    }

    public void addMoves() {
        this.moves = this.moves + 1;
        System.out.println("move updated " + this.moves + " " + this.name);
        //System.out.println();
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

    public int getPlayer_number() {
        return this.player_number;
    }

}
