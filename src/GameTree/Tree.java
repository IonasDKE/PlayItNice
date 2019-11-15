package GameTree;

import View.GraphicLine;
import View.Square;

import java.util.ArrayList;

public class Tree {
    private State root;
    private ArrayList<State> leaf = new ArrayList<>();

    public Tree(){
        rebuild();
    }

    public void rebuild (){
        ArrayList<GraphicLine> newLines = View.GraphicLine.getCloned();
        root = new State(newLines, View.Square.buildSquares(newLines));
        leaf.add(root);
        extend(1);
    }

    //grow the tree deeper
    public void extend(int height){
       for(int i =0; i<height; i++) {
           ArrayList<State> newLeafs = new ArrayList<>();

           for (State t : leaf) {
               for (State s : t.getChildren()) {
                   newLeafs.add(s);
               }
           }

           leaf = newLeafs;
       }
    }

    //add visit graph methods

    public State getRoot(){
        return root;
    }
}
