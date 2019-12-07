package View;

import AI.Mcts;
import Controller.Controller;
import GameTree.State;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Line {

    private GraphicLine graphicLine;
    private int id;
    private boolean empty = true;
    private ArrayList<Square> squares= new ArrayList<>();
    private static ArrayList<Line> lines = new ArrayList<>();

    public Line(int id, GraphicLine g){
        this.id= id;
        this.graphicLine = g;
        //State.currentState().getLines().add(this);
        lines.add(this);

    }

    public Line (int id,boolean empty ,ArrayList<Square> squares ){
        this.id = id;
        this.empty=empty;
        this.squares=squares;
    }

    public Line (int id){
        this.id = id;
    }

    /**
     * this method is used to fill (color) a line, it is called on a single line and it
     * also switches the turn
     */
    public void fill(){

        Player actualPlayer = State.getCurrentActualPlayer();

        if (Controller.checkMove(this)) {
            //System.out.println("fill line "+this.id);
            this.setEmpty(false);

            this.graphicLine.setStroke(actualPlayer.getColor());

            for (Square sq : this.getSquares()) {
                sq.colorSquare(actualPlayer);
            }

            Controller.updateTurn(this, State.currentState());

            Controller.updateComponents();

            if (Controller.checkEnd()) {
                System.out.println("endGame");
                EndWindow.display(Launcher.thisStage);
            } else {
                //checks if the next player to play is an AI, if it is the case, makes it play
                Mcts.setNewRoots();
                Controller.checkAiPlay();
            }
        }
    }

    /**
     * @return all the lines contained in the board
     */
    public static ArrayList<Line> getLines(){
        return lines;
    }

    //return the id as an integer

    /**
     * @return the id of a line
     */
    public int getid() {
        return id;
    }

    /**
     * @return true if a line has not been colored yet
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * @param empty takes a boolean and set a line to empty
     */
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }

    public ArrayList<Square> getClonedSquares(){
        ArrayList<Square> result = new ArrayList<>();
        for(Square a : this.getSquares()){
            result.add(a);
        }
        return result;
    }

    public void assignSquare(Square sq){
        this.squares.add(sq);
    }

    public void setSquares(ArrayList<Square> squares) {
        this.squares = squares;
    }

    /**
     * @return a cloned line
     */
    public Line cloned(){
        Line result = new Line(this.id, this.empty, this.getClonedSquares());
        return result;
    }

    /** this method prints all the informations about some lines
     * @param l  an array list which contains some lines
     */
    public static void display(ArrayList<Line> l){
        for(Line line : l){
            if(line.isEmpty()) {
                System.out.print("line " + line.getid() + ", empty = " + line.isEmpty() + ", squares = ");
                for (Square s : line.getSquares()) {
                    System.out.print(s.getid() + ", ");
                }
                System.out.println();
            }
        }
    }
    public void fillNoEffect() {
        this.empty=false;
    }

}