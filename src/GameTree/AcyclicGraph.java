package GameTree;

import GameTree.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AcyclicGraph extends Graph{
    private final Node trueRoot;
    public Node root;
    private static ArrayList<Integer> graphSize =new ArrayList<>();

    public AcyclicGraph(Node newRoot) {
        trueRoot=root=newRoot;
    }

    public Node getRoot() {
        return this.root;
    }

    public void setRoot(Node newRoot) {
        this.root= newRoot;
    }

    public void setNewRoot() {
        //tree.rootCheckExtend(2);

        for (Node node : this.root.getChildren()){
            //System.out.println();
            //System.out.println("New State");
            //node.getState().display();

            int nbdiff = node.getState().isEqual(State.currentState());
            //System.out.println("nbdiff = " + nbdiff);

            if (nbdiff== 0) {
                this.setRoot(node);
                System.out.println("mcts Acyclic graph root changed");
                //node.getState().display();
            }
        }
    }

}
