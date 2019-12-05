package GameTree;

import Controller.Controller;
import View.Line;
import View.Square;
import View.Player;

import java.util.ArrayList;

import static Controller.Controller.isThirdLine;

public class State {

    private ArrayList<Player> players = new ArrayList<>();
    private int turn;
    private static State currentState;
    private ArrayList<State> children;
    private ArrayList<Line> lines;
    private ArrayList<Square> squares;
   // private ArrayList<Integer> scores=new ArrayList<>(Arrays.asList(0,0)); //the score of a player at that state, at the same index as
      /*                                                          //player in players array
    public State (ArrayList<Line> g){
        lines = g;
        squares = Square.buildSquares(g);
    }
*/
    public State (ArrayList<Line> g, ArrayList<Player> players){
        this.lines = g;
        this.squares = Square.buildSquares(g);
        this.players = players;
    }

    //use this state constructor only for the current state!
    public void setLinesAndSquares(ArrayList<Line> lines, ArrayList<Square> squares) {
        this.lines = lines;
        this.squares = squares;
    }

    //use this state constructor only for the current state!
    public State( ArrayList<Player> players, int turn) {
        this.players = players;
        this.turn = turn;
    }

    /**
     * @return the player which is actually playing (which is why this is static)
     */
    public static Player getCurrentActualPlayer(){
        return State.currentState().getActualPlayer();
    }
    public static ArrayList<Player> getCurrentPlayers(){
        return State.currentState().getPlayers();

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

    public void setChildren(ArrayList<State> children) {
        this.children = children;
    }

    public ArrayList<Player> getPlayers() {
        return players;
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

        Controller.updateTurn(filledLine, childState);

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
        State result = new State(State.cloned(this.getLines()),Player.cloned(this.players));
        result.setTurn(this.getTurn());
        if(this.children!=null) {
            result.setChildren(this.clonedChildren());
        }
        return result;
    }

    //returns a cloned arraylist of lines
    public static ArrayList<Line> cloned(ArrayList<Line> lines){
        ArrayList<Line> result = new ArrayList<>();
        for(Line line : lines){
            result.add(line.cloned());
        }
        return result;
    }

    //finds the line which empty attribute is different from state 1 then state 2
    public static Line findDiffLine(ArrayList<Line> state1, ArrayList<Line> state2) {
        Line randomEmptyLine=null;
        for (Line line : state1) {
            if (line.isEmpty())
                randomEmptyLine=line;
            if (line.isEmpty() != State.findLine(line.getid(),state2).isEmpty() && line.isEmpty()) {
                return line;
            }
        }
        System.out.println("random line returned");
        return randomEmptyLine;
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
        this.getPlayers().clear();
        this.getChildren().clear();
        this.turn = 0;
    }

    public int numberOfAvailableMoves(){
        return getEmptyLines().size();
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

    public ArrayList<Line> getLines() {
        return lines;
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }

    public int getScore(int turn){
        return players.get(turn).getScore();
    }

    public Player getActualPlayer() {
        return players.get(turn);
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void nextTurn(){
        this.turn = this.turn+1;
    }


    public int getScore(Player player) {
        //-1 is just an arbitrary value
        int result = -1;
        for (Player p: players) {
            if (p.getName() == player.getName()) {
                return p.getScore();
            }
        }
        if (result == -1) {
            System.out.println("player not found");
        }
        return result;
    }

    public ArrayList<State> clonedChildren(){
        ArrayList<State> result = new ArrayList<>();
        if(this.children==null){
            System.out.println("children do not exist");
        }
        for(State state : this.children){
            result.add(state.cloned());
        }
        return result;
    }

}