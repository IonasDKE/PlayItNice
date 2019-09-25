package Model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Shape {

    //Defines the shape ( line or box )
    Polygon poly;
    int width;
    int height;
    int centerX;
    int centerY;
    Color color;

    public Shape(){
        poly = new Polygon();
        width = 0;
        height = 0;
        centerX = 0;
        centerY = 0;
        color = Color.BLACK;
    }

    boolean isClickedAtRightLocation(int centerX, int centerY){
        return poly.contains(centerX - this.centerX - width / 2, centerY - this.centerY - height /2);
    }

}
