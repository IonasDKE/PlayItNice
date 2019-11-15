package GameTree;

import Controller.Controller;
import View.GraphicLine;
import View.Square;

import java.util.ArrayList;

public class State {
    private ArrayList<State> children;
    private ArrayList<GraphicLine> lines = new ArrayList<>();
    private ArrayList<Square> squares = new ArrayList<>();

    public State (ArrayList<GraphicLine> g, ArrayList<Square> s){
        lines = g;
        squares = s;
    }

    public ArrayList<State> getChildren(){
        if(children==null){
            children = computeChildren();
        }
        return children;
    }

    private ArrayList<State> computeChildren(){
        ArrayList<State> result = new ArrayList<>();

        for(GraphicLine line : lines){
            if(line.isEmpty() && !Controller.isThirdLine(line)) {
                ArrayList<GraphicLine> newLines = GraphicLine.getCloned(this.lines);
                GraphicLine.findLine(line.getid(),newLines).setEmpty(false);
                State childState = new State(newLines,View.Square.buildSquares(newLines));
                result.add(childState);
            }
        }

        if(result.size()==0) {
            for (GraphicLine line : lines) {
                if(line.isEmpty()){
                    ArrayList<GraphicLine> newLines = GraphicLine.getCloned(this.lines);
                    GraphicLine.findLine(line.getid(),newLines).setEmpty(false);
                    State childState = new State(newLines,View.Square.buildSquares(newLines));
                    result.add(childState);
                }
            }
        }
        return result;
    }

    //add state info methodes
}
