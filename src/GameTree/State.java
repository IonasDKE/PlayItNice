package GameTree;

import Controller.Controller;
import View.Line;
import View.Square;
import View.Player;
import Controller.*;

import java.util.ArrayList;
import java.util.Arrays;

import static Controller.Controller.isThirdLine;

public class State {

    private Player playerToPlay;
    private static State currentState;
    private ArrayList<State> children;
    private ArrayList<Line> lines;
    private ArrayList<Square> squares;
    private Player playerToPlay;
    private ArrayList<Integer> scores=new ArrayList<>(Arrays.asList(0,0)); //the score of a player at that state, at the same index as
      /*                                                          //player in players array
    public State (ArrayList<Line> g){
        lines = g;
        squares = Square.buildSquares(g);
    }
*/
    public State (ArrayList<Line> g, Player player){
        lines = g;
        squares = Square.buildSquares(g);
        playerToPlay = player.cloned();
    }

    public State(ArrayList<Line> lines, ArrayList<Square> squares, Player player) {
        this.lines = lines;
        this.squares = squares;
        playerToPlay = player.cloned();
    }

    //get the children of the State
    public ArrayList<State> getChildren(){
        return this.children;
    }

    public ArrayList<State> computeAndGetChildren(){
        if(this.children==null) {
            computeChildren();
        }
        return this.children;
    }

    public void computeChildren(){
        ArrayList<State> result = new ArrayList<>();

        //children that would build a third line in a square are excluded

        for(View.Line line : this.lines){
            if(line.isEmpty() && !isThirdLine(line)) {
                addChild(line,result);
            }
        }

        //case if it is not possible to pick a line that will not be a third line
        if(result.size()==0) {
          //  System.out.println("case 2");
            for (Line line : this.lines) {
                if(line.isEmpty()){
                    addChild(line,result);
                }
            }

        }
        //System.out.println("result = " + result.size());
        this.children=result;

    }

    private void addChild(Line line, ArrayList<State> children){
        State childState = this.cloned();
        Line filledLine = State.findLine(line.getid(),childState.getLines());
        filledLine.setEmpty(false);
        Player nextPlayer;
        int scored = Controller.checkAnySquareClaimed(filledLine);
        if(scored>0){
            nextPlayer = this.getPlayerToPlay().cloned();
            nextPlayer.addScore(scored);
        }else{
            nextPlayer = Player.nextPlayer(this.playerToPlay).cloned();
        }
        childState.setPlayerToPlay(nextPlayer);
        children.add(childState);

        //System.out.println();
        //System.out.println("child "+children.size());
        //childState.display();
    }

    public void increaseScore(Player player) {
        this.scores.set(player.getIndex(), +1);
    }

    public void display(){
        Line.display(this.getLines());
        Square.display(this.getSquares());
        System.out.println();
    }

    public static State currentState() {
        return currentState;
    }

    public void setSquares(ArrayList<Square> squares) {
        this.squares = squares;
    }

    public static void setCurrentState(State currentState) {
        State.currentState = currentState;
    }

    //finds a square in the current game state
    public static Square findSquare(int id){
        return findSquare(id, currentState.getSquares());
    }

    //find the square that as a certain id, return's that square
    public static Square findSquare(int id, ArrayList<Square> sqs) {
        Square out= null;
        for (Square sq : sqs) {
            if (sq.getid()==id)
                out = sq;
        }
        if(out==null){
            //System.out.println("cannot find this square");
        }
        return out;
    }

    //returns a cloned state
    public State cloned(){
        return new State(State.cloned(this.getLines()),this.getPlayerToPlay());
    }

    //returns a cloned arraylist of lines
    public static ArrayList<Line> cloned(ArrayList<Line> lines){
        ArrayList<Line> result = new ArrayList<>();
        for(Line line : lines){
            result.add(line.cloned());
        }
        return result;
    }

    //finds the lines that needs to be colored for mcts
    public static Line findDiffLine(ArrayList<Line> state1, ArrayList<Line> state2) {
        for (Line line : state1) {
                if (line.isEmpty() != State.findLine(line.getid(),state2).isEmpty()) {
                    return line;
                }
        }
        return null;
    }

    public static Line findDiffLine( ArrayList<Line> state1){
        return findDiffLine(State.currentState().getLines(), state1);
    }

    //find the line that as a certain id, return's that line
    public static Line findLine(int id, ArrayList<Line> lines) {
        Line lineToReturn = null;
        for (Line line : lines) {
            if (line.getid()==id)
                lineToReturn = line;
        }
        return lineToReturn;
    }

    //clears a state
    public void reset(){
        this.getLines().clear();
        this.getSquares().clear();
    }

    public int numberOfAvailableMoves(){
        //System.out.println(getAvailableMoves().size());
        return getAvailableMoves().size();

    }

    public ArrayList<Line> getEmptyLines() {
        ArrayList<Line> emptyLines = new ArrayList<>();
        for (Line line:this.getLines()) {
            if (line.isEmpty()) {
                emptyLines.add(line);
            }
        }
        return emptyLines;
    }

    public ArrayList<Line> getAvailableMoves(){
        //System.out.println(this.lines.size());
        return this.lines;

    }
    public ArrayList<Line> getNdValenceLines(){
        ArrayList<Line> lines = new ArrayList<>();
        //System.out.println(" nd" + this.getLines().size());
        for (Line line: this.getLines()) {
            if (line.isEmpty() && !isThirdLine(line)) {
                lines.add(line);
            }
        }
        return lines;
    }

    public int isEqual(State other){
        int nbOfDifferences = 0;
        for(Line l : other.getLines()){
            if(l.isEmpty() != State.findLine(l.getid(),this.lines).isEmpty()){
                nbOfDifferences ++;
                //System.out.println("nbOfDifferences = " + l.getid());
            }
        }
        return nbOfDifferences;
    }

    public static Line findMove(State parent, State child){
        //System.out.println("bugg");
        return parent.getLines().get(0);
    }
    public ArrayList<Line> getLines() {
        return lines;
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }

    public int getScore(int turn){
        if(turn == 0){
            return Player.getPlayers().get(0).getScore();
        }
        else{
            return Player.getPlayers().get(1).getScore();
        }
    }

    public Player getPlayerToPlay() {
        return this.playerToPlay;
    }

    public void setPlayerToPlay(Player newPlayer) {
        this.playerToPlay=newPlayer;
    }

    public int getScore(Player player) {
        return scores.get(player.getIndex());
    }

    public void setScores(ArrayList<Integer> newScores) {
        this.scores=newScores;
    }

    //TO DO : add state info methods

}