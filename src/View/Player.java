package View;
import AI.AISolver;
import AI.Mcts;
import GameTree.State;
//import AI.AlphaBeta;
import javafx.scene.paint.Color;
import Controller.Controller;
import java.util.ArrayList;

public class Player {
    private AISolver solver;
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
        if(ai == "Mcts"){
            solver = new Mcts();
        }
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
        System.out.println("ai = " + ai);
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

    public boolean isAlpha() {
        if (ai == "Alpha Beta") {
            return true;
        }
        else{
            return false;
            }
    }

    public void alphaBeta() {
        Controller.setAlphaBeta();
    }

    public void mcts() {
        solver.nextMove(State.currentState(), Integer.parseInt(name));
    }

    public static void display(){
        for(Player p : players){
            System.out.println("p = " + p.ai);
        }
    }
}