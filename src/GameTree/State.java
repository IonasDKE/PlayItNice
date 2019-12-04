package GameTree;

import View.Line;
import View.Square;
import View.Player;

import java.util.ArrayList;

import static Controller.Controller.isThirdLine;

public class State {

    private static State currentState;
    private ArrayList<Line> getAvailableMoves;
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

    public ArrayList<State> computeAndGetChildren(){
        if(this.children==null) {
            computeChildren();
        }
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
        State.findLine(line.getid(),childState.getLines()).setEmpty(false);
        children.add(childState);

        //System.out.println();
        //System.out.println("child "+children.size());
        //childState.display();
    }

    public void fillLine(Line lineToFill) {
        for (Line line :this.getLines()) {
            if (line == lineToFill) {
                line.setEmpty(false);
            }
        }
    }

    public void display(){
        Line.display(this.getLines());
        //Square.display(this.getSquares());
        //System.out.println();
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
        System.out.println(" nd" + this.getLines().size());
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

    public static Line findMove(ArrayList<Line> state) {
        Line toReturn = null;
        for (Line toFind: currentState.getLines()) {
            //System.out.println("toFind emptiness :" + toFind.isEmpty());
            for (Line line : state) {
                //System.out.println("line emptiness :" + line.isEmpty());
                if (toFind.isEmpty() != line.isEmpty() && toFind.isEmpty()) {
                    toReturn = toFind;
                    //System.out.println("line found ");
                }
            }
        }
        System.out.println();
        return toReturn;
    }

    public int getScore(int turn){
        if(turn == 0){
            return Player.getPlayers().get(0).getScore();
        }
        else{
            return Player.getPlayers().get(1).getScore();
        }
    }

    //TO DO : add state info methods
}