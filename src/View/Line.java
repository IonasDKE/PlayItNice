package View;

import AI.Mcts;
import Controller.Controller;
import GameTree.Node;
import GameTree.State;
import Controller.*;

import java.io.IOException;
import java.util.ArrayList;

public class Line {

    public static boolean doQTraining = false;
    public int reward;
    public int currentState;
    public int nextState;
    private GraphicLine graphicLine;
    private int id;
    private boolean empty = true;
    private ArrayList<Square> squares= new ArrayList<>();


    public Line(int id, GraphicLine g){
        this.id= id;
        this.graphicLine = g;
    }

    public Line (int id,boolean empty ,ArrayList<Square> squares){
        this.id = id;
        this.empty=empty;
        this.squares=squares;
    }

    public Line (int id){
        this.id = id;
    }

    /**
     * this method is used to fill (color) a line, it is called on a single line and it
     * also switches the turn
     */
    public static boolean simulation = false;
    public static boolean runTesting =true;
    public void fill() throws IOException {
        int width= GridController.gridWidth;
        int height= GridController.gridHeight;
        Player actualPlayer = State.getCurrentActualPlayer();
        State.currentState().getLines().remove(Integer.valueOf(this.getId()));

        if (checkPhase()) {
            System.out.println("COEFFICIENT: "+Run.j);
            Node.COEFFICIENT = Run.j;
        }
        if (Controller.checkMove(this)) {

            //System.out.println("fill line "+this.id);
            this.setEmpty(false);
            State.currentState().getLines().remove(this);

            this.graphicLine.setStroke(actualPlayer.getColor());
            //this.graphicLine.setStroke(Color.BLACK);

            for (Square sq : this.getSquares()) {
                sq.colorSquare(actualPlayer);
            }
            Controller.updateTurn(this, State.currentState());
            Mcts.setNewRoots();

            if (simulation) {
                if (runTesting) {
                    if (Testing.checkEnd()) {
                        System.out.println("endGame");
                        int score = State.currentState().getPlayers().get(0).getScore();
                        Testing.scores.get(Testing.scores.size()-1).add(score);
                        if (score < (width*height)/2 +1) {
                            Testing.wins.get(Testing.wins.size()-1).add(0);
                        }else if ((width*height)%2==0 && score == (width*height)/2 +1){
                            Testing.wins.get(Testing.wins.size()-1).add(null);
                        }else
                            Testing.wins.get(Testing.wins.size()-1).add(1);

                        return;

                        //EndWindow.display(Launcher.thisStage);
                    } else {
                        Controller.checkAiPlay();
                    }
                }else {
                    if (Run.checkEnd()) {
                        System.out.println("endGame");
                        int score = State.currentState().getPlayers().get(0).getScore();
                        Run.scores.add(score);
                        if (score < (width*height)/2 +1) {
                            System.out.println(score);
                            Run.wins.add(0);
                        } else if ((width*height)%2==0 && score == (width*height)/2 +1){
                            System.out.println(score);
                            Run.wins.add(null);
                        }else {
                            System.out.println(score);
                            Run.wins.add(1);
                        }
                        return;

                        //EndWindow.display(Launcher.thisStage);
                    } else {
                        Controller.checkAiPlay();
                    }
                }
            } else {
                if (!Controller.checkEnd()) {
                    //checks if the next player to play is an AI, if it is the case, makes it play
                    Controller.updateComponents();
                    Controller.checkAiPlay();
                } else {
                    System.out.println("endGame");
                    //EndWindow.display(Launcher.thisStage);
                }
            }
        }
    }

    public static boolean checkPhase() {
        boolean toReturn = true;
        for (Square sq: GridController.getSquares()) {
           if (sq.getValence()>2)
               toReturn=false;

        }
        return toReturn;
    }

    /**
     * @return the id of a line
     */
    public int getId() {
        return id;
    }

    /**
     * @return true if a line has not been colored yet
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * @param empty takes a boolean and set a line to empty
     */
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }

    public ArrayList<Square> getClonedSquares(){
        ArrayList<Square> result = new ArrayList<>();
        for(Square a : this.getSquares()){
            result.add(a);
        }
        return result;
    }

    public void assignSquare(Square sq){
        this.squares.add(sq);
    }

    public void setSquares(ArrayList<Square> squares) {
        this.squares = squares;
    }

    /**
     * @return a cloned line
     */
    public Line cloned(){
        Line result = new Line(this.id, this.empty, this.getClonedSquares());
        return result;
    }

    /** this method prints all the informations about some lines
     * @param l  an array list which contains some lines
     */
    public static void display(ArrayList<Line> l) {
        for (Line line : l) {
            //if(line.isEmpty()) {
            System.out.print("line " + line.getId() + ", empty = " + line.isEmpty() + ", squares = ");
            for (Square s : line.getSquares()) {
                System.out.print(s.getid() + ", ");
            }
            System.out.println();
            // }
        }
    }

}