package GameTree;

public class Tree {
    private State root;

    public void rebuild (){
        root = new State(View.GraphicLine.getReducedLines(), View.Square.getReducedSquare());
        extend(1);
    }

    //grow the tree deeper
    public void extend(int height){

    }

    //add visit graph methods

    public State getRoot(){
        return root;
    }
}
