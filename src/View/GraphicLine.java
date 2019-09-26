package View;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class GraphicLine extends Line {

    private static final int STROKE_WIDTH = 9;
    //Arraylist made for easily iterate through all the lines
    public final static ArrayList<GraphicLine> lines = new ArrayList<>();
    private static int turn = 0;

    private Player lineOwner;
    private Color color;

    public GraphicLine(int x, int y, int a, int b, int id){
        super(x,y,a,b);
        this.setId(Integer.toString(id));
        this.setStroke(Color.WHITE);
        this.setStrokeWidth(STROKE_WIDTH);
        this.color = Color.valueOf("white");
        this.lineOwner = null;
        lines.add(this);
        setOnMouseClicked(event -> fill());
    }

    public static ArrayList<GraphicLine> getLines() {
        return lines;
    }

    public void fill(){
        //System.out.println("player " + Controller.Controller.getPlayers()[turn].getMoves());
        if (Controller.Controller.checkMove(this, Player.getPlayers().get(turn))) {
            this.color = Player.getPlayers().get(turn).getColor();
            this.setStroke(Player.getPlayers().get(turn).getColor());
            this.lineOwner = Player.getPlayers().get(turn);

            if (Player.getPlayers().get(turn).getMoves() == 0)
                changeTurn();
        }
    }

    public void fillPlayer(Color color){
        this.setStroke(color);
        //System.out.println(this.idProperty());
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
    //return player that own's the line
    public Player getLineOwner() {
        return this.lineOwner;
    }

    public static void changeTurn() {
        if (turn < Player.getPlayers().size()-1) {
            Player.getPlayers().get(turn).addMoves();
            turn ++;
        }

        else {
            Player.getPlayers().get(turn).addMoves();
            turn = 0;
        }
    }

    public Color getColor() {
        return this.color;
    }

}