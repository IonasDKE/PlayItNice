
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;


import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Controller {
    @FXML
    Slider Slider_Hight;
    @FXML
    Slider Slider_Width;
    @FXML
    TextField Text_Hight;
    @FXML
    TextField Text_Width;

    public void hightPrint(){
        sliderPrint(Slider_Hight, Text_Hight);
    }
    public void widthPrint(){
        sliderPrint(Slider_Width, Text_Width);
    }

    private void sliderPrint(Slider slider, TextField text){
        slider.valueProperty().addListener( new InvalidationListener(){

            @Override
            public void invalidated(Observable observable) {
                text.setText(Double.toString((int)slider.getValue()));
                System.out.println("slider = " + slider.getValue());
            }
        });
    }

    public static Boolean checkMove(GraphicLine line, Player player){
        int numberOfCompleteSquare;
        //System.out.println(player);
        if (Color.valueOf("red") == line.getColor()) {

            //line is horizontal
            if (line.getStartX() != line.getEndX()) {
                numberOfCompleteSquare = checkSquare(line, "horizontal");
                player.addScore(numberOfCompleteSquare);

            } else { // line is vertical
                numberOfCompleteSquare = checkSquare(line, "vertical");
                player.addScore(numberOfCompleteSquare);

            }
            //System.out.println(numberOfCompleteSquare);
            if (numberOfCompleteSquare > 0 && player.getMoves() == 0)
                player.addMoves();

            return true;
        }else {
            System.out.println("Movement not allowed");
            return false;
        }
    }

    private static int checkSquare(GraphicLine line, String direction){
        int[] dataForHorizontal = {-10,-20,-9,10,20,11};
        int[] dataForVertical = {-11,-1,9,10,1,-10};

        int counter=0;
        int numberOfSquare =0;

        if (direction.equals("horizontal")) {
            for (int i=0; i<6; i++){
                //System.out.println(GraphicLine.getId(line)+dataForHorizontal[i]);
                //System.out.println(GraphicLine.findLine(Integer.toString(GraphicLine.getId(line)+dataForHorizontal[i])) );
                if(GraphicLine.findLine(Integer.toString(GraphicLine.getId(line)+dataForHorizontal[i])) != null &&
                        GraphicLine.findLine(Integer.toString(GraphicLine.getId(line)+dataForHorizontal[i])).getBoxOwner() != null){
                    counter++;
                    if (i == 2 && counter == 3){
                        numberOfSquare ++;
                        System.out.println("number 1 " + numberOfSquare);
                        counter = 0;

                    }else if (counter == 3) {
                        numberOfSquare++;
                        System.out.println("number 2 "+ numberOfSquare);
                        counter = 0;
                    }

                }

            }
        }else if (direction.equals("vertical")) {
            for (int j=0; j<6; j++){
                //System.out.println(GraphicLine.getId(line)+dataForHorizontal[j]);
                //System.out.println(GraphicLine.findLine(Integer.toString(GraphicLine.getId(line)+dataForHorizontal[j])) );
                if(GraphicLine.findLine(Integer.toString(GraphicLine.getId(line)+dataForVertical[j])) != null &&
                        GraphicLine.findLine(Integer.toString(GraphicLine.getId(line)+dataForVertical[j])).getBoxOwner() != null) {
                    counter++;

                    if (j == 2 && counter == 3) {
                        numberOfSquare++;
                        System.out.println("number 3 "+ numberOfSquare);
                        counter = 0;
                    }else if (counter == 3) {
                        numberOfSquare++;
                        System.out.println("number 4 "+ numberOfSquare);
                        counter = 0;
                    }
                }
            }
        }
        return numberOfSquare;
    }

}
