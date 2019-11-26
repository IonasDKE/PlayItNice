package View;

import Controller.Controller;
import GameTree.State;

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

    public Line (int id){
        this.id = id;
    }

    public void fill(){

        Player actualPlayer = Player.getActualPlayer();

        if (Controller.checkMove(this, actualPlayer)) {
            this.graphicLine.setStroke(actualPlayer.getColor());

            this.setEmpty(false);

            for (Square sq : this.getSquares()) {
                sq.colorSquare(actualPlayer);
            }

            Controller.updateTurn(this, actualPlayer);

            Controller.updateComponents();
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
        return empty;
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
        Line result = new Line(this.id);
        result.setEmpty(this.empty);
        result.setSquares(this.getClonedSquares());
        return result;
    }
    /*
    public ArrayList<Line> cloneLines(){
        ArrayList<Line> clone = new ArrayList<>();
        for (Line l : this) {
            Line result = new Line(l.id);
            result.setEmpty(l.empty);
            result.setSquares(l.getClonedSquares());
        }
        return clone;
    }
    */

    public static void display(ArrayList<Line> l){
        for(Line line : l){
            System.out.print("line " + line.getid()+ ", filled = "+ line.isEmpty()+", squares = ");
            for(Square s :line.getSquares()){
                System.out.print(s.getid()+", ");
            }
            System.out.println();
        }
    }

}