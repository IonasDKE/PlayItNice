package Controller;

import AI.Mcts;
import GameTree.State;
import AI.AISolver;
import AI.AlphaBeta;
import View.Player;
import View.*;
import javafx.scene.shape.Rectangle;


import java.util.ArrayList;
import java.util.Random;



public class Controller {

    public static AISolver firstSolver;
    public static Board board;
    public static State state;

    // checks if the line has already been claimed
    public static Boolean checkMove(View.Line line, Player player) {
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
    public static void updateTurn(View.Line line, Player player) {
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

            System.out.println("turn = " + turn + ", ai: " + Player.getPlayers().get(turn).isAi());
            System.out.println("channel " + Controller.getChannelNb());
            System.out.println();
            System.out.println();

            //checks if the next player to play is an AI, if it is the case, makes it play
            player = Player.getActualPlayer();
            if (player.isAi()) {
                //calls for the specific AI play method
                switch (player.getAiType()) {
                    case "End Square":
                        player.endSquarePlay();
                        break;
                    case "Alpha Beta":
                        firstSolver = new AlphaBeta();
                        firstSolver.nextMove(State.currentState(),turn).fill();
                        break;
                    case "Mcts":
                        player.mcts();
                        break;
                }
            }
        }

    }

    // check if any square has been claimed
    private static int checkAnySquareClaimed(View.Line line) {
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
        ArrayList<Square> toBeVisited = State.currentState().getSquares();
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
        State.currentState().setSquares(visited);
        return result;
    }

    //return the number of channels
    public static int getChannelNb(){
        return getChannels().size();
    }

    //returns all the neighouring squares of a given square
    private static ArrayList<Square> goToNextSquares(ArrayList<Square> sqs, Square s) {
        ArrayList<Square> children = new ArrayList<>();
        for (Line line : s.getEmptyInnerBorders()) {
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
        for (Line line : s.getEmptyInnerBorders()) {
            for (Square neighbouringSquare : line.getSquares()) {
                if (neighbouringSquare != s ) {
                    children.add(neighbouringSquare);
                }
            }
        }
        return children;
    }

    //claim the squares who can be claimed
    public static void completeSquare(ArrayList<Square> sqs) {
        boolean doRecursion = false;
        for (Square sq : sqs) {
            if (sq.getValence() == 1) {
                sq.colorSquare(Player.getActualPlayer());
                sq.getEmptyBorders().get(0).fill();
                doRecursion = true;
            }
        }
        if (doRecursion && !(countClaimedSquare() == (Launcher.getChosenM() * Launcher.getChosenN()))) {
            completeSquare(sqs);
        }
    }

    public static int completeSquareID(ArrayList<Square> sqs) {
        boolean doRecursion = false;
        for (Square sq : sqs) {
            if (sq.getValence() == 1) {
                sq.colorSquare(Player.getActualPlayer());
                doRecursion = true;
                return sq.getEmptyBorders().get(0).getid();

            }
        }
        if (doRecursion && !(countClaimedSquare() == (Launcher.getChosenM() * Launcher.getChosenN()))) {
            completeSquare(sqs);
        }
        return 0;
    }



    public static void colorRandomLine(ArrayList<Line> lines){
        Random rand = new Random();
        boolean lineHasNotBeenPicked = true;
        int index = 0;

        //case 1 finds a line that doesnt give the opponent the opportunity to claim a square
        while(lineHasNotBeenPicked && index< lines.size() ) {
            Line chosenLine = lines.get(index);
            if(chosenLine.isEmpty() && !isThirdLine(chosenLine)){
                System.out.println("fill "+ chosenLine.getid());
                chosenLine.fill();
                lineHasNotBeenPicked = false;
            }
            index++;
        }

        //case 2, picks up a random line
        while (lineHasNotBeenPicked && !(countClaimedSquare() == (Launcher.getChosenM() * Launcher.getChosenN()))) {
            int randomIndex = rand.nextInt( lines.size());
            if ( lines.get(randomIndex).isEmpty()) {
                System.out.println("fillR "+ lines.get(randomIndex).getid());
                lines.get(randomIndex).fill();
                lineHasNotBeenPicked = false;
            }
        }

    }

    //checks if claiming a the line will update any square to a valence of 1
    public static boolean isThirdLine(Line line){
        boolean result = false;

        for(Square sq : line.getSquares()){
            if(sq.getValence()==2){
                result=true;
            }
        }
        return result;
    }

    //counts the number of squares that players have claimed
    public static int countClaimedSquare(){
        int x =0;
        int count = 0;
        for(Square sq: State.currentState().getSquares()){
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
            Player player = Player.getActualPlayer();
            switch (player.getAiType()) {
                case "End Square":
                    player.endSquarePlay();
                    break;
                case "Alpha Beta":
                    firstSolver = new AlphaBeta();
                    firstSolver.nextMove(State.currentState(), turn).fill();
                    break;
                case "Mcts":
                    firstSolver=new Mcts();
                    player.mcts();
            }
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
                    winner=i;
                }

            }
        }
        Rectangle sq = new Rectangle();
        sq.setFill(Player.getPlayers().get(winner).getColor());
        sq.setWidth(75);
        sq.setHeight(75);
        sq.setTranslateY(30);
        return sq;
    }

    public static void setAlphaBeta() {
        firstSolver.nextMove(State.currentState(), turn).fill();
    }

    private static boolean firstTurn = true;
    public static void setMcts() {
        State state = new State(State.currentState().getLines());
        //state.display();
        if (firstTurn) {
            firstSolver = new Mcts();
            firstTurn=false;
        }else {
            firstSolver.setNewRoot(State.currentState());
            System.out.println("set new root");
        }
        //change the root at the end of human turn in the Mcts
        firstSolver.nextMove(state, turn);

    }

}