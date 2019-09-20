package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    Slider Slider_Hight;
    Slider Slider_Width;
    TextField Text_Hight;
    TextField Text_Width;

    public void sliderPrint(ActionEvent e, TextField field, Slider slider){
        field.setText(Double.toString((int)slider.getValue()));
    }

}
