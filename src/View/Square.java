package View;

import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Square extends Rectangle {


    private static ArrayList<Square> squares = new ArrayList<>();
    private ArrayList<GraphicLine> borders = new ArrayList<>();
    private Rectangle rect;
    private int id;

    public Square(int x, int y, int size, int id){
        this.rect = new Rectangle(x,y, size,size);
        rect.setFill(Color.GRAY);
        this.id = id;
        squares.add(this);
    }

    public Square(int id){
        this.id = id;
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

    public static Square findSquare(int id){
        return findSquare(id, squares);
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
        boolean complete = false;
        if(this.getValence()==0){
            complete = true;
        }
        return complete;
    }


    public static void setSquares(ArrayList<Square> squares) {
        Square.squares = squares;
    }

    public void setBorders(ArrayList<GraphicLine> borders) {
        this.borders = borders;
    }

    //gets the squares from the lines !! arraylist squares of lines must not be empty
    public static ArrayList<Square> buildSquares(ArrayList<GraphicLine> lines){
        ArrayList<Square> result = new ArrayList<>();

        for(GraphicLine line : lines){

          for(int i =0; i<line.getSquares().size(); i++){
              Square a = line.getSquares().get(0);
              Square f = Square.findSquare(a.getid(),result);
              if(f ==null) {
                  f = new Square(a.getid());
                  result.add(f);
              }
                 line.getSquares().remove(0);
                 f.addBorder(line);
                 // adds also f in the squares arraylists of line. As a result a is replaced by a cloned version
          }
        }
        return result;
    }

    public int getValence(){
        int countLines = 0;
        for (GraphicLine line : this.getBorders()){
            if(line.isEmpty()){
                countLines++;
            }
        }
        return countLines;
    }

    public static void display(ArrayList<Square> sqs){
        for(Square s: sqs){
            System.out.print("square = " + s.getid()+", valence "+ s.getValence()+ ", borders = ");
            for(GraphicLine l : s.getBorders()){
                System.out.print(l.getid()+", "+l.isEmpty()+"; ");
            }
            System.out.println();
        }
    }
}