package GameTree;

import Controller.Controller;
import View.GraphicLine;
import View.Square;

import java.util.ArrayList;

public class State {
    public ArrayList<GraphicLine> getLines() {
        return lines;
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }

    private ArrayList<State> children;
    private ArrayList<GraphicLine> lines;
    private ArrayList<Square> squares;

    public State (ArrayList<GraphicLine> g){
        lines = g;
        squares = Square.buildSquares(g);
    }

    //get the children of the State
    public ArrayList<State> getChildren(){
        if(children==null){
            children = computeChildren();
        }
        return children;
    }

    private ArrayList<State> computeChildren(){
        ArrayList<State> result = new ArrayList<>();

        //children that would build a third line in a square are excluded
        for(GraphicLine line : lines){
            if(line.isEmpty() && !Controller.isThirdLine(line)) {
                addChild(line,result);
            }
        }

        //case if it is not possible to pick a line that will not be a third line
        if(result.size()==0) {
           // System.out.println("case 2");
            for (GraphicLine line : lines) {
                if(line.isEmpty()){
                    addChild(line,result);
                }
            }
        }
        return result;
    }

    private void addChild(GraphicLine line, ArrayList<State> children){
        ArrayList<GraphicLine> newLines = GraphicLine.getCloned(this.lines);
        GraphicLine.findLine(line.getid(),newLines).setEmpty(false);
        State childState = new State(newLines);
        children.add(childState);

        System.out.println();
        System.out.println("child "+children.size());
        childState.display();
    }

    public void display(){
        GraphicLine.display(this.getLines());
        Square.display(this.getSquares());
        System.out.println();
    }

    //TO DO : add state info methods
}
