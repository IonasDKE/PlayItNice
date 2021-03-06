/**
 * The GraphicLine class is a graphic element of the GUI that corresponds to the lines in the Dots and Boxes game.
 * Computations are performend on the Line class.
 */

package View;

import javafx.scene.paint.Color;

import java.io.IOException;


public class GraphicLine extends javafx.scene.shape.Line {

    private static final int STROKE_WIDTH = 10;

    private View.Line line;

    public GraphicLine(int x, int y, int a, int b, int id) {
        super(x, y, a, b);
        this.line = new View.Line(id,this);
        this.setStroke(Color.WHITE);
        this.setStrokeWidth(STROKE_WIDTH);
        setOnMouseClicked(event -> {
            try {
                this.getLine().fill();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public View.Line getLine() {
        return line;
    }
}