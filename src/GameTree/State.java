package GameTree;

import View.Launcher;
import Controller.Controller;
import Controller.GridController;
import RLearning.QTraining;
import View.Square;
import View.Player;

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
        ArrayList<ArrayList<Integer>> symmetricStates = new ArrayList<>();
        int counter=0;


        for (int line : this.lines) {
            State child = computeAChild(line);
            if (!inList(GridController.getUnEmptyLines(child), symmetricStates)) {
                result.add(child);
                symmetricStates.addAll(checkStateSymmetry(GridController.getUnEmptyLines(child)));
            } else {
               counter++;
            }

        }

        System.out.println("pruned: "+counter);
        System.out.println("children size: "+result.size());
        this.children=result;
    }

    public ArrayList<ArrayList<Integer>> checkStateSymmetry(ArrayList<Integer> lines) {
        ArrayList<ArrayList<Integer>> symmetricState = new ArrayList<>();

        for (int i=0; i<4; i++) {
            symmetricState.add(new ArrayList<>());
            if (i==0) {
                symmetricState.get(symmetricState.size() - 1).addAll(verticalSymmetry(lines));
            } else {
                symmetricState.get(symmetricState.size() - 1).addAll(verticalSymmetry(combination(lines, i)));
            }

            symmetricState.add(new ArrayList<>());
            if (i==1) {
                symmetricState.get(symmetricState.size()-1).addAll(horizontalSymmetry(lines));
            }else {
                symmetricState.get(symmetricState.size()-1).addAll(horizontalSymmetry(combination(lines,i)));
            }

            symmetricState.add(new ArrayList<>());
            if (i==2) {
                symmetricState.get(symmetricState.size() - 1).addAll(diagonalUpSymmetry(lines));
            }else {
                symmetricState.get(symmetricState.size() - 1).addAll(diagonalUpSymmetry(combination(lines,i)));
            }

            symmetricState.add(new ArrayList<>());
            if (i==3) {
                symmetricState.get(symmetricState.size() - 1).addAll(diagonalDownSymmetry(lines));
            }else {
                symmetricState.get(symmetricState.size() - 1).addAll(diagonalDownSymmetry(combination(lines,i)));
            }
        }
        return symmetricState;
    }

    public ArrayList<Integer> combination(ArrayList<Integer> lines, int index) {
        if (index == 0) {
            return verticalSymmetry(lines);
        }else if (index==1) {
            return horizontalSymmetry(lines);
        }else if (index==2) {
            return diagonalUpSymmetry(lines);
        }else {
            return diagonalDownSymmetry(lines);
        }
    }

    public static void display(ArrayList<Integer> lines) {
        for (Integer line:lines) {
            System.out.print(line+", ");
        }
    }

    public boolean inList(ArrayList<Integer> lines, ArrayList<ArrayList<Integer>> symmetricStates) {
        for (ArrayList<Integer> setOfLines:symmetricStates) {
            boolean inList=true;
            for (Integer line: lines) {
                if (!setOfLines.contains(line)) {
                    inList= false;
                }
            }
            if (inList)
                return true;
        }
        return false;
    }

    public void display() {
        for (int l : lines) {
            System.out.print(l + ", ");
        }
        System.out.println();
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
        result.addAll(lines);
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

    public static ArrayList<Integer> verticalSymmetry (ArrayList<Integer> stateLines){
        ArrayList<Integer> result = new ArrayList<>();
        for(Integer id : stateLines){
            boolean pairHeight = toDozen(id) % 2 == 0;
            boolean pairWidth = gridWidth % 2 == 0;
            if (pairWidth) {
                if ( pairHeight || toUnits(id) != gridWidth / 2){
                    int newTwin = vertical(id);
                    result.add(newTwin);
                }else{
                    result.add(id);
                }

            } else {
                if ( !pairHeight || toUnits(id) != (gridWidth - 1) / 2) {
                    int newTwin = vertical(id);
                    result.add(newTwin);
                }else{
                    result.add(id);
                }
            }
        }
        return result;
    }

    public static ArrayList<Integer> horizontalSymmetry(ArrayList<Integer> stateLines){
        ArrayList<Integer> result = new ArrayList<>();
        for(Integer id : stateLines){
            if (toDozen(id) != gridHeight) {
                int twin = horizontal(id);
                result.add(twin);
            }
            else{
                result.add(id);
            }
        }
        return result;
    }

    public static ArrayList<Integer> diagonalUpSymmetry(ArrayList<Integer> stateLines){
        ArrayList<Integer> result = new ArrayList<>();
        for(Integer id : stateLines) {
            int newTwin = upDiagnalTwin(id);
            result.add(newTwin);
        }
        return result;
    }

    public static ArrayList<Integer> diagonalDownSymmetry(ArrayList<Integer> stateLines){
        ArrayList<Integer> result = new ArrayList<>();
        for(Integer id : stateLines) {
            int newTwin = downDiagnalTwin(id);
            result.add(newTwin);
        }
        return result;
    }


    public static ArrayList<Integer> rotated(int id) {
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Integer> twins = new ArrayList<>();
        result.add(id);

        int twin = horizontal(id);
        safeAdd(result,id,twin,twins);

        twin = vertical(id);
        safeAdd(result, id, twin, twins);

        int size = result.size();
        for(int j = 0 ; j<size; j++ ){
            Integer i = result.get(j);

            int newTwin = downDiagnalTwin(i) ;
            safeAdd(result, id, newTwin, twins);

            int newTwin2 = upDiagnalTwin(i) ;
            safeAdd(result, id, newTwin2, twins);
        }

        while(twins.size()!=0){
            int newTwin = twins.remove(0);

            newTwin = vertical(newTwin);

            safeAdd(result, id, newTwin);

            newTwin = horizontal(newTwin);

            safeAdd(result, id, newTwin);

        }
        return result;
    }

    public static void safeAdd(ArrayList<Integer> a, int id, int twin, ArrayList<Integer> twins){
        if(twin!=id) {
            if (a.contains(id)) {
                a.add(id);
                twins.add(twin);
                System.out.println("add "+a.get(a.size()-1));
            }
        }
    }

    public static void safeAdd(ArrayList<Integer> a, int id, int twin){
        if(twin!=id) {
            if (a.contains(id)) {
                a.add(id);
                System.out.println("add "+a.get(a.size()-1));
            }
        }
    }

    public static int horizontal(int id){
        return id + 20 * (gridHeight - toDozen(id));
    }

    public static int vertical(int id){
        boolean pairHeight = toDozen(id) % 2 == 0;
        double temp = gridWidth;
        int result =(int)(id + 2 *(temp/2 - toUnits(id)));

        if (pairHeight) {
            result-=1;
        }
        return result;
    }

    public static Integer downDiagnalTwin( int id){
        Integer result = new Integer(id);

        boolean downLeftCorner = downLeftCorner(id);
        int stack = 1;
        boolean pairHeight = (toDozen(id)) % 2 == 0;

        while (!getDownDiagonalLines().contains(result)){
            if(pairHeight){
                if(downLeftCorner) {
                    result += 1;
                }else{
                    result-= 1;
                }
            }else{
                if(downLeftCorner) {
                    result -= 20;
                }else{
                    result += 20;
                }
            }
            stack++;
            // System.out.println("Move "+result);
        }

        if(downLeftCorner) {
            result -= 10;
        }else{
            result += 10;
        }

        if(pairHeight && downLeftCorner){
            result+=1;
        }
        if(!pairHeight && !downLeftCorner){
            result-=1;
        }
        //  System.out.println("Switch "+result);

        for(int i = 1; i<stack ; i++){
            if(pairHeight){
                if(downLeftCorner) {
                    result -= 20;
                }else{
                    result += 20;
                }
            }else{
                if(downLeftCorner) {
                    result += 1;
                }else{
                    result -= 1;
                }
            }
            //  System.out.println("MoveBack "+ result);
        }

        return result;
    }

    public static Integer upDiagnalTwin( int id){
        Integer result = new Integer(id);
        int stack = 1;
        boolean upperLeftCorner = uppperLeftCorner(id);
        boolean pairHeight = (toDozen(id)) % 2 == 0;

        while (!getUpDiagnalLines().contains(result)){
            if(pairHeight){
                if(upperLeftCorner) {
                    result += 1;
                }else{
                    result -=1;
                }
            }else{
                if(upperLeftCorner) {
                    result += 20;
                }else{
                    result -= 20;
                }
            }
            stack++;
            // System.out.println("Move "+result);
        }

        if(upperLeftCorner) {
            result += 10;
        }else {
            result -= 10;
        }
        if(pairHeight && upperLeftCorner){
            result+=1;
        }
        if(!upperLeftCorner && !pairHeight){
            result -=1;
        }
        //System.out.println("Switch "+result);

        for(int i = 1; i<stack ; i++){
            if(pairHeight){
                if(upperLeftCorner) {
                    result += 20;
                }else{
                    result -= 20;
                }
            }else{
                if(upperLeftCorner) {
                    result += 1;
                }else {
                    result -= 1;
                }
            }
            // System.out.println("MoveBack "+ result);

        }
        return result;
    }
    public static  ArrayList<Square> computeUpDiagonalSquares(){
        ArrayList<Square> result = new ArrayList<>();
        // it is assumed that the grid is squared
        for(int i = 0 ; i<gridWidth; i++){
            result.add(GridController.squares.get((gridWidth+i*(gridWidth-1))-1));
            //System.out.println("index "+(gridWidth+i*(gridWidth-1))+ " id "+result.get(result.size()-1).getid());
        }
        return result;
    }

    public static ArrayList<Square> computeDownDiagonalSquares(){
        ArrayList<Square> result = new ArrayList<>();
        // it is assumed that the grid is squared
        for(int i = 0 ; i<gridWidth; i++){
            result.add(GridController.squares.get(i*(gridWidth+1)));
        }
        return result;
    }

    public static ArrayList<Integer> getDownDiagonalLines(){
        ArrayList<Integer> result = new ArrayList<>();
        if(downDiagonalLines==null){
            downDiagonalLines = new ArrayList<>();
            ArrayList<Square> squares = computeDownDiagonalSquares();
            for(Square s :squares){
                // System.out.println("Square "+s.getid());
                for(Integer i : s.getBordersIds()){
                    //System.out.println("Border "+i);
                    downDiagonalLines.add(i);
                }
                //System.out.println();
            }
        }
        return downDiagonalLines;
    }

    public static ArrayList<Integer> getUpDiagnalLines(){
        ArrayList<Integer> result = new ArrayList<>();
        if(upDiagonalLines==null){
            upDiagonalLines = new ArrayList<>();
            ArrayList<Square> squares = computeUpDiagonalSquares();
            for(Square s :squares){
                //   System.out.println("Square "+s.getid());
                //  System.out.println("Border "+i);
                upDiagonalLines.addAll(s.getBordersIds());
                //  System.out.println();
            }
        }
        return upDiagonalLines;
    }

    public static boolean uppperLeftCorner(int id){
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
    private static int gridWidth=Launcher.getChosenM();
    private static int gridHeight =Launcher.getChosenN();
}