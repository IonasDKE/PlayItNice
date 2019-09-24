import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class GraphicLine extends Line {

    private static final int STROKE_WIDTH = 9;

    //Arraylist made for easily iterate through all the lines
    public final static ArrayList<GraphicLine> lines = new ArrayList<>();
    private boolean empty = true;

    public GraphicLine(int x, int y, int a, int b, int id){
        super(x,y,a,b);
        this.setId(Integer.toString(id));
        this.setStroke(Color.WHITE);
        this.setStrokeWidth(STROKE_WIDTH);
        lines.add(this);
        setOnMouseClicked(event -> fill());
        //setOnMouseEntered(event -> shade());
    }

    public static ArrayList<GraphicLine> getLines() {
        return lines;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void fill(){
        this.setStroke(Color.GREEN);
        System.out.println(this.idProperty());
        empty=false;
    }

    public void fillPlayer(Color color){
        this.setStroke(color);
        System.out.println(this.idProperty());
        empty=false;
    }

    public void shade(){
        this.setStroke(Color.LIGHTGREEN);
        this.setOpacity(95);
    }

}