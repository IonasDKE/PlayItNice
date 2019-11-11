package View;

import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Square extends Rectangle {


    public static ArrayList<Square> squares = new ArrayList<>();
    private ArrayList<GraphicLine> borders = new ArrayList<>();
    private Rectangle rect;
    private int id;

    public Square(int x, int y, int size, int id){
        this.rect = new Rectangle(x,y, size,size);
        rect.setFill(Color.GRAY);
        this.id = id;
        squares.add(this);
    }

    private Square(Rectangle rect, int id){
        this.rect=rect;
        this.id=id;
    }

    public static ArrayList<Square> getSquares() {
        return squares;
    }

    public  ArrayList<GraphicLine> getBorders() {
        return borders;
    }

    //returns the borders of the square which are still empty
    public ArrayList<GraphicLine> getEmptyBorders(){
        ArrayList<GraphicLine> result = new ArrayList<>();
        for( GraphicLine line : borders){
            if(line.isEmpty()){result.add(line);}
        }
        return result;
    }

    //same as the above method but removing the borders which are edges of the main grid
    public ArrayList<GraphicLine> getEmptyInnerBorders(){
        ArrayList<GraphicLine> result = new ArrayList<>();
        for( GraphicLine line : borders){
            if(line.isEmpty() && line.getSquares().size()!=1 ){result.add(line);}
        }
        return result;
    }

    public Rectangle getRect() {
        return rect;
    }

    public int getid() {
        return id;
    }

    // adds a border reference to the square
    public void addBorder(GraphicLine line){
        if(borders.size()<4){
            borders.add(line);
            line.assignSquare(this);
        }else{
            System.out.println("borders are full");
        }
    }

    //find the square that as a certain id, return's that square
    public static Square findSquare(int id) {
        Square out= null;
        for (Square sq : squares) {
            if (sq.getid()==id)
                out = sq;
        }
        if(out==null){
            System.out.println("cannot find this square");
        }
        return out;
    }

    //color a square form the color of a player
    public void colorSquare(Player player){
        if(this.isClaimed()){
            //colouring pattern
            Stop[] stops = new Stop[] {
                    new Stop(0.2, Color.GRAY),
                    new Stop(0.5, player.getColor()),
                    new Stop(0.8, Color.GRAY)
            };
            LinearGradient linearGradient =
                    new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
            rect.setFill(linearGradient);
        }
    }

    //check if a square has been completed
    public boolean isClaimed(){
        boolean complete = true;
        for (GraphicLine line : borders){
            if (line.isEmpty()){ complete=false;}
        }
        return complete;
    }


    public static void setSquares(ArrayList<Square> squares) {
        Square.squares = squares;
    }

    public void setBorders(ArrayList<GraphicLine> borders) {
        this.borders = borders;
    }

    public Square cloned(){
        Square result = new Square(this.getRect(), this.id);
        result.setBorders(this.getBorders());
        return result;
    }

    public int getValence(){
        int countLines = 0;
        for (GraphicLine line : borders){
            if(line.isEmpty()){
                countLines++;
            }
        }
        return countLines;
    }
}