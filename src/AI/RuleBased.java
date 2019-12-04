package AI;

import Controller.Controller;
import GameTree.State;
import View.Launcher;
import View.Line;
import View.Player;
import View.Square;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import static Controller.Controller.countClaimedSquare;
import static Controller.Controller.isThirdLine;


public class RuleBased extends AISolver {
    @Override
    public Line nextMove(State board, int color) {

        //board.display();
        Line result = null;

        if( board.getNdValenceLines().size() != 0) {
            result = completeSquare(State.currentState().getSquares());
            if(result == null) {
                result = colorRandomLine(board);
            }
        }else{

            result = fillPhase();
        }

        return result;
    }

    //find all the different of channels, return them on a arraylist of arraylist of squares
    public static ArrayList<ArrayList<Square>> getChannels() {
        ArrayList<Square> visited = new ArrayList<>();
        ArrayList<Square> toBeVisited = State.currentState().getSquares();
        ArrayList result = new ArrayList();

        // while all the squares have not been visited
        while (toBeVisited.size() != 0) {
            ArrayList<Square> newChannel = new ArrayList();
            Square checkSq = toBeVisited.remove(0);

            visited.add(checkSq);
            ArrayList<Square> children = new ArrayList<>();

            if(!checkSq.isClaimed()) {
                result.add(newChannel);
                newChannel.add(checkSq);
                children.add(checkSq);
            }
            while (children.size() != 0) {
                for (Square s : goToNextSquares(toBeVisited, children.get(0))) {
                    if(!s.isClaimed()) {
                        newChannel.add(s);
                        children.add(s);
                    }
                    toBeVisited.remove(s);
                    visited.add(s);
                }
                children.remove(0);
            }
        }
        State.currentState().setSquares(visited);
        return result;
    }

    //return the number of channels
    public static int getChannelNb() {
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
                if (neighbouringSquare != s) {
                    children.add(neighbouringSquare);
                }
            }
        }
        return children;
    }

    //claim the squares who can be claimed
    public static Line completeSquare(ArrayList<Square> sqs) {
        Line result = null;
        for (Square sq : sqs) {
            if (sq.getValence() == 1) {
                //System.out.println("fill square");
                result = sq.getEmptyBorders().get(0);
            }
        }
        return result;
    }

    public static Line colorRandomLine(State s) {
        System.out.println("called random");
        Random rand = new Random();
        ArrayList<Line> lines = s.getNdValenceLines();
        int index = rand.nextInt(lines.size());
        //Line result = lines.get(index);
        //case 1 finds a line that doesnt give the opponent the opportunity to claim a square
        return lines.get(index);
    }

    public static Line fillPhase(){
        Line result = null;

        Random rand = new Random();

        System.out.println(" pick smallest channel");

        ArrayList<Square> smallestChannel = getSortedChannels().get(0);
        int randomSqIndex = rand.nextInt(smallestChannel.size());

        Square randomSq = smallestChannel.get(randomSqIndex);
        int randomLineIndex = rand.nextInt(randomSq.getEmptyBorders().size());

        result = randomSq.getEmptyBorders().get(randomLineIndex);
        return  result;
    }

    private static ArrayList<ArrayList<Square>> getSortedChannels(){
        return sort(getChannels());
    }
    private static ArrayList<ArrayList<Square>> sort(ArrayList<ArrayList<Square>> channels){
        channels.sort(new Comparator<ArrayList<Square>>() {
            @Override
            public int compare(ArrayList<Square> squares, ArrayList<Square> t1) {
                if(squares.size() > t1.size()){
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        return channels;
    }

    private static int pairScore(){

        int result = 0;
        boolean add = true;
        for(ArrayList<Square> a :  getSortedChannels()){
            if(add){
                result += a.size();
                add=false;
            }else{
                add= true;
            }
        }
        return result;
    }

    private static int impairScore(){

        int result = 0;
        boolean add = false;
        for(ArrayList<Square> a :  getSortedChannels()){
            if(add){
                result += a.size();
                add=false;
            }else{
                add= true;
            }
        }
        return result;
    }

<<<<<<< HEAD
}
=======
}


>>>>>>> master
