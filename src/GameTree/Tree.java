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

    //meant to rebuild the game tree a each turn
    public void rebuild (){
        ArrayList<GraphicLine> newLines = View.GraphicLine.getCloned();
        System.out.println();
        GraphicLine.display(newLines);
        root = new State(newLines);
        leaf.add(root);
        extend(1);
    }


    //grow the tree deeper
    public void extend(int height){
       for(int i =0; i<height; i++) {
           System.out.println("extend "+i);
           ArrayList<State> newLeafs = new ArrayList<>();

           for (State t : leaf) {
               System.out.println();
               System.out.println("Parent = ");
               t.display();
               ArrayList<State> children = t.getChildren();
               for (State s : children) {
                   newLeafs.add(s);
               }
           }

           leaf = newLeafs;
       }
    }

    //TO DO : add visit graph methods

    public State getRoot(){
        return root;
    }

    public ArrayList<State> getLeaf() {
        return leaf;
    }

}

