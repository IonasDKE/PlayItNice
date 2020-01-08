package RLearning;

import GameTree.State;
import View.Line;
import View.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAgent extends Player {

    /**
     *  IMPLEMENT A RANDOM AGENT E.G A BOT WHICH WILL RANDOMLY SELECT MOVES
     */
    int turn;
    State state;

    public RandomAgent(int turn, State state){
        this.turn = turn;
        this.state = state;
    }

    public void reset() {
        this.state.reset();
    }

    public void move(){
         if(state.getAvailableMoves().size()!=0) {
        Random rand = new Random();
        ArrayList<Line> lines = this.state.getNdValenceLines();
        int index = rand.nextInt(lines.size());
        //Line result = lines.get(index);
        //case 1 finds a line that doesnt give the opponent the opportunity to claim a square
        try {
            lines.get(index).fill();
         } catch (IOException e) {
            e.printStackTrace();
         }
         }
    }


}
