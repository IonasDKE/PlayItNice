package View;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class Board {

    public final static int Board_Hight = 700;
    public final static int Board_Width = 700;
    private final static int GRID_SIZE = 400;
    private static int xTranslation=(Board_Hight-GRID_SIZE)/2;
    private static int yTranslation=(Board_Width-GRID_SIZE)/2;
    private static Label playerNb;
    private static ArrayList<Text> scores = new ArrayList<>();

    public static ArrayList<Text> getScores() {
        return scores;
    }

    public static Label getPlayerNb() {
        return playerNb;
    }

    //private ArrayList<Shape> gridComponents = new ArrayList<>();

    public static Scene makeBoard(int width, int hight, ArrayList<Color> colors){

        Font gameFont = new Font(18);

        BorderPane mainPane = new BorderPane();
        HBox top = new HBox();
        Text turn = new Text("It is now the turn of player: ");
        turn.setFont(gameFont);
        turn.setFill(Color.WHITE);
        top.getChildren().add(turn);

        playerNb = new Label("1");
        playerNb.setFont(gameFont);
        playerNb.setTextFill(Color.WHITE);
        playerNb.setId("label_player_nb");
        top.getChildren().add(playerNb);
        top.setAlignment(Pos.CENTER);
        mainPane.setTop(top);

        mainPane.setCenter(makeGrid(width,hight));

        HBox bottom = new HBox();

        Text text = new Text("PLAYER SCORES       ");
        text.setFont(gameFont);
        text.setFill(Color.WHITE);
        bottom.getChildren().add(text);

        for(int i = 0; i< colors.size(); i++){

            new Player(colors.get(i), Integer.toString(i+1));

            Rectangle playerColor = new Rectangle(30,15);
            playerColor.setFill(colors.get(i));
            bottom.getChildren().add(playerColor);

            Text playerScore = new Text("0");
            playerScore.setFont(gameFont);
            playerScore.setFill(Color.WHITE);
            Label eq = new Label(" = ");
            eq.setFont(gameFont);
            eq.setTextFill(Color.WHITE);
            bottom.getChildren().add(eq);
            bottom.getChildren().add(playerScore);

            playerScore.setId(Integer.toString(i+1));
            scores.add(playerScore);
            Label space = new Label("     ");
            space.setFont(gameFont);
            bottom.getChildren().add(space);
        }
        bottom.setAlignment(Pos.CENTER);

        mainPane.setBottom(bottom);

        Scene newScene = new Scene(mainPane,Board_Width, Board_Hight);
        newScene.getStylesheets().add("GUIstyle.css");
        return newScene;
    }


    private static Pane  makeGrid( int width, int higth){
        int DOT_SIZE = 10;
        Pane pane = new Pane();
        int squareSize = GRID_SIZE/Integer.max(width,higth);

        //build the horizontal lines and the rectangles filling space between the lines
        for(int h = 0; h<=higth; h++){
            for(int w=0; w<width; w++){
                GraphicLine line = new GraphicLine(w*squareSize+xTranslation, h*squareSize+yTranslation, w*squareSize+squareSize+xTranslation, h*squareSize+yTranslation, 2*10*h+w);

                if(h!=higth){
                    Square sq = new Square(w*squareSize+xTranslation, h*squareSize+yTranslation,squareSize, 2*10*h+w);
                    pane.getChildren().add(sq.getRect());
                    sq.addBorder(line);
                }

                if(h!=0){Square.findSquare(2*10*(h-1)+w).addBorder(line);};

                pane.getChildren().add(line);
            }
        }

        //build the vertical lines and dots
        for(int h = 0; h<higth; h++) {
            for (int w = 0; w <= width; w++) {

                GraphicLine line = new GraphicLine(w*squareSize+xTranslation, h*squareSize+yTranslation, w*squareSize+xTranslation, h*squareSize+squareSize+yTranslation, 2*10*h+10+w);

                if(w!=width){Square.findSquare(2*10*h+w).addBorder(line);}
                if(w!=0){Square.findSquare(2*10*h+w-1).addBorder(line);}

                pane.getChildren().add(line);
                pane.getChildren().add(new Circle(w*squareSize+xTranslation, h*squareSize+yTranslation, DOT_SIZE, Color.RED));
                if(h==(higth-1)) { pane.getChildren().add(new Circle(w*squareSize+xTranslation, h*squareSize+squareSize+yTranslation, DOT_SIZE, Color.RED));}
            }
        }
        return pane;
    }


}