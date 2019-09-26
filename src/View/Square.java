package View;

import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Square extends Rectangle {

    public final static ArrayList<Square> squares = new ArrayList<>();
    private final ArrayList<GraphicLine> borders = new ArrayList<>();
    private Rectangle rect;
    private int id;

    public Square(int x, int y, int size, int id){
        this.rect = new Rectangle(x,y, size,size);
        rect.setFill(Color.GRAY);
        this.id = id;
        squares.add(this);
    }

    public static ArrayList<Square> getSquares() {
        return squares;
    }

    public ArrayList<GraphicLine> getBorders() {
        return borders;
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

    public static void display(){
        for (Square sq : squares) {
            System.out.println(sq.id+"  sq = " + sq.borders.get(0).isEmpty()+sq.borders.get(1).isEmpty()+sq.borders.get(2).isEmpty()+sq.borders.get(3).isEmpty());

        }
    }

    //color a square form the color of a player
    public void colorSquare(Player player){
        if(this.isComplete()){
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
    public boolean isComplete(){
        boolean complete = true;
        for (GraphicLine line : borders){
            if (line.isEmpty()){ complete=false;}
        }
        return complete;
    }
}
