package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

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

}
