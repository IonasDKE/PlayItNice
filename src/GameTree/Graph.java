package GameTree;
import java.util.ArrayList;

public abstract class Graph {
    public Node root;

    public void setNewRoot() {
        for (Node node : this.getLayer(1)){
            //System.out.println();
            //System.out.println("New State");
            //node.getState().display();

            int nbdiff = node.getState().isEqual(State.currentState());
            //System.out.println("nbdiff = " + nbdiff);

            if (nbdiff== 0) {
                this.setRoot(node);
                //System.out.println("mcts root changed");
                //node.getState().display();
            }
        }

    }

    public Node getRoot() {
        return this.root;
    }

    public void setRoot(Node newRoot) {
        this.root = newRoot;
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
<<<<<<< HEAD
}
=======
}
>>>>>>> 2d0ddf53d7dc7c70935bee3733646ba4cb787d00
