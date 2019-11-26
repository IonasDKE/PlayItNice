package GameTree;

import View.GraphicLine;
import View.Line;
import View.Square;

import java.util.ArrayList;

public class Tree {
    private Node root;
    private ArrayList<Node> leaf = new ArrayList<>();

    public Tree(){
        rebuild();
    }
    //constructor for the MCTS tree
    public Tree(Node node) {
        this.root=node;
    }

    //meant to rebuild the game tree a each turn
    public void rebuild (){

        root = new Node(State.currentState().cloned(), null);
        System.out.println("root = ");
        root.getState().display();

        leaf.add(root);
        extend(1);
    }

    //grow the tree deeper
    public void extend(int height){
       for(int i =0; i<height; i++) {
           long be = System.currentTimeMillis();
           System.out.println("extend "+i);
           ArrayList<Node> newLeafs = new ArrayList<>();

           for (Node parent : leaf) {
               System.out.println();
               System.out.println("Parent = ");
               parent.getState().display();
               ArrayList<State> children = parent.getState().getChildren();
               for (State s : children) {
                   newLeafs.add(new Node(s,parent));
               }
           }

           leaf = newLeafs;
           System.out.println("time :"+ (System.currentTimeMillis()-be)/1000+ " seconds; leaf size = "+leaf.size());

       }
    }

    public void changeRoot(Node newRoot) {
        this.root=newRoot;
    }

    //for MCTS, delets all the non-used subtree
    public void deleteParents(Node currentNode) {
        if (currentNode.getChildren().size()==0 ||
                (currentNode.getChildren().size()==1 && currentNode.getChildren().get(0)==this.root)) {
            currentNode=null;
        }else{
            for (Node n:currentNode.getChildren()) {
                deleteParents(n);
            }
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

