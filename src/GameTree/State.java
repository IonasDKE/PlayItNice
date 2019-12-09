package GameTree;

import Controller.Controller;
import View.Line;
import View.Square;
import View.Player;

import java.util.ArrayList;

import static Controller.Controller.isThirdLine;

public class State {

    private ArrayList<Player> players;
    private int turn;
    private static State currentState;
    private ArrayList<State> children;
    private ArrayList<Line> lines;
    private ArrayList<Square> squares;

    /**
     * @param g set of line
     * @param players assign an array list which contains all the player to a state
     */
    public State (ArrayList<Line> g, ArrayList<Player> players){
        this.lines = g;
        this.squares = Square.buildSquares(g);
        this.players = players;
    }

    /**
     * use this state constructor only for the current state
     */
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
        /*
        for(View.Line line : this.lines){
            if(line.isEmpty() && !isThirdLine(line)) {
                addChild(line,result);
            }
        }
        */
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

        int scored = Controller.checkAnySquareClaimed(filledLine);

        if(scored>0){
            childState.getActualPlayer().addScore(scored);
        }else{
            childState.updateTurn();
        }


        Controller.updateTurn(filledLine, childState);

        children.add(childState);

        //System.out.println();
        //System.out.println("child "+children.size());
        //childState.display();
    }

    /*public void increaseScore(Player player) {
        this.scores.set(player.getIndex(), +1);
    }*/

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
        /*
        if(this.children!=null) {
            result.setChildren(this.clonedChildren());
        }
         */
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

    //finds the lines that needs to be colored for mcts
    public static Line findDiffLineMinMax(ArrayList<Line> state1, ArrayList<Line> state2) {
        Line randomEmptyLine=null;
        for (Line line : state1) {
            if (line.isEmpty())
                randomEmptyLine=line;
            if (line.isEmpty() != State.findLine(line.getid(),state2).isEmpty() && line.isEmpty()) {
                return line;
            }
        }
        //System.out.println("random line returned");
        return randomEmptyLine;
    }

    //finds the lines that needs to be colored for mcts
    public static Line findDiffLineMcts(ArrayList<Line> state1, ArrayList<Line> state2) {
        Line randomEmptyLine=null;
        for (Line line : state1) {
            if (line.isEmpty() != State.findLine(line.getid(),state2).isEmpty()) {
                return line;
            }
        }
        //System.out.println("random line returned");
        return randomEmptyLine;
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
        if(children!=null) {
            this.getChildren().clear();
        }
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

    public ArrayList<Line> getAvailableMoves(){
        ArrayList<Line> newLines = new ArrayList<>();
        for(Line line : currentState().getLines()){
            if(line.isEmpty()){
                newLines.add(line);
            }
        }
        return newLines;
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

    public int getScore(int turn) {
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

    public void updateTurn() {
        if (turn < players.size() - 1) {
            turn++;
        } else {
            turn = 0;
        }
    }

    public void setPlayers(ArrayList<Player> newPlayers, int turn) {
        this.players = newPlayers;
        this.turn = turn;
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
        for(State state : this.children){
            result.add(state.cloned());
        }
        return result;
    }

    public int getValence(int k) {
        int counter = 0;
        for(Square sq : getSquares()){
            if(sq.getValence()==k){
                counter++;
            }
        }
       return counter;
    }

    public static int inverseTurn(int turn){
        if (turn ==0){
            return 1;
        }
        else if (turn ==1){
            return 0;
        }
        return 0;
    }

    public State duplicateBoard(Line line, int turn) {
        return  computeAndGetChildren().get(1);
    }

    public void nextTurn(){
        this.turn = this.turn+1;
    }

    public boolean isComplete() {
        return getScore(players.get(0)) + getScore(players.get(1)) == getLines().size()-2;
    }

    public State computeAChild(Line line){
        State childState = this.cloned();
        Line filledLine = State.findLine(line.getid(),childState.getLines());
        filledLine.setEmpty(false);

        Controller.updateTurn(filledLine, childState);
        return childState;
    }

   /*public void setScores(ArrayList<Integer> newScores) {
        for(Player p : players){

        }
    }*/

    //TO DO : add state info methods

}