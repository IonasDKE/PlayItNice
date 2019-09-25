
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class GraphicLine extends Line {

    private static final int STROKE_WIDTH = 9;
    //Arraylist made for easily iterate through all the lines
    public final static ArrayList<GraphicLine> lines = new ArrayList<>();

    private Player playerOne = new Player(Color.GREEN, "one ");
    private Player playerTwo = new Player(Color.BLUE, "two");
    private Player[] players = {playerOne, playerTwo};
    private static int turn = 0;

    private Player  boxOwner;
    private Color color;

    public GraphicLine(int x, int y, int a, int b, int id){
        super(x,y,a,b);
        this.setId(Integer.toString(id));
        this.setStroke(Color.RED);
        this.setStrokeWidth(STROKE_WIDTH);
        this.color = Color.valueOf("red");
        this.boxOwner = null;
        lines.add(this);
        setOnMouseClicked(event -> fill());
    }

    public static ArrayList<GraphicLine> getLines() {
        return lines;
    }

    public void fill(){
        System.out.println("player " + players[turn].getPlayer_number());
        if (Controller.checkMove(this, players[turn])) {
            this.color = players[turn].getColor();
            this.setStroke(players[turn].getColor());
            this.boxOwner = players[turn];

            if (players[turn].getMoves() == 0)
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

    public static GraphicLine findLine(String id) {
        GraphicLine lineTorReturn = null;
        for (GraphicLine line : lines) {
            if (line.getId().equals(id))
                lineTorReturn = line;
        }
        return lineTorReturn;
    }

    public Player getBoxOwner() {
        return this.boxOwner;
    }

    public static void changeTurn() {
        if (turn == 0)
            turn = 1;
        else
            turn = 0;
    }

    public Color getColor() {
        return this.color;
    }

}