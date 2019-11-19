package Model;

import View.GraphicLine;
import View.Line;

public class WeightedEdge {

    private int weight ;
    private Line line;

    WeightedEdge(Line line, int weight) {
        this.line = line ;
        this.weight = weight ;
    }

    public Line getLine(){
        return this.line;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public void setLine(Line line){
        this.line = line;
    }
}

