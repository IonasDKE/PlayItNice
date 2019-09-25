package Controller;

import View.Player;
import javafx.scene.paint.Color;


public class Controller {

    private static Player playerOne = new Player(Color.GREEN, "one ");
    private static Player playerTwo = new Player(Color.BLUE, "two");
    private static Player[] players = {playerOne, playerTwo};

    public static Player[] getPlayers() {
        return players;
    }

    public static Boolean checkMove(View.GraphicLine line, View.Player player){
        int numberOfCompleteSquare;
        System.out.println("Player score " + player.getScore());
        //System.out.println(player);
        if (Color.valueOf("white") == line.getColor()) {

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
            if (numberOfCompleteSquare > 0)
                player.addMoves();
            else if (player.getMoves() != 0 && player.getMoves() > 0)
                player.decreaseMoves();



                //player.decreaseMoves();

            return true;
        }else {
            System.out.println("Movement not allowed");
            return false;
        }
    }

    private static int checkSquare(View.GraphicLine line, String direction){
        int[] dataForHorizontal = {-10,-20,-9,10,20,11};
        int[] dataForVertical = {-11,-1,9,10,1,-10};

        int counter=0;
        int numberOfSquare =0;

        if (direction.equals("horizontal")) {
            for (int i=0; i<6; i++){
                if(View.GraphicLine.findLine(Integer.toString(View.GraphicLine.getId(line)+dataForHorizontal[i])) != null &&
                        View.GraphicLine.findLine(Integer.toString(View.GraphicLine.getId(line)+dataForHorizontal[i])).getBoxOwner() != null){
                    counter++;
                    if (i == 2 && counter == 3){
                        numberOfSquare ++;
                        counter = 0;

                    }else if (counter == 3) {
                        numberOfSquare++;
                        counter = 0;
                    }

                }

            }
        }else if (direction.equals("vertical")) {
            for (int j=0; j<6; j++){
                if(View.GraphicLine.findLine(Integer.toString(View.GraphicLine.getId(line)+dataForVertical[j])) != null &&
                        View.GraphicLine.findLine(Integer.toString(View.GraphicLine.getId(line)+dataForVertical[j])).getBoxOwner() != null) {
                    counter++;

                    if (j == 2 && counter == 3) {
                        numberOfSquare++;
                        counter = 0;
                    }else if (counter == 3) {
                        numberOfSquare++;
                        counter = 0;
                    }
                }
            }
        }
        return numberOfSquare;
    }

}
