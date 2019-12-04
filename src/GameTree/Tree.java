package GameTree;

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
        //System.out.println("root = ");
        //root.getState().display();

        //leaf.add(root);
        //extend(1);
    }

    //grow the tree deeper
    public void extend(int height){
       for(int i =0; i<height; i++) {
           long be = System.currentTimeMillis();
           //System.out.println("extend "+i);
           ArrayList<Node> newLeafs = new ArrayList<>();

           for (Node parent : leaf) {
               //System.out.println();
               //System.out.println("Parent = ");
               //parent.getState().display();
                   ArrayList<Node> children = parent.computeAndGetChildren();
                   for (Node s : children) {
                       newLeafs.add(s);
                   }
           }
           System.out.println("newLeafs = " + newLeafs.size());
           leaf = newLeafs;
           //System.out.println("time :"+ (System.currentTimeMillis()-be)/1000+ " seconds; leaf size = "+leaf.size());

       }
    }

    public void rootCheckExtend(int height){
        ArrayList<Node> Leafs = new ArrayList<>();
        Leafs.add(this.root);

        for(int i =0; i<height; i++) {
            long be = System.currentTimeMillis();
            //System.out.println("extend "+i);
            ArrayList<Node> newLeafs = new ArrayList<>();

            for (Node parent : Leafs) {
                //System.out.println();
                //System.out.println("Parent = ");
                //parent.getState().display();
                if(!parent.hasChildren()) {
                    ArrayList<Node> children = parent.computeAndGetChildren();
                    for (Node s : children) {
                        newLeafs.add(s);
                        System.out.println("children = " + children.size());
                    }
                }
            }
            Leafs = newLeafs;
            //System.out.println("time :"+ (System.currentTimeMillis()-be)/1000+ " seconds; leaf size = "+leaf.size());
        }
    }

    //for MCTS, delets all the non-used subtree
    public void deleteRootParent() {
        Node parent = this.getRoot().getParent();
        for (Node childToDelete: parent.getChildren()) {
            if (childToDelete != this.getRoot())
                childToDelete=null;
        }
        this.getRoot().setParent(null);
        parent=null;

    }

    public void setRoot(Node newRoot) {
        this.root=newRoot;
    }

    public ArrayList<Node> getLayer(int layerNb) {
        ArrayList<Node> result = new ArrayList<>();
        result.add(this.root);

       // System.out.println();
       // System.out.println("layer root = ");
      //  root.getState().display();

        for (int i = 0; i < layerNb; i++) {
            ArrayList<Node> layer = new ArrayList<>(result);
            result.clear();
           // System.out.println("layer.size() = " + layer.size());
            for (Node n : layer) {
                if (!n.hasChildren()) {
                    n.computeChildren();
                    //System.out.println("comp");
                }
                for (Node nn : n.getChildren()) {
                    result.add(nn);
                }
            }
        }

        return result;
    }
    //TO DO : add visit graph nodes methods

    public Node getRoot(){
        return root;
    }

    public void setNewRoot(State state) {
        //tree.rootCheckExtend(2);

        for (Node node : this.getLayer(1)){
            //System.out.println();
            //System.out.println("New State");
            //node.getState().display();

            int nbdiff = node.getState().isEqual(state);

            if (nbdiff== 0) {
                this.setRoot(node);
                 System.out.println("Mcts.setNewRoot strange");
                 //node.getState().display();
            }
        }

    }
    public int treeSize=0;
    public void getTreeSize(Node node) {
        treeSize++;
        for (Node n:node.getChildren()) {
            getTreeSize(n);
        }

    }

     public ArrayList<Node> getLeaves() {
        return leaf;
    }

}

