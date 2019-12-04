package View;

import AI.AISolver;
import AI.AlphaBeta;
import AI.Mcts;
import GameTree.State;
import AI.*;
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
    private int index=0;

    public Player(Color color, String name, String ai) {
        this.color = color;
        this.name = name;
        this.moves = 1;
        this.score = 0;
        this.ai= ai;
        players.add(this);
        this.index=index;
        index++;

        switch (ai) {
            case  "Mcts":
                solver = new Mcts();
                break;
            case "Rule Based":
                solver = new RuleBased();
                break;
            case "Alpha Beta" :
                solver = new AlphaBeta();
                break;
        }
    }
    public Player() {

    }

    public void addToPlayers(){
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
        //System.out.println("ai = " + ai);
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

    public void aiPlay() {
        //System.out.println("called ai player");
        Line chosenLine = solver.nextMove(State.currentState(), Integer.parseInt(name));
        chosenLine.fill();
        System.out.println("ai fill "+chosenLine.getid());
    }

    public boolean isAlpha() {
        if (ai == "Alpha Beta") {
            return true;
        }
        else{
            return false;
        }
    }

    public static Player nextPlayer(Player prevPlayer){
        int index = 0;
        Player nextPlayer = null;
        for(Player player : players){
            if(player.getName() == prevPlayer.getName()){
                if (index == players.size() - 1) {
                    nextPlayer = players.get(0);
                }
                else{
                    nextPlayer = players.get(index+1);
                }
            }
            index++;
        }
        if(nextPlayer== null){
            System.out.println("did not find player");
        }
        return nextPlayer;
    }

    public static void display(){
        for(Player p : players){
            System.out.println("p = " + p.ai);
        }
    }
   static int counter =0;
    public static Player nextPlayer(Player prevPlayer){
        int index = 0;
        Player nextPlayer = new Player();
        for(Player player : players){
            if(player.getName()==prevPlayer.getName()){
                if (index == players.size() - 1) {
                    nextPlayer = players.get(0);
                }
                else{
                    nextPlayer = players.get(index+1);
                }
            }
            index++;
        }
        if(nextPlayer== null){
            System.out.println("did not find player");
        }
        return nextPlayer;
    }

    public Player cloned(){
        Player cloned  = new Player(this.color,this.name,this.ai);
        cloned.score = this.score;
        cloned.moves = this.moves;

        //might produce a bug
        cloned.solver = this.solver;

        return cloned();
    }

    public int getIndex() {
        return this.index;
    }

}