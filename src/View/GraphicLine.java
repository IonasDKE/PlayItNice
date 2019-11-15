package View;
import Controller.Controller;
import GameTree.Tree;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class GraphicLine extends Line {

    private static final int STROKE_WIDTH = 10;

    //Arraylist made for easily iterate through all the lines
    public final static ArrayList<GraphicLine> lines = new ArrayList<>();

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }

    public void setSquares(ArrayList<Square> squares) {
        this.squares = squares;
    }

    private boolean empty = true;
    private Color color;
    private ArrayList<Square> squares= new ArrayList<>();
    private int id;

    public boolean isEmpty() {
        return empty;
    }

    public GraphicLine(int x, int y, int a, int b, int id){
        super(x,y,a,b);
        this.id= id;
        this.setStroke(Color.WHITE);
        this.setStrokeWidth(STROKE_WIDTH);
        this.color = Color.valueOf("white");
        lines.add(this);
        setOnMouseClicked(event -> fill());
    }

    public GraphicLine(int id){
     this.id= id;
    }

    public void fill(){

        Player actualPlayer = Player.getActualPlayer();

        if (Controller.checkMove(this, actualPlayer)) {
            this.color = actualPlayer.getColor();
            this.setStroke(actualPlayer.getColor());
            this.empty= false;

            for( Square sq : this.getSquares()){
                sq.colorSquare(actualPlayer);
            }

            Controller.updateTurn(this,actualPlayer);

            Controller.updateComponents();
            //lines.remove(this);
        }
    }


    //return the id as an integer
    public int getid() {
        return id;
    }

    public static GraphicLine findLine(int id){
        return findLine(id,lines);
    }

    //find the line that as a certain id, return's that line
    public static GraphicLine findLine(int id, ArrayList<GraphicLine> lines) {
        GraphicLine lineToReturn = null;
        for (GraphicLine line : lines) {
            if (line.getid()==id)
                lineToReturn = line;
        }
        return lineToReturn;
    }



    public void assignSquare(Square sq){
            this.squares.add(sq);
    }

    public Color getColor() {
        return this.color;
    }

    public double evaluate(){
        return 0.0;
    }

    public static ArrayList<GraphicLine> getLines() {
        return lines;
    }

    public static ArrayList<GraphicLine> getCloned(ArrayList<GraphicLine> lines){
        ArrayList<GraphicLine> result = new ArrayList<>();
        for(GraphicLine line : lines){
            result.add(line.cloned());
        }
        return result;
    }

    public static ArrayList<GraphicLine> getCloned(){
        return getCloned(lines);
    }
    /*public static ArrayList<GraphicLine> getReducedLines(){
        return doHeuristics(lines);
    }

    private static ArrayList<GraphicLine> doHeuristics(ArrayList<GraphicLine> l) {
    ArrayList<GraphicLine> result = new  ArrayList();

    for (GraphicLine line: lines){
        //select all the remaining empty lines
        //if(line.isEmpty()){---
            result.add(line.cloned());
        //}
    }
    return result;
    }*/

    public static ArrayList<GraphicLine> getEmptyLines(){
        ArrayList<GraphicLine> result = new ArrayList();
        for(GraphicLine line : lines){
            if(line.isEmpty()){
                result.add(line);
            }
        }
        return  result;
    }

    public GraphicLine cloned(){
        GraphicLine result = new GraphicLine(this.id);
        result.setSquares(this.getSquares());
        return result;
    }
}