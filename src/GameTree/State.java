package GameTree;

import Controller.Controller;
import Controller.GridController;
import View.Line;
import View.Square;
import View.Player;

import java.util.ArrayList;

public class State {

    private ArrayList<Player> players;
    private int turn;
    private static State currentState;
    private ArrayList<State> children;
    private ArrayList<Integer> lines;
    //private ArrayList<Square> squares;

    /**
     * @param g set of line
     * @param players assign an array list which contains all the player to a state
     */
    public State (ArrayList<Integer> g, ArrayList<Player> players){
        this.lines = g;
       // this.squares = Square.buildSquares(g);
        this.players = players;
    }

    /**
     * use this state constructor only for the current state
     */
   /* public void setLinesAndSquares(ArrayList<Line> lines, ArrayList<Square> squares) {
        this.lines = lines;
        this.squares = squares;
    }*/
    public void setLines(ArrayList<Integer> lines) {
        this.lines = lines;
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
        return currentState.getActualPlayer();
    }
    public static ArrayList<Player> getCurrentPlayers(){
        return currentState.getPlayers();
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
      //  System.out.println("parent");
        this.display();
            for (int line : this.lines) {
                   State child = computeAChild(line);
                  // child.display();
                   result.add(child);
            }

        this.children=result;
    }

    /*public void increaseScore(Player player) {
        this.scores.set(player.getIndex(), +1);
    }*/

    public void display(){
        for(int l : lines){
            System.out.print(l+", ");
        }
        System.out.println();
    }

    public static State currentState() {
        return currentState;
    }

    public static void setCurrentState(State currentState) {
        State.currentState = currentState;
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
    public static ArrayList<Integer> cloned(ArrayList<Integer> lines){
        ArrayList<Integer> result = new ArrayList<>();
        for(int line : lines){
            result.add(new Integer(line));
        }
        return result;
    }

    //finds the lines that needs to be colored for mcts
    public static int findDiffLine(ArrayList<Integer> state1, ArrayList<Integer> state2) {
        Integer randomEmptyLine=null;
        for (int line : state1) {
            if (!state2.contains(line)) {
                return line;
            }
        }
        if(randomEmptyLine==null) {
           // System.out.println("parent and child are identical");
            return state2.get(0);
        }
        return randomEmptyLine;
    }

    public static int findDiffLine(State state1, State state2) {
      //  System.out.println("state1");
        //state1.display();
        //System.out.println("state2");
        //state2.display();
        return findDiffLine(state1.getLines(),state2.getLines());
    }

    //clears a state
    public void reset(){
        this.getLines().clear();
        this.getPlayers().clear();
        if(children!=null) {
            this.getChildren().clear();
        }
        this.turn = 0;
    }

    public int numberOfAvailableMoves(){
        return lines.size();
    }


    public ArrayList<Integer> getAvailableMoves(){
       /* ArrayList<Line> newLines = new ArrayList<>();
        for(Line line : currentState().getLines()){
            if(line.isEmpty()){
                newLines.add(line);
            }
        }
        return newLines;*/
       return lines;
    }


    public int isEqual(State other){
        int nbOfDifferences = 0;
        for(Integer l : this.getLines()){
            if(!other.getLines().contains(l)){
                nbOfDifferences ++;
                //System.out.println("nbOfDifferences = " + l.getid());
            }
        }
        return nbOfDifferences;
    }

    public ArrayList<Integer> getLines() {
        return lines;
    }

   /* public ArrayList<Square> getSquares() {
        return squares;
    }*/

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

    public void setPlayers(ArrayList<Player> newPlayers, int turn) {
        this.players = newPlayers;
        this.turn = turn;
    }

    public int getScore(Player player) {
        //-1 is just an arbitrary value
        int result = -1;
        for (Player p: players) {
            if (p.getName().equals(player.getName())) {
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

    public int getValenceNb(int k) {
        int counter = 0;
        for(Square sq : GridController.getSquares()){
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
        else {
            return 0;
        }
    }


    public void nextTurn(){
        this.turn = this.turn+1;
    }

    public boolean isComplete() {
        return getScore(players.get(0)) + getScore(players.get(1)) == getLines().size()-2;
    }

    public State computeAChild(int line){

        State childState = this.cloned();

        childState.getLines().remove(new Integer(line));

        Controller.updateTurn(line, childState);

        return childState;
    }

}