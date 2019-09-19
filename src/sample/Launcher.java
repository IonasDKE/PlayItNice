import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Launcher  extends Application {


    private final int WIDTH = 700;
    private final int HEIGHT = 800;
    private ComboBox selectPlayer;
    private String[] gameSizes = {"3 x 3", "3 x 2" , "4 x 4", "4 x 5", "5 x 6", "8 x 8"};
    private ObservableList<String> typeOfPlayer = FXCollections.observableArrayList("Select a player", "Human", "MiniMax", "...");
    private RadioButton[] radioButtons;
    private GridPane sizeBox;
    private Text sizeText;
    private ToggleGroup tg;

    public void start(Stage primaryStage) {

        Scene scene = new Scene(getContentPane(), WIDTH, HEIGHT);
        scene.getStylesheets().add("GUIstyle.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dots and Boxes");
        primaryStage.show();

    }


    public Label getEmptyLabel(int w, int h){
        Label emptyLabel = new Label();
        emptyLabel.setPrefSize(w, h);
        return emptyLabel;
    }

    private VBox getContentPane(){

        VBox pane = new VBox(20);
        pane.setAlignment(Pos.TOP_CENTER);

        FileInputStream input = null;
        try {
            input = new FileInputStream("/Users/Eliott/Desktop/TitleDotsAndBoxes.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        HBox initiateTitle = new HBox(imageView);
        initiateTitle.setAlignment(Pos.CENTER);


        pane.getChildren().add(getEmptyLabel(WIDTH,50));
        pane.getChildren().add(initiateTitle);
        pane.getChildren().add(getEmptyLabel(WIDTH,50));

        Text playerLabel = new Text("Opponent:");
        playerLabel.setUnderline(true);
        playerLabel.setFill(Color.WHITE);

        selectPlayer = new ComboBox(typeOfPlayer);
        selectPlayer.getSelectionModel().selectFirst();

        pane.getChildren().add(playerLabel);
        pane.getChildren().add(selectPlayer);

        pane.getChildren().add(getEmptyLabel(WIDTH,50));

        sizeBox = new GridPane();
        sizeBox.setHgap(20);
        sizeBox.setVgap(10);
        radioButtons = new RadioButton[gameSizes.length];
        sizeBox.setAlignment(Pos.CENTER);


        tg = new ToggleGroup();

        for(int i = 0 ; i < gameSizes.length; i++) {

            radioButtons[i] = new RadioButton(gameSizes[i]);
            radioButtons[i].setToggleGroup(tg);
            switch (i) {
                case 0:
                    sizeBox.add(radioButtons[i], 0, 0, 1, 1);
                    break;
                case 1:
                    sizeBox.add(radioButtons[i], 0, 1, 1, 1);
                    break;
                case 2:
                    sizeBox.add(radioButtons[i], 1, 0, 1, 1);
                    break;
                case 3:
                    sizeBox.add(radioButtons[i], 1, 1, 1, 1);
                    break;
                case 4:
                    sizeBox.add(radioButtons[i], 2, 1, 1, 1);
                    break;
                case 5:
                    sizeBox.add(radioButtons[i], 2, 0, 1, 1);
                    break;

            }
        }

        tg.selectToggle(null);
        sizeText =  new Text("Size of the Board:");
        sizeText.setUnderline(true);
        sizeText.setFill(Color.WHITE);



        pane.getChildren().add(sizeText);
        pane.getChildren().add(sizeBox);

        pane.getChildren().add(getEmptyLabel(WIDTH,50));


        Button startButton = new Button("Start Game");
        startButton.setAlignment(Pos.CENTER);
        pane.getChildren().add(startButton);



        return pane;
    }


}
