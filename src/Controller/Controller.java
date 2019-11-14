package Controller;

import View.Player;
import View.*;
import javafx.scene.shape.Rectangle;


import java.util.ArrayList;
import java.util.Random;


public class Controller {

    // checks if the line has already been claimed
    public static Boolean checkMove(View.GraphicLine line, Player player) {
        if (line.isEmpty()) {
            return true;
        } else {
            System.out.println("Movement not allowed");
            return false;
        }
    }

    //stores the information of which player's turn it is
    public static int turn = 0;

    // decreases the player's moves in case he hasn't claimed any square and adds a score to the player in case he has
    // claimed a square, a move is implicitely added to the player as its moves haven't been decreased
    public static void updateTurn(View.GraphicLine line, Player player) {
        int numberOfCompleteSquare = checkAnySquareClaimed(line);
        if (numberOfCompleteSquare > 0) {
            player.addScore(numberOfCompleteSquare);
        } else {
            player.decreaseMoves();
        }
        if (player.getMoves() == 0) {
            player.addMoves();
            if (turn < Player.getPlayers().size() - 1) {
                turn++;
            } else {
                turn = 0;
            }

            //System.out.println("turn = " + turn + ", ai: " + Player.getPlayers().get(turn).isAi());
            //System.out.println("channel " + Controller.getChannelNb());
            //System.out.println();
            //System.out.println();

            //checks if the next player to play is an AI, if it is the case, makes it play
            player = Player.getActualPlayer();
            if (player.isAi()) {
                //calls for the specific AI play method
                switch (player.getAiType()) {
                    case "End Square":
                        player.endSquarePlay();
                }
            }
        }

    }

    // check if any square has been claimed
    private static int checkAnySquareClaimed(View.GraphicLine line) {
        int squareNb = 0;
        for (Square sq : line.getSquares()) {
            if (sq.isClaimed()) {
                squareNb++;
            }
        }
        System.out.println("squareNb = " + squareNb);
        return squareNb;
    }

    //update of gui labels of the playing frame
    public static void updateComponents() {
        Player p = Player.getActualPlayer();

        Board.getPlayerNb().setText(p.getName());
        Board.getScores().get(Integer.parseInt(p.getName()) - 1).setText(Integer.toString(p.getScore()));
        if(checkEnd()){
            EndWindow.display(Launcher.thisStage);
        }
    }

    //find all the different of channels, return them on a arraylist of arraylist of squares
    public static ArrayList<ArrayList<Square>> getChannels() {
        ArrayList<Square> visited = new ArrayList<>();
        ArrayList<Square> toBeVisited = Square.getSquares();
        ArrayList result = new ArrayList();

        // while all the squares have not been visited
        while (toBeVisited.size() != 0) {
            ArrayList<Square> newChannel = new ArrayList();
            result.add(newChannel);
            Square checkSq = toBeVisited.remove(0);
            newChannel.add(checkSq);
            visited.add(checkSq);
            ArrayList<Square> children = new ArrayList<>();
            children.add(checkSq);

            while (children.size() != 0) {
                for (Square s : goToNextSquares(toBeVisited, children.get(0))) {
                    toBeVisited.remove(s);
                    newChannel.add(s);
                    visited.add(s);
                    children.add(s);
                }
                children.remove(0);
            }
        }
        Square.setSquares(visited);
        return result;
    }

    //return the number of channels
    public static int getChannelNb(){
    return getChannels().size();
    }

    //returns all the neighouring squares of a given square
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

    //returns all the neighouring squares of a given square
    private static ArrayList<Square> goToNextSquares(Square s) {
        ArrayList<Square> children = new ArrayList<>();
        for (GraphicLine line : s.getEmptyInnerBorders()) {
            for (Square neighbouringSquare : line.getSquares()) {
                if (neighbouringSquare != s ) {
                    children.add(neighbouringSquare);
                }
            }
        }
        return children;
    }

    //claim the squares who can be claimed
    public static void completeSquare() {
        boolean doRecursion = false;
        for (Square sq : Square.getSquares()) {
            if (sq.getValence() == 1) {
                sq.colorSquare(Player.getActualPlayer());
                sq.getEmptyBorders().get(0).fill();
                doRecursion = true;
            }
        }
        if (doRecursion && !(countClaimedSquare() == (Launcher.getChosenM() * Launcher.getChosenN()))) {
            completeSquare();
        }
    }

    public static void colorRandomLine(){
        Random rand = new Random();
        boolean lineHasNotBeenPicked = true;
        int index = 0;

        //finds a line that doesnt give the opponent the opportunity to claim a square
        while(lineHasNotBeenPicked && index<GraphicLine.getLines().size() ) {
            GraphicLine chosenLine = GraphicLine.getLines().get(index);
            if(chosenLine.isEmpty() && !isThirdLine(chosenLine)){
                System.out.println("fill "+ chosenLine.getId());
                chosenLine.fill();
                lineHasNotBeenPicked = false;
            }
            index++;
        }

        //picks up a random line
        while (lineHasNotBeenPicked && !(countClaimedSquare() == (Launcher.getChosenM() * Launcher.getChosenN()))) {
            int randomIndex = rand.nextInt(GraphicLine.getLines().size());
            if (GraphicLine.getLines().get(randomIndex).isEmpty()) {
                System.out.println("fillR "+GraphicLine.getLines().get(randomIndex).getId());
                GraphicLine.getLines().get(randomIndex).fill();
                lineHasNotBeenPicked = false;
            }
        }

    }

    //checks if claiming a the line will update any square to a valence of 1
    private static boolean isThirdLine(GraphicLine line){
        boolean result = false;
        for(Square sq : line.getSquares()){
            if(sq.getValence()<=2){
                result=true;
            }
        }
        return result;
    }

    //counts the number of squares that players have claimed
    public static int countClaimedSquare(){
        int count = 0;
        for(Square sq: Square.getSquares()){
            if(sq.getValence()== 0){
                count++;
            }
        }
        return count;
    }

    // checks if the first player to play is an ai type, if it is the case, it makes the ai play
    public static void aiStart(){
        if(Player.getActualPlayer().isAi()){
            System.out.println("AI is starting");
            Player.getActualPlayer().endSquarePlay();
        }
    }

    //check if the game has ended
    public static  boolean checkEnd(){
        if(countClaimedSquare() == (Launcher.getChosenM() * Launcher.getChosenN())){
            return true;
        }
        else{
            return false;
        }
    }

    //update the gui components of the ending frame
    public static Rectangle setWinner(){
         int winner =0;
        //Player winner = Player.getPlayers().get(0);
        int max = 0;
        if(checkEnd()){
            for(int i = 0 ; i < Player.getPlayers().size(); i++){
                if (max < Player.getPlayers().get(i).getScore()){
                    max = Player.getPlayers().get(i).getScore();
                    //winner = Player.getPlayers().get(i);
                winner=i;
                }

            }
           // Board.getPlayerNb().setText(winner.getName());

        }
        Rectangle sq = new Rectangle();
        sq.setFill(Player.getPlayers().get(winner).getColor());
        sq.setWidth(75);
        sq.setHeight(75);
        sq.setTranslateY(30);
       // Player.getPlayers().get(winner).getColor();
       return sq;

    }
}