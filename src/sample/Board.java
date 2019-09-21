package sample;


import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class Board {

    public final static int Board_Hight = 700;
    public final static int Board_Width = 700;

    public static Scene makeBoard(int width, int hight){
        BorderPane mainPane = new BorderPane();
        HBox top = new HBox();
        Text turn = new Text("It is now the turn of player: ");
        top.getChildren().add(turn);

        Label playerNb = new Label("0");
        playerNb.setId("label_player_nb");
        top.getChildren().add(playerNb);

        mainPane.setTop(top);

        mainPane.setCenter(makeGrid(width,hight));


        Scene newScene = new Scene(mainPane,Board_Width, Board_Hight );
        return newScene;
    }


    private static Pane  makeGrid( int width, int hight){
        int dotSize = 4;
        Pane pane = new Pane();
        int squareSize = 300/Integer.max(width,hight);

        //build the horizontal lines
        for(int h = 0; h<=hight; h++){
            for(int w=0; w<width; w++){
                pane.getChildren().add(new GraphicLine(w*squareSize,h*squareSize ,w*squareSize+squareSize, h*squareSize, 2*10*h+w));
                pane.getChildren().add(new Circle(w*squareSize,h*squareSize, dotSize, Color.BLACK));
                pane.getChildren().add(new Circle(w*squareSize+squareSize,h*squareSize, dotSize, Color.BLACK));
            }
        }

        //build the vertical lines
        for(int h = 0; h<hight; h++) {
            for (int w = 0; w <= width; w++) {
                pane.getChildren().add(new GraphicLine(w*squareSize,h*squareSize ,w*squareSize, h*squareSize+squareSize, 2*10*h+10+w));
                pane.getChildren().add(new Circle(w*squareSize,h*squareSize, dotSize, Color.BLACK));
                pane.getChildren().add(new Circle(w*squareSize,h*squareSize+squareSize, dotSize, Color.BLACK));
            }
        }
        return pane;
    }
}
