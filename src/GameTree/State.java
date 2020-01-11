package GameTree;

import View.Launcher;
import Controller.Controller;
import Controller.GridController;
import RLearning.QTraining;
import View.Square;
import View.Player;
import RLearning.QTraining;

import java.util.ArrayList;
import java.util.Random;

public class State {

    private ArrayList<Player> players;
    private int turn;
    private static State currentState;
    private ArrayList<State> children;
    private ArrayList<Integer> lines;

    /**
     * @param g       set of line
     * @param players assign an array list which contains all the player to a state
     */
    public State(ArrayList<Integer> g, ArrayList<Player> players) {
        this.lines = g;
        // this.squares = Square.buildSquares(g);
        this.players = players;
    }

    public State() {

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
    public State(ArrayList<Player> players, int turn) {
        this.players = players;
        this.turn = turn;
    }

    /**
     * @return the player which is actually playing (which is why this is static)
     */
    public static Player getCurrentActualPlayer() {
        return currentState.getActualPlayer();
    }

    public static ArrayList<Player> getCurrentPlayers() {
        return currentState.getPlayers();
    }

    //get the children of the State
    public ArrayList<State> getChildren() {
        return this.children;
    }

    public ArrayList<State> computeAndGetChildren() {
        if (this.children == null) {
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

    public void computeChildren() {
        ArrayList<State> result = new ArrayList<>();
        ArrayList<Integer> symmetricMoves = new ArrayList<>();
        //System.out.println("parent");
        //this.display();
        for (int line : this.lines) {
            if (!isInArray(line, symmetricMoves))
                symmetricMoves.addAll(getAllSymmetricMoves(line));
                State child = computeAChild(line);
                result.add(child);
        }
        System.out.println("symmetric moves size: "+symmetricMoves.size());
        System.out.println();
        this.children=result;
    }

    public boolean isInArray(int line, ArrayList<Integer> array) {
        int counter=0;
        for (int arrayElement:array) {
            System.out.println("lien id: "+line+" array element: "+arrayElement);
            if (line==arrayElement) {
                counter++;
                return true;
            }
        }
        System.out.println(counter);
        return false;
    }


    public void display() {
        for (int l : lines) {
            System.out.print(l + ", ");
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
    public State cloned() {
        State result = new State(State.cloned(this.getLines()), Player.cloned(this.players));
        result.setTurn(this.getTurn());

        if (this.children != null) {
            result.setChildren(this.clonedChildren());
        }

        return result;
    }

    //returns a cloned arraylist of lines
    public static ArrayList<Integer> cloned(ArrayList<Integer> lines) {
        ArrayList<Integer> result = new ArrayList<>();
        for(int line : lines){
            result.add(line);
        }
        return result;
    }

    //finds the lines that needs to be colored for mcts
    public static int findDiffLine(ArrayList<Integer> state1, ArrayList<Integer> state2) {
        Integer randomEmptyLine = null;
        for (int line : state1) {
            if (!state2.contains(line)) {
                return line;
            }
        }
        if (randomEmptyLine == null) {
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
        return findDiffLine(state1.getLines(), state2.getLines());
    }

    //clears a state
    public void reset() {
        this.getLines().clear();
        this.getPlayers().clear();
        if (getChildren() != null) {

            this.getChildren().clear();
        }
        this.turn = 0;
    }

    public void setPlayable() {
        //currentState().setLines(GridController.getLinesIDs());
    }

    public int numberOfAvailableMoves() {
        return lines.size();
    }


    public ArrayList<Integer> getAvailableMoves() {

        return lines;
    }


    public int isEqual(State other) {
        int nbOfDifferences = 0;
        for (Integer l : this.getLines()) {
            if (!other.getLines().contains(l)) {
                nbOfDifferences++;
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
        for (Player p : players) {
            if (p.getName().equals(player.getName())) {
                return p.getScore();
            }
        }
        if (result == -1) {
            System.out.println("player not found");
        }
        return result;
    }

    public ArrayList<State> clonedChildren() {
        ArrayList<State> result = new ArrayList<>();
        for (State state : this.children) {
            result.add(state.cloned());
        }
        return result;
    }

    public int getValence(int k) {
        int counter = 0;
        for (Square sq : GridController.getSquares()) {
            if (sq.getValence() == k) {
                counter++;
            }
        }
        System.out.println(counter);
        return counter;
    }

    public static int inverseTurn(int turn) {
        if (turn == 0) {
            return 1;
        } else {
            return 0;
        }
    }


    public void nextTurn() {
        this.turn = this.turn + 1;
    }

    public boolean isComplete() {
        return getScore(players.get(0)) + getScore(players.get(1)) == getLines().size() - 2;
    }

    public State computeAChild(int line) {

        State childState = this.cloned();
        childState.getLines().remove(Integer.valueOf(line));
        Controller.updateTurn(line, childState);
        return childState;
    }

    public int getNextTurn(int turn) {
        if (turn == 1) {
            return 0;
        } else {
            return 1;
        }
    }

    public int getHashedID() {
        int id = 0;
        Random rand = new Random();

        for(int i = 0 ; i < QTraining.width * QTraining.height; i ++ ){
            id = id * QTraining.width;
            id += rand.nextInt(1);
        }
        return id;
    }

    public boolean isPlayable(int index) {
        /**
         * TODO
         * at a given state, check if the index is a valid mode
         * which means it checks if the line which correseponds to
         * the index is possible to play
         */
        for(Integer itg : getLines()){
            if(itg==index){
                return true;
            }
        }
        return false;
    }


   /*public void setScores(ArrayList<Integer> newScores) {
        for(Player p : players){
        Controller.updateTurn(line, childState);

        return childState;
    }

        /*
    public static void checkSymmetry(ArrayList<State> allStates) {
        for (State state: allStates) {
            allStates.removeIf(toCompare -> state != toCompare && rotationSymmetry(state,toCompare));
        }
    }

    public static boolean rotationSymmetry(State state,State toCompare) {
        for (int i=0; i<4; i++) {
            if (toCompare.getAllSymmeticMoves(i)==state.getLines()) {
                return true;
            }
        }
        return false;
    }

         */

    public static ArrayList<Integer> getAllSymmetricMoves(int id) {
        int gridHeight= Launcher.getChosenM();
        int gridWidth= Launcher.getChosenN();

        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Integer> twins = new ArrayList<>();
        //result.add(id);

        //System.out.println("Horizontal Twin");
        //Check if it's a horizontal symmetry
        if (toDozen(id) != gridHeight) {
            int twin = horizontal(id, gridHeight);
            result.add(twin);
            twins.add(twin);
            //System.out.println("add "+result.get(result.size()-1));
        }


        boolean pairHeight = toDozen(id) % 2 == 0;
        boolean pairWidth = gridWidth % 2 == 0;

        //System.out.println("Vertical Twin");
        //Check if it's a vertical symmetry
        if (pairWidth) {
            if (pairHeight || toUnits(id) != gridWidth / 2){
                int newTwin = vertical(id, gridWidth);
                if(!result.contains(newTwin)) {
                    result.add(newTwin);
                    twins.add(newTwin);
                    //System.out.println("add "+result.get(result.size()-1));
                }
            }

        } else {
            if (!pairHeight || toUnits(id) != (gridWidth - 1) / 2) {
                int newTwin = vertical(id, gridWidth);
                if(!result.contains(newTwin)) {
                    result.add(newTwin);
                    twins.add(newTwin);
                    //System.out.println("add "+result.get(result.size()-1));
                }
            }
        }

        //System.out.println("Diagonal Twin");
        //Check if it's a diagonal symmetry
        int size = result.size();
        for (int j = 0 ; j<size; j++){
            Integer i = result.get(j);
            if(downLeftCorner(i)){
                //  System.out.println("Twin down "+i);
                int newTwin = downDiagonalTwin(i, gridWidth) ;
                if(!result.contains(newTwin)) {
                    result.add(newTwin);
                    twins.add(newTwin);
                    //System.out.println("add "+result.get(result.size()-1));
                }
            }

            if(upperLeftCorner(i, gridWidth)){
                // System.out.println("Twin up "+i);
                int newTwin =upDiagonalTwin(i, gridWidth) ;
                if(!result.contains(newTwin)) {
                    result.add(newTwin);
                    twins.add(newTwin);
                    //System.out.println("add "+result.get(result.size()-1));
                }
            }
        }

        while(twins.size()!=0){
            int twin = twins.remove(0);

            //    System.out.println("Horizontal Twin");
            if (toDozen(twin) != gridHeight) {

                int newTwin = horizontal(twin, gridHeight);
                // System.out.println("newTwin = " + newTwin);
                if(!result.contains(newTwin)) {
                    result.add(newTwin);
                    twins.add(newTwin);
                    //System.out.println("add "+result.get(result.size()-1));
                }
            }

            pairHeight = toDozen(twin) % 2 == 0;
            pairWidth = gridWidth % 2 == 0;

            //   System.out.println("Vertical Twin");
            if (pairWidth) {
                if (pairHeight || toUnits(twin) != gridWidth / 2){
                    int newTwin = vertical(twin, gridWidth);
                    //  System.out.println("newTwin = " + newTwin);
                    if(!result.contains(newTwin)) {
                        result.add(newTwin);
                        twins.add(newTwin);
                        //System.out.println("add "+result.get(result.size()-1));
                    }
                }

            } else {
                if (!pairHeight || toUnits(twin) != (gridWidth - 1) / 2) {
                    int newTwin = vertical(twin, gridWidth);
                    //System.out.println("newTwin = " + newTwin);
                    if(!result.contains(newTwin)) {
                        result.add(newTwin);
                        twins.add(newTwin);
                        //System.out.println("add "+result.get(result.size()-1));
                    }
                }
            }

        }
        System.out.println("symmetric moves size: "+result.size());
        return result;
    }

    public static int horizontal(int id, int gridHeight){
        return id + 20 * (gridHeight - toDozen(id));
    }

    public static int vertical(int id, int gridWidth){
        boolean pairHeight = toDozen(id) % 2 == 0;
        double temp = gridWidth;
        int result =(int)(id + 2 *(temp/2 - toUnits(id)));

        if (pairHeight) {
            result-=1;
        }
        return result;
    }

    public static Integer downDiagonalTwin(int id, int gridWidth){
        int result = id;
        int stack = 1;
        boolean pairHeight = (toDozen(id)) % 2 == 0;
        while (!getDownDiagnalLines(gridWidth).contains(result)){
            if(pairHeight){
                result+=1;
            }else{
                result-=20;
            }
            stack++;
            // System.out.println("Move "+result);
        }

        result-=10;
        if(pairHeight){
            result+=1;
        }
        //  System.out.println("Switch "+result);

        for(int i = 1; i<stack ; i++){
            if(pairHeight){
                result-=20;
            }else{
                result+=1;
            }
            //  System.out.println("MoveBack "+ result);
        }

        return result;
    }

    public static Integer upDiagonalTwin(int id, int gridWidth){
        int result = id;
        int stack = 1;
        boolean pairHeight = (toDozen(id)) % 2 == 0;
        while (!getUpDiagonalLines(gridWidth).contains(result)){
            if(pairHeight){
                result+=1;
            }else{
                result+=20;
            }
            stack++;
            // System.out.println("Move "+result);
        }

        result+=10;
        if(pairHeight){
            result+=1;
        }
        //System.out.println("Switch "+result);

        for(int i = 1; i<stack ; i++){
            if(pairHeight){
                result+=20;
            }else{
                result+=1;
            }
            // System.out.println("MoveBack "+ result);

        }
        return result;
    }

    public static ArrayList<Square> computeUpDiagonalSquares(int gridWidth){
        ArrayList<Square> result = new ArrayList<>();
        // it is assumed that the grid is squared
        for (int i = 0 ; i<gridWidth; i++){
            result.add(GridController.squares.get((gridWidth+i*(gridWidth-1))-1));
            //System.out.println("index "+(gridWidth+i*(gridWidth-1))+ " id "+result.get(result.size()-1).getid());
        }
        return result;
    }

    public static ArrayList<Square> computeDownDiagonalSquares(int gridWidth){
        ArrayList<Square> result = new ArrayList<>();
        // it is assumed that the grid is squared
        for (int i = 0 ; i<gridWidth; i++){
            result.add(GridController.squares.get(i*(gridWidth+1)));
        }
        return result;
    }

    public static ArrayList<Integer> getDownDiagnalLines(int gridWidth){
        if (downDiagonalLines==null) {
            downDiagonalLines = new ArrayList<>();
            ArrayList<Square> squares = computeDownDiagonalSquares(gridWidth);
            for (Square s :squares){
                // System.out.println("Square "+s.getid());
                for (Integer i : s.getBordersIds()){
                    //System.out.println("Border "+i);
                    downDiagonalLines.add(i);
                }
                //System.out.println();
            }
        }
        return downDiagonalLines;
    }

    public static ArrayList<Integer> getUpDiagonalLines(int gridWidth){
        ArrayList<Integer> result = new ArrayList<>();
        if(upDiagonalLines==null){
            upDiagonalLines = new ArrayList<>();
            ArrayList<Square> squares = computeUpDiagonalSquares(gridWidth);
            for(Square s :squares){
                //   System.out.println("Square "+s.getid());
                for(Integer i : s.getBordersIds()){
                    //  System.out.println("Border "+i);
                    upDiagonalLines.add(i);
                }
                //  System.out.println();
            }
        }
        return upDiagonalLines;
    }

    public static boolean upperLeftCorner(int id, int gridWidth){
        double temp = toDozen(id);
        return (toUnits(id)+temp/2)<gridWidth;
    }

    public static boolean downLeftCorner(int id){
        double temp = toDozen(id);
        if(temp%2==0){
            temp = temp/2;
        }else{
            temp = (temp+1)/2;
        }
        return toUnits(id)< temp;
    }

    public static int toDozen(int id){
        return (id - toUnits(id)) / 10;
    }

    public static int toUnits(int id){
        return  id % 10;
    }

    private static ArrayList<Integer> downDiagonalLines ;
    private static ArrayList<Integer> upDiagonalLines ;


}