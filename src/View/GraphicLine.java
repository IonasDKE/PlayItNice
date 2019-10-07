package View;
import Controller.Controller;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class GraphicLine extends Line {

    private static final int STROKE_WIDTH = 9;
    //Arraylist made for easily iterate through all the lines
    public final static ArrayList<GraphicLine> lines = new ArrayList<>();

    public ArrayList<Square> getSquares() {
        return squares;
    }

    private boolean empty = true;
    private Color color;
    private ArrayList<Square> squares= new ArrayList<>();

    public boolean isEmpty() {
        return empty;
    }

    public GraphicLine(int x, int y, int a, int b, int id){
        super(x,y,a,b);
        this.setId(Integer.toString(id));
        this.setStroke(Color.WHITE);
        this.setStrokeWidth(STROKE_WIDTH);
        this.color = Color.valueOf("white");
        lines.add(this);
        setOnMouseClicked(event -> fill());
    }

    public static ArrayList<GraphicLine> getLines() {
        return lines;
    }

    public void fill(){

        Player actualPlayer = Player.getActualPlayer();

        if (Controller.checkMove(this, actualPlayer)) {
            this.color = actualPlayer.getColor();
            this.setStroke(actualPlayer.getColor());
            this.empty= false;

            Controller.updateTurn(this,actualPlayer);

            for( Square sq : squares){
                sq.colorSquare(actualPlayer);
            }
           Controller.updateComponents();
            if (actualPlayer.getMoves() == 0) { Player.changeTurn(); }

            //System.out.println(Controller.findChannelNb());
        }
    }


    //return the id as an integer
    public static int getId(GraphicLine line) {
        return Integer.parseInt(line.getId());
    }

    //find the line that as a certain id, return's that line
    public static GraphicLine findLine(String id) {
        GraphicLine lineToReturn = null;
        for (GraphicLine line : lines) {
            if (line.getId().equals(id))
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


}