package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class GraphicLine extends Line {

    //Arraylist made for easily iterate through all the lines
    public final static ArrayList<GraphicLine> lines = new ArrayList<>();
    private boolean empty = true;

    public GraphicLine(int x, int y, int a, int b, int id){
        super(x,y,a,b);
        this.setId(Integer.toString(id));
        this.setStroke(Color.RED);
        lines.add(this);
    }

    public static ArrayList<GraphicLine> getLines() {
        return lines;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void fill(){
        this.setFill(Color.BLACK);
        empty=false;
    }
}
