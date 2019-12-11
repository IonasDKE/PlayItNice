package View;
import AI.*;
import GameTree.State;
import javafx.scene.paint.Color;
import Controller.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class Player {

    private AISolver solver;
    private Color color;
    private int score;
    private String name;
    private String ai;


    public Player(Color color, String name, String ai) {
        this.color = color;
        this.name = name;
        this.score = 0;
        this.ai= ai;
    }

    public void setSolver(){
        switch (this.ai) {
            case  "Mcts":
                solver = new Mcts();
                break;
            case "Rule Based":
                solver = new RuleBased();
                break;
            case "Alpha Beta" :
                solver = new AlphaBeta();
                break;
            case "MiniMax" :
                solver = new MiniMax();
                break;
        }
        if(solver==null){
            System.out.println("solver "+this.ai+ " is null");
        }
    }

    /** this function increments the score of a player
     * @param toAdd the value "point"
     */
    public void addScore(int toAdd) {
        this.score += toAdd;
    }

    /**
     * @return true if the player is an AI ( not a human )
     */
    public boolean isAi() {
        if (ai == "Human"){
            return false;
        }else{
            return true;
        }
    }

    /**
     * @return the string which contains which ai we are working with
     */
    public String getAiType(){
        return ai;
    }


    public int getScore(){
        return this.score;
    }

    public Color getColor() {
        return this.color;
    }


    public String getName() {
        return this.name;
    }


    public boolean isAlpha() {
        if (ai == "Alpha Beta")
        { return true; }
        else{ return false; }
    }

    public void aiPlay() throws IOException {
        //System.out.println("called ai player");

        Line chosenLine = solver.nextMove(State.currentState().cloned(), State.currentState().getTurn());
        //System.out.println("ai fill "+chosenLine.getid());

        State.findLine(chosenLine.getid(),State.currentState().getLines()).fill();

    }

    /**
     * display the type of player
     */
    public static void display(){
        for(Player p : State.getCurrentPlayers()){
            System.out.println("p = " + p.ai);
        }
    }

    /**
     * @param prevPlayer the player who played the move before
     * @return the next player which has to play
     */
    public static Player nextPlayer(Player prevPlayer){
        int index = 0;
        Player nextPlayer = null;
        for(Player player : State.getCurrentPlayers()){
            if(player.getName() == prevPlayer.getName()){
                if (index == State.getCurrentPlayers().size() - 1) {
                    nextPlayer = State.getCurrentPlayers().get(0);
                }
                else{
                    nextPlayer = State.getCurrentPlayers().get(index+1);
                }
            }
            index++;
        }
        if(nextPlayer== null){
            System.out.println("did not find player");
        }
        return nextPlayer;
    }

    /**
     * @return a clone of a player
     */
    public Player cloned(){
        Player cloned  = new Player(this.color,this.name,this.ai);
        cloned.score = this.score;

        //might produce a bug
        cloned.solver = this.solver;

        return cloned;
    }

    public static ArrayList<Player> cloned(ArrayList<Player> p){
        ArrayList<Player> result = new ArrayList<>();
        for(Player player : p){
            result.add(player.cloned());
        }
        return result;
    }

    public void setScore(int newScore) {
        this.score = newScore;
    }

}