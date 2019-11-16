package GameTree;

import View.GraphicLine;
import View.Square;

import java.util.ArrayList;

public class Tree {
    private Node root;
    private ArrayList<Node> leaf = new ArrayList<>();

    public Tree(){
        rebuild();
    }

    //meant to rebuild the game tree a each turn
    public void rebuild (){
        ArrayList<GraphicLine> newLines = View.GraphicLine.getCloned();
        System.out.println();
        GraphicLine.display(newLines);
        root = new Node(new State(newLines), null);
        leaf.add(root);
        extend(1);
    }


    //grow the tree deeper
    public void extend(int height){
       for(int i =0; i<height; i++) {
           System.out.println("extend "+i);
           ArrayList<Node> newLeafs = new ArrayList<>();

           for (Node n : leaf) {
               System.out.println();
               System.out.println("Parent = ");
               n.getState().display();
               ArrayList<State> children = n.getState().getChildren();
               for (State s : children) {
                   newLeafs.add(new Node(s,n));
               }
           }

           leaf = newLeafs;
       }
    }

    //TO DO : add visit graph nodes methods

    public Node getRoot(){
        return root;
    }

    public ArrayList<Node> getLeaves() {
        return leaf;
    }

}

