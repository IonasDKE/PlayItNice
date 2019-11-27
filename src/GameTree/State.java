package GameTree;

import Controller.Controller;
import View.Line;
import View.Player;
import View.Square;

import java.util.ArrayList;
import java.util.Random;

import static Controller.Controller.completeSquareID;

public class State {


    private static State currentState;
    private ArrayList<Line> getAvailableMoves;

    public static int inverseTurn(int turn) {
        if(turn == 0){
            return 1;
        }
        else {
            return 0;
        }
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }

    private ArrayList<State> children;
    private ArrayList<Line> lines;
    private ArrayList<Square> squares;

    public State (ArrayList<Line> g){
        lines = g;
        squares = Square.buildSquares(g);
    }

    public State(ArrayList<Line> lines, ArrayList<Square> squares) {
        this.lines = lines;
        this.squares = squares;
    }

    //get the children of the State
    public ArrayList<State> getChildren(){
        return this.children;
    }


    public ArrayList<Line> cloneLines(){
        ArrayList<Line> clone = new ArrayList<>();
        for (Line l : lines) {
            Line result = new Line(l.getid(), l.isEmpty(), l.getClonedSquares());
            clone.add(result);
        }
        return clone;
    }

    public void computeChildren(){
        ArrayList<State> result = new ArrayList<>();

        //children that would build a third line in a square are excluded
        for(View.Line line : this.lines){
            if(line.isEmpty() && !Controller.isThirdLine(line)) {
                addChild(line,result);
            }
        }

        //case if it is not possible to pick a line that will not be a third line
        //if(result.size()==0) {
        for (Line line : this.lines) {
            if(line.isEmpty()){
                addChild(line,result);
            }
        }
        //}
        this.children=result;

    }
    private void addChild(Line line, ArrayList<State> children){
        State childState = this.cloned();
        State.findLine(line.getid(),childState.getLines()).setEmpty(false);
        children.add(childState);

        //System.out.println();
        //System.out.println("child "+children.size());
        //childState.display();
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


    public ArrayList<Line> getEmptyLines() {
        ArrayList<Line> emptyLines = new ArrayList<>();
        for (Line line:this.getLines()) {
            if (line.isEmpty()) {
                emptyLines.add(line);
            }
        }
        return emptyLines;
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
        return new State(State.cloned(this.getLines()));
    }

    //returns a cloned arraylist of lines
    public static ArrayList<Line> cloned(ArrayList<Line> lines){
        ArrayList<Line> result = new ArrayList<>();
        for(Line line : lines){
            result.add(line.cloned());
        }
        return result;
    }

    public static Line findLine(int id){

        return findLine(id,currentState.getLines());
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

    //finds the lines that needs to be colored for mcts
    public static Line findLine(ArrayList<Line> currentState, ArrayList<Line> stateWithBestPlay) {
        for (Line line : currentState) {
            for (Line toFind : stateWithBestPlay) {
                if (line.isEmpty() != toFind.isEmpty()) {
                    return line;
                }
            }
        }
        return null;
    }


    //clears a state
    public void reset(){
        this.getLines().clear();
        this.getSquares().clear();
    }

    public int getScore(int turn){
        if(turn == 0){
            return Player.getPlayers().get(0).getScore();
        }
        else{
            return Player.getPlayers().get(1).getScore();
        }
    }

    //public State updateState(Line line, int color){
    //    return getChildren();
    //}

    public ArrayList<Line> getAvailableMoves(){
        //System.out.println(this.lines.size());
        return this.lines;

    }

    public int numberOfAvailableMoves(){
        //System.out.println(getAvailableMoves().size());
        return getAvailableMoves().size();

    }

    public static Line findMove(State parent, State child){
        for(Line a : parent.getLines()){
            if(a.isEmpty() != State.findLine(a.getid(), child.getLines()).isEmpty()){
                return a;
            }
        }
        return null;
    }
    public void fillLine(Line lineToFill) {
        for (Line line :this.getLines()) {
            if (line == lineToFill) {
                line.setEmpty(false);
            }
        }

    }


    //TO DO : add state info methods
}