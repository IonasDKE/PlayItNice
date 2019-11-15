package GameTree;

import View.GraphicLine;
import View.Square;

import java.util.ArrayList;

public class State {
    private ArrayList<GraphicLine> lines = new ArrayList<>();
    private ArrayList<Square> squares = new ArrayList<>();
    public State (ArrayList<GraphicLine> g, ArrayList<Square> s){
        lines = g;
        squares = s;
    }

    //add state info methodes
}
