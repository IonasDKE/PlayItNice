package Controller;

import View.*;
import View.Square;

import java.util.ArrayList;

public class GridController {
    public static ArrayList<Square> squares;
    public static ArrayList<Line> lines;

    public static void setLinesAndSquares(ArrayList<Line> l, ArrayList<Square> s) {
        lines = l;
        squares = s;
    }

    public static ArrayList<Square> getSquares() {
        return squares;
    }

    public static void setSquares(ArrayList<Square> sqs) {
        squares = sqs;
    }

    public static ArrayList<Line> getLines() {
        return lines;
    }

    //finds a square in the current game state
    public static Square findSquare(int id){
        return findSquare(id,squares);
    }

    //find the square that as a certain id, return's that square
    public static Square findSquare(int id, ArrayList<Square> sqs) {
        Square out= null;
        for (Square sq : sqs) {
            if (sq.getid()==id)
                out = sq;
        }
        if(out==null){
            //System.out.println("cannot find this square");
        }
        return out;
    }

    public static Line findLine(int id) {
        return findLine(id,lines);
    }
    //find the line that as a certain id, return's that line
    public static Line findLine(int id, ArrayList<Line> lines) {

        Line lineToReturn = null;
        for (Line line : lines) {
            if (line.getId() == id)
                lineToReturn = line;
        }
        return lineToReturn;
    }

    //find the line that as a certain id, return's that line
    public static Integer findIntLine(int id, ArrayList<Integer> lines) {

        Integer lineToReturn = null;
        for (Integer line : lines) {
            if (line == id)
                lineToReturn = line;
        }
        return lineToReturn;
    }
    public static ArrayList<Square> getSquares(int line){
        return findLine(line).getSquares();
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
    public static boolean isThirdLine(Integer line) {
        boolean result = false;

        for (Square sq : GridController.getSquares(line)) {
            if (sq.getValence() == 2) {
                result = true;
            }
        }
        return result;
    }
    public static ArrayList<Line> getNdValenceLines(){
        ArrayList<Line> result = new ArrayList<>();
        //System.out.println(" nd" + this.getLines().size());
        for (Line line: lines) {
            if (line.isEmpty() && !GridController.isThirdLine(line)) {
                result.add(line);
            }
        }
        return result;
    }
}
