package Controller;

import View.*;



import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    // check if the line has already been picked
    public static Boolean checkMove(View.GraphicLine line, View.Player player) {
        //Square.display();
        if (line.isEmpty()) {
            return true;
        } else {
            System.out.println("Movement not allowed");
            return false;
        }
    }

    public static void updateTurn(View.GraphicLine line, View.Player player) {
        int numberOfCompleteSquare = checkSquare(line);
        if (numberOfCompleteSquare > 0) {
            player.addScore(numberOfCompleteSquare);
        } else {
            player.decreaseMoves();
        }

    }

    // check if any square has been completed
    private static int checkSquare(View.GraphicLine line) {
        int squareNb = 0;
        for (Square sq : line.getSquares()) {
            if (sq.isComplete()) {
                squareNb++;
            }
        }
        System.out.println("squareNb = " + squareNb);
        return squareNb;
    }

    //First AI
    public static void updateComponents() {
        Player p = Player.getActualPlayer();
        System.out.println(countSquare());
        if (Launcher.pOneAI == true && Launcher.pTwoAI == true) {
            if (p.getTurn() == 1 || p.getTurn() == 2) {
                       completeSquare();
              }
            }

        if (Launcher.pTwoAI == true) {
            if (p.getTurn() == 2) {
                completeSquare();
            }
        }

        if (Launcher.pOneAI == true) {
            if (p.getTurn() == 1) {
                completeSquare();
            }
        }

        Board.getPlayerNb().setText(p.getName());
        Board.getScores().get(Integer.parseInt(p.getName()) - 1).setText(Integer.toString(p.getScore()));
    }

    public static boolean checkEnding() {
        boolean empty = false;
        for (GraphicLine line : GraphicLine.getLines()) {
            if (line.isEmpty()) {
                empty = true;
            }
        }
        return empty;
    }

    public static int findChannelNb() {
        ArrayList<Square> visited = new ArrayList<>();
        ArrayList<Square> toBeVisited = Square.getSquares();
        int ChannelNb = 0;

        while (toBeVisited.size() != 0) {
            ChannelNb++;
            Square checkSq = toBeVisited.remove(0);
            visited.add(checkSq);
            ArrayList<Square> children = new ArrayList<>();
            children.add(checkSq);
            //System.out.println("checkSq = " + checkSq.getid());
            while (children.size() != 0) {
                for (Square s : goToNextSquares(toBeVisited, children.get(0))) {
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

    private static ArrayList<Square> goToNextSquares(ArrayList<Square> sqs, Square s) {
        ArrayList<Square> children = new ArrayList<>();
        for (GraphicLine line : s.getEmptyInnerBorders()) {
            for (Square neighbouringSquare : line.getSquares()) {
                if (neighbouringSquare != s && sqs.contains(neighbouringSquare)) {
                    children.add(neighbouringSquare);
                }
            }
        }
        return children;
    }

    public static void completeSquare() {
         boolean found = false;

         for (Square sq : Square.getSquares()) {
                    if (sq.getBorders().get(0).isEmpty() && !(sq.getBorders().get(1).isEmpty()) && !(sq.getBorders().get(2).isEmpty()) && !(sq.getBorders().get(3).isEmpty())) {

                        sq.getBorders().get(0).fill();
                    }
                    if (!(sq.getBorders().get(0).isEmpty()) && (sq.getBorders().get(1).isEmpty()) && !(sq.getBorders().get(2).isEmpty()) && !(sq.getBorders().get(3).isEmpty())) {
                        sq.getBorders().get(1).fill();
                    }
                    if (!(sq.getBorders().get(0).isEmpty()) && !(sq.getBorders().get(1).isEmpty()) && (sq.getBorders().get(2).isEmpty()) && !(sq.getBorders().get(3).isEmpty())) {
                        sq.getBorders().get(2).fill();
                        ;
                    }
                    if (!(sq.getBorders().get(0).isEmpty()) && !(sq.getBorders().get(1).isEmpty()) && !(sq.getBorders().get(2).isEmpty()) && (sq.getBorders().get(3).isEmpty())) {
                        sq.getBorders().get(3).fill();
                    }
                }

                while (found == false && !(countSquare() == (Launcher.getChosenM() * Launcher.getChosenN()))) {
                    Random rand = new Random();
                    int randomIndex = rand.nextInt(GraphicLine.getLines().size());
                    if (GraphicLine.getLines().get(randomIndex).isEmpty()) {
                        GraphicLine.getLines().get(randomIndex).fill();
                        found = true;
                    }
                }

    }
    public static int countSquare(){
       int count = 0;
        for(Square sq: Square.getSquares()){
            if(sq.getEmptyLineNumber()== 0){
                count++;
            }
        }
        return count;
    }

}
