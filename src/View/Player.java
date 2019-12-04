package View;
import AI.AISolver;
import AI.AlphaBeta;
import AI.Mcts;
import AI.RuleBased;
import GameTree.State;
import javafx.scene.paint.Color;
import Controller.Controller;
import java.util.ArrayList;


public class Player {

    private AISolver solver;
    private Color color;
    private int moves;
    private int score;
    private String name;
    private static ArrayList<Player> actualPlayers = new ArrayList<>();
    private String ai;


    public Player(Color color, String name, String ai) {
        this.color = color;
        this.name = name;
        this.moves = 1;
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
        }
    }

    public Player(){

    }

    public void  addToPlayers(){
        actualPlayers.add(this);
    }

    public static ArrayList<Player> getActualPlayers() {
        return actualPlayers;
    }

    /** this function increments the score of a player
     * @param toAdd the value "point"
     */
    public void addScore(int toAdd) {
        this.score += toAdd;
    }

    /**
     * this is used when a player gets a point and
     * gets a new move
     */
    public void addMoves() {
        this.moves += 1;
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


    /**
     * @return the player which is actually playing (which is why this is static)
     */
    public static Player getActualPlayer(){
        return actualPlayers.get(Controller.turn);
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

    /**
     * used when a player gets a point then he can play again
     */
    public void decreaseMoves() {
        this.moves -= 1;
    }


    public boolean isAlpha() {
        if (ai == "Alpha Beta") { return true; }
        else{ return false; }
    }

    public void aiPlay() {
        //System.out.println("called ai player");
        Line chosenLine = solver.nextMove(State.currentState(), Integer.parseInt(name)-1);
        State.findLine(chosenLine.getid(),State.currentState().getLines()).fill();
        System.out.println("ai fill "+chosenLine.getid());
    }

    /**
     * display the type of player
     */
    public static void display(){
        for(Player p : actualPlayers){
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
        for(Player player : actualPlayers){
            if(player.getName() == prevPlayer.getName()){
                if (index == actualPlayers.size() - 1) {
                    nextPlayer = actualPlayers.get(0);
                }
                else{
                    nextPlayer = actualPlayers.get(index+1);
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
        cloned.moves = this.moves;

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

}