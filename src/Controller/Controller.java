package Controller;


import View.Player;
import View.*;



import java.util.ArrayList;
import java.util.Random;


public class Controller {

    // check if the line has already been picked
    public static Boolean checkMove(View.GraphicLine line, Player player) {
        //Square.display();
        if (line.isEmpty()) {
            return true;
        } else {
            System.out.println("Movement not allowed");
            return false;
        }
    }

    public static void updateTurn(View.GraphicLine line, Player player) {
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
            if (sq.isClaimed()) {
                squareNb++;
            }
        }
        System.out.println("squareNb = " + squareNb);
        return squareNb;
    }

    //update of gui labels
    public static void updateComponents() {
        Player p = Player.getActualPlayer();

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

    //find the number of channels
    public static ArrayList<ArrayList<Square>> findChannels() {

        ArrayList<ArrayList<Square>> result = new ArrayList<>();
        ArrayList<Square> visited = new ArrayList<>();
        ArrayList<Square> toBeVisited = Square.getSquares();

        while (toBeVisited.size() != 0) {

            ArrayList<Square> channel = new ArrayList<>();
            Square checkSq = toBeVisited.remove(0);
            channel.add(checkSq);
            visited.add(checkSq);
            ArrayList<Square> children = new ArrayList<>();
            children.add(checkSq);
            //System.out.println("checkSq = " + checkSq.getid());
            while (children.size() != 0) {
                for (Square s : goToNextSquares(toBeVisited, children.get(0))) {
                    toBeVisited.remove(s);
                    channel.add(s);
                    visited.add(s);
                    children.add(s);
                }
                children.remove(0);
            }
            result.add(channel);
        }

        Square.setSquares(visited);
        return result;
    }

    public static int channelNb(){
        return findChannels().size();
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

    //complete the squares who can be filled
    public static void completeSquare() {
        boolean ok = true;
        for (Square sq : Square.getSquares()) {
            if (sq.getEmptyBorders().size() == 1) {
                sq.colorSquare(Player.getActualPlayer());
                sq.getEmptyBorders().get(0).fill();
                ok = false;
            }
        }
        if (!ok && !(countSquare() == (Launcher.getChosenM() * Launcher.getChosenN()))) {
            completeSquare();
        }
    }
                /*while (found == false && !(countSquare() == (Launcher.getChosenM() * Launcher.getChosenN()))) {
                    Random rand = new Random();
                    int randomIndex = rand.nextInt(GraphicLine.getLines().size());
                    if (GraphicLine.getLines().get(randomIndex).isEmpty()) {
                        GraphicLine.getLines().get(randomIndex).fill();
                        found = true;
                    }
                }*/


    public static void colorRandomLine() {
        Random rand = new Random();
        boolean ok = true;
        int index = 0;

        while (ok == true && index < GraphicLine.getLines().size()) {
            //int randomIndex = rand.nextInt(GraphicLine.getLines().size());
            GraphicLine chosenLine = GraphicLine.getLines().get(index);
            if (chosenLine.isEmpty() && !isThirdLine(chosenLine)) {
                System.out.println("fill " + chosenLine.getId());
                chosenLine.fill();
                ok = false;
            }
            index++;
        }

        //case there is no line that could be filled avoiding to give a point to the oponent
        while (ok == true && !(countSquare() == (Launcher.getChosenM() * Launcher.getChosenN()))) {
            int randomIndex = rand.nextInt(GraphicLine.getLines().size());
            if (GraphicLine.getLines().get(randomIndex).isEmpty()) {
                System.out.println("fillR " + GraphicLine.getLines().get(randomIndex).getId());
                GraphicLine.getLines().get(randomIndex).fill();
                ok = false;
            }
        }
    }

    private static boolean isThirdLine(GraphicLine line) {
        boolean result = false;
        for (Square sq : line.getSquares()) {
            if (sq.getValence() <= 2) {
                result = true;
            }
        }
        return result;
    }

    public static int countSquare() {
        int count = 0;
        for (Square sq : Square.getSquares()) {
            if (sq.getValence() == 0) {
                count++;
            }
        }
        return count;
    }

    public static void aiStart() {
        if (Player.getActualPlayer().isAi()) {
            System.out.println("AI is starting");
            Player.getActualPlayer().endSquarePlay();
        }
    }

    public GraphicLine evaluate() {
        GraphicLine result = null;
        for (GraphicLine line : GraphicLine.getEmptyLines()) {
            if (result == null) { result = line; }
            if (result.evaluate() < line.evaluate()) { result = line;}
        }
        return result;
    }
}

