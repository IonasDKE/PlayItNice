package sample;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Label;


public class EndWindow extends Application {
    
    Stage mystage;
    Scene myscene;
    
    BorderPane bdrPn= new BorderPane();

    private final int WIDTH = 700;
    private final int HEIGHT = 800;
    final ImageView selectedImage = new ImageView();
    private Text sizeText1;
    private Text sizeText2;


    public static void main(String[] args){
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage){
        
        VBox root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);

        root.getChildren().add(getEmptyLabel(WIDTH,50));
        root.getChildren().add(getEmptyLabel(WIDTH,50));

        mystage=primaryStage;
        mystage.setTitle("EndFrame");
        mystage.setMinHeight(700);
        mystage.setMinWidth(800);
        myscene= new Scene(bdrPn,700,800);
        myscene.setFill(Color.BLACK);
        myscene.getStylesheets().add("sample/style.css");

        //GAME OVER IMAGES
        Image image1 = new Image("sample/GameOver.png");
        selectedImage.setImage(image1);
        root.getChildren().add(selectedImage);
        myscene.setRoot(root);


        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);

        //TEXTS
        sizeText1 =  new Text(150,300,"Score player 1");
        sizeText1.setFill(Color.MEDIUMAQUAMARINE);
        sizeText1.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));

        sizeText2= new Text(300,300,"Score player 2");
        sizeText2.setFill(Color.MEDIUMAQUAMARINE);
        sizeText2.setFont(Font.font("Helvetica", FontWeight.BOLD,30));
        hbox.getChildren().addAll(sizeText1,sizeText2);

        //Add space between the two texts
        hbox.setSpacing(100);

        // Adding Hbox into a Vbox
        root.getChildren().add(hbox);
        root.getChildren().add(getEmptyLabel(WIDTH,50));

        //Buttons
        HBox button=new HBox();
        button.setSpacing(5);
        button.setAlignment(Pos.BOTTOM_CENTER);
        Button startAgain= new Button("Try Again");
        startAgain.setAlignment(Pos.BOTTOM_CENTER);

        Button quit= new Button("Quit");
        quit.setAlignment(Pos.BOTTOM_RIGHT);

        Button menu= new Button("Menu");
        menu.setAlignment(Pos.BOTTOM_LEFT);
        button.getChildren().addAll(menu,startAgain,quit);
        root.getChildren().add(button);

        mystage.setScene(myscene);

        mystage.show();
    }


    public Label getEmptyLabel(int w, int h){
        Label emptyLabel = new Label();
        emptyLabel.setPrefSize(w, h);
        return emptyLabel;
    }
}
