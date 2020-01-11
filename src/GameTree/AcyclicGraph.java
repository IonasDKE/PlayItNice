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


<<<<<<< HEAD
}
=======
}
>>>>>>> 2d0ddf53d7dc7c70935bee3733646ba4cb787d00
