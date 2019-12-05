package Controller;

import GameTree.State;
import View.Player;
import View.*;
import javafx.scene.shape.Rectangle;


public class Controller {


    //stores the information of which player's turn it is
    public static int turn = 0;
    private static State state = State.currentState();

    // checks if the line has already been claimed
    public static Boolean checkMove(View.Line line) {
        if (line.isEmpty()) {
            return true;
        } else {
            System.out.println("Movement not allowed");
            return false;
        }
    }


    // decreases the player's moves in case he hasn't claimed any square and adds a score to the player in case he has
    // claimed a square, a move is implicitely added to the player as its moves haven't been decreased
    public static void updateTurn(View.Line line, Player player) {
        int numberOfCompleteSquare = checkAnySquareClaimed(line);
        if (numberOfCompleteSquare > 0) {
            player.addScore(numberOfCompleteSquare);
        } else {
            player.decreaseMoves();
        }

        updateComponents();

        if (player.getMoves() == 0) {
            player.addMoves();
            if (turn < Player.getActualPlayers().size() - 1) {
                turn++;
            } else {
                turn = 0;
            }
        }
        /*
            System.out.println();
            System.out.println();
            System.out.println("turn = " + turn + ", ai: " + Player.getActualPlayer().isAi());
            System.out.println("channel " + Controller.getChannelNb());
            System.out.println();
        */
        if (checkEnd()) {
            System.out.println("endGame");
            EndWindow.display(Launcher.thisStage);
        }else {
            //checks if the next player to play is an AI, if it is the case, makes it play
            checkAiPlay();
        }
    }

    public static void checkAiPlay(){
        Player player = Player.getActualPlayer();
        if (player.isAi()) {
            //calls for the specific AI play method
            //System.out.println("player = " + player.getAiType());
            player.aiPlay();
        }else{
            // System.out.println("player = " + player.getAiType()+ " "+ player.getName());
        }
    }

    // check if any square has been claimed
    public static int checkAnySquareClaimed(View.Line line) {
        int squareNbBefore = 0;
        int squareNbAfter = 0;
        line.setEmpty(true);
        for (Square sq : line.getSquares()) {
            if (sq.isClaimed()) {
                squareNbBefore++;
            }
        }
        line.setEmpty(false);
        for (Square sq : line.getSquares()) {
            if (sq.isClaimed()) {
                squareNbAfter++;
            }
        }
        //System.out.println("squareNb = " + (squareNbAfter-squareNbBefore));
        return squareNbAfter-squareNbBefore;
    }

    //update of gui labels of the playing frame
    public static void updateComponents() {
        Player p = Player.getActualPlayer();

        Board.getPlayerNb().setText(p.getName());
        Board.getScores().get(Integer.parseInt(p.getName()) - 1).setText(Integer.toString(p.getScore()));
    }


    //checks if claiming the line will update any square to a valence of 1
    public static boolean isThirdLine(Line line) {
        boolean result = false;

        for (Square sq : line.getSquares()) {
            if (sq.getValence() == 2) {
                result = true;
            }
        }
        return result;
    }

    //counts the number of squares that players have claimed
    public static int countClaimedSquare() {
        int count = 0;
        for (Square sq : State.currentState().getSquares()) {
            if (sq.getValence() == 0) {
                count++;
            }
        }
        return count;
    }


    //check if the game has ended
    public static boolean checkEnd() {
        if (countClaimedSquare() == (Launcher.getChosenM() * Launcher.getChosenN())) {
            return true;
        } else {
            return false;
        }
    }

    //update the gui components of the ending frame
    public static Rectangle setWinner() {
        int winner = 0;
        //Player winner = Player.getPlayers().get(0);
        int max = 0;
        if (checkEnd()) {
            for (int i = 0; i < Player.getActualPlayers().size(); i++) {
                if (max < Player.getActualPlayers().get(i).getScore()) {
                    max = Player.getActualPlayers().get(i).getScore();
                    winner = i;
                }

            }
        }
        Rectangle sq = new Rectangle();
        sq.setFill(Player.getActualPlayers().get(winner).getColor());
        sq.setWidth(75);
        sq.setHeight(75);
        sq.setTranslateY(30);
        return sq;
    }


}
