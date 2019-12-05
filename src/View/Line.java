package View;

import AI.Mcts;
import Controller.Controller;
import GameTree.State;
import GameTree.Node;

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

    public void fill(){

        Player actualPlayer = State.getCurrentActualPlayer();

        if (Controller.checkMove(this)) {
            //System.out.println("fill line "+this.id);
            this.setEmpty(false);

            this.graphicLine.setStroke(actualPlayer.getColor());

            for (Square sq : this.getSquares()) {
                sq.colorSquare(actualPlayer);
            }

            Controller.updateTurn(this, actualPlayer);
        }
    }

    public static ArrayList<Line> getLines(){
        return lines;
    }

    //return the id as an integer
    public int getid() {
        return id;
    }

    public boolean isEmpty() {
        return this.empty;
    }

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

    public Line cloned(){
        Line result = new Line(this.id, this.empty, this.getClonedSquares());
        return result;
    }

    public static void display(ArrayList<Line> l){
        for(Line line : l){
            System.out.print("line " + line.getid()+ ", filled = "+ line.isEmpty()+", squares = ");
            for(Square s :line.getSquares()){
                System.out.print(s.getid()+", ");
            }
            System.out.println();
        }
    }

    public void display() {
        System.out.println(this.empty+" "+ this.id);
    }

}