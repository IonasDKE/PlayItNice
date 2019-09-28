package Controller;

import View.Board;
import View.GraphicLine;
import View.Player;
import View.Square;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

public class Controller {

    // check if the line has already been picked
    public static Boolean checkMove(View.GraphicLine line, View.Player player) {
        //Square.display();
        if (line.isEmpty()) {
          return true;
        }else {
            System.out.println("Movement not allowed");
            return false;
        }
    }

    public static void updateTurn(View.GraphicLine line, View.Player player){
        int numberOfCompleteSquare = checkSquare(line);
        if (numberOfCompleteSquare > 0) {
            player.addScore(numberOfCompleteSquare);
        }
        if (numberOfCompleteSquare > 0){
            // update the player score label on the gui
        }
        else {
            player.decreaseMoves();
        }
    }

    // check if any square has been completed
    private static int checkSquare(View.GraphicLine line) {
        int squareNb = 0;
        for(Square sq : line.getSquares()){
            if(sq.isComplete()){squareNb++;}
        }
        System.out.println("squareNb = " + squareNb);
        return squareNb;
    }

    public static void updateComponents(){
        Player p = Player.getActualPlayer();
        Board.getPlayerNb().setText(p.getName());
        Board.getScores().get(Integer.parseInt(p.getName())-1).setText(Integer.toString(p.getScore()));
    }

    public static boolean checkEnding(){
        boolean empty = false;
        for(GraphicLine line : GraphicLine.getLines()){
            if(line.isEmpty()){empty=true;}
        }
        return empty;
    }

    public static int findChannelNb(){
        ArrayList<Square> visited  = new ArrayList<>();
        ArrayList<Square> toBeVisited = Square.getSquares();
        int ChannelNb=0;

        while(toBeVisited.size()!=0){
            ChannelNb++;
            Square checkSq = toBeVisited.remove(0);
            visited.add(checkSq);
            ArrayList<Square> children = new ArrayList<>();
            children.add(checkSq);
            //System.out.println("checkSq = " + checkSq.getid());
            while( children.size() !=0) {
                for(Square s : goToNextSquares(toBeVisited, children.get(0)) ){
                    toBeVisited.remove(s);
                    visited.add(s);
                    children.add(s);
                }
                children.remove(0);
            }
        }
        Square.setSquares(visited);
        return ChannelNb;
    }

    private static ArrayList<Square> goToNextSquares(ArrayList<Square> sqs, Square s){
        ArrayList<Square> children = new ArrayList<>();
        for(GraphicLine line : s.getEmptyInnerBorders()){
            for(Square neighbouringSquare : line.getSquares()){
                if(neighbouringSquare!=s && sqs.contains(neighbouringSquare)){
                    children.add(neighbouringSquare);
                }
            }
        }
        return children;
    }
  /* int numberOfCompleteSquare = 0;
            //line is horizontal
            if (line.getStartX() != line.getEndX()) {
                numberOfCompleteSquare = checkSquare(line, "horizontal");
                if (numberOfCompleteSquare > 0)
                    player.addScore(numberOfCompleteSquare);

            } else { // line is vertical
                numberOfCompleteSquare = checkSquare(line, "vertical");
                if (numberOfCompleteSquare > 0)
                    player.addScore(numberOfCompleteSquare);

            }
            //System.out.println(numberOfCompleteSquare);
            if (numberOfCompleteSquare > 0){

            }
            else
                player.decreaseMoves();

            System.out.println("Player moves " + player.getMoves());

            return true;
        }else {
            System.out.println("Movement not allowed");
            return false;
        }

    */


  /* private static int checkSquare(View.GraphicLine line, String direction){
        int[] dataForHorizontal = {-10,-20,-9,10,20,11}; //data needed to search if the is a square above/ below the current line
        int[] dataForVertical = {-11,-1,9,10,1,-10}; // data needed to search if there is a square left/right the current line

        int counter=0; // number of colored line around the current line
        int numberOfSquare =0;

        if (direction.equals("horizontal")) {
            for (int i=0; i<6; i++){
                if(View.GraphicLine.findLine(Integer.toString(View.GraphicLine.getId(line)+dataForHorizontal[i])) != null &&
                        View.GraphicLine.findLine(Integer.toString(View.GraphicLine.getId(line)+dataForHorizontal[i])).isEmpty() == false){
                    counter++;
                    if (i == 2 && counter == 3){ //if all the three lines, that form a square, ABOVE the current line are colored
                        numberOfSquare ++;
                        counter = 0;

                    }else if (counter == 3) {//if all the three lines, that form a square, BELOW the current line are colored
                        numberOfSquare++;
                        counter = 0;
                    }

                }

            }
        }else if (direction.equals("vertical")) {
            for (int j=0; j<6; j++){
                if(View.GraphicLine.findLine(Integer.toString(View.GraphicLine.getId(line)+dataForVertical[j])) != null &&
                        View.GraphicLine.findLine(Integer.toString(View.GraphicLine.getId(line)+dataForVertical[j])).isEmpty() == false) {
                    counter++;

                    if (j == 2 && counter == 3) {//if all the three lines, that form a square, on the RIGHT of the current line are colored
                        numberOfSquare++;
                        counter = 0;
                    }else if (counter == 3) {//if all the three lines, that form a square, on the left of the current line are colored
                        numberOfSquare++;
                        counter = 0;
                    }
                }
            }
        }
        return numberOfSquare;
    }*/

}
