package RLearning;

import GameTree.State;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.*;
import static java.lang.System.out;

public class QLearning {

    int numberOfState;
    int numberOfMoves;
    protected HashMap<Integer, double[]> q = new HashMap<Integer, double[]>();
    protected double Qinit;
    protected ArrayList<QVector> policyRecorder;
    protected double alpha;
    protected double gamma;

    /**

     * @param numberOfState number of states in the game
     * @param numberOfMoves
     * @param alpha the learning rate of the Q function
     * @param gamma the discount factor of the Q function
     * @param Qinit the initial Q we set
     */
    public QLearning(int numberOfState, int numberOfMoves, double alpha, double gamma, double Qinit) {
        this.numberOfState = numberOfState;
        this.numberOfMoves = numberOfMoves;
        this.Qinit = Qinit;
        // records the state + moves
        this.policyRecorder = new ArrayList<>();
        //learning rate
        this.alpha = alpha;
        //discount factor
        this.gamma = gamma;
        //computes the q values and set them in a hashmap
        //getQAndCSV(0);
    }

    /**
     * @param stateID represents the id of a state of a game
     * @return
     */
    public double[] getQAndCSV(int stateID){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new File("trainingRL.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        double [] sb = new double[(QTraining.height* QTraining.width)*2];
        out.println("coucou");
        //Checks if state has been computed
        if (q.containsKey(stateID)) {
            out.println("hey");

            sb = q.get(stateID);
        }
        else{
            // else we store the values of the state in the array
            for (int i = 0; i < sb.length; i++) {
                 sb[i] = getQinit();
                 writer.append(sb[i] + " ");
            }
            // and then we add them to the hashmap
            q.put(stateID, sb);

        }
        // and we write it to the CSV file
        writer.close();
        return sb;
    }

    public int getBestQLine(State state){
        //Uncomment to see the q hashmap
        //q.entrySet().forEach(entry->{
        //    System.out.println(entry.getKey() + " " + Arrays.toString(entry.getValue()));
        //});
        int getStateId = state.getHashedID();
        double[] qVals = getQAndCSV(getStateId);
        //loop until converges
        while(true){
            //gets the index of the maximum q value among all the q values
            int index = 0;
            double max = qVals[0];
            for (int i = 0; i < qVals.length ; i++){
                if(qVals[i]> max){
                    max = qVals[i];
                    index = i;
                }
            }
            //checks if the move is valid at the given state
            if(state.isPlayable(index)){
                return index;
            }
            //else set the q value to -1.0 (it has been computed)
            else{
                qVals[index] = -1.0;
            }
        }
    }

    public void update(State state){
        // selects the line of the current state (using the q table)
        int lineWhichHasBeenSelected = getBestQLine(State.currentState());
        //Maps a state to it's actions
        int stateID = State.currentState().getHashedID();
        policyRecorder.add(new QVector(stateID, lineWhichHasBeenSelected));
    }

    /**
     * @return the initial Q (which have to be instantiated
     */
    private double getQinit() {
        return this.Qinit;
    }

    /**
     * @param reward  represents the reward given after a game
     */
    public void learnFromPolicy(int reward){
        //reverse the policy history to get the last move(final move) fist
        ArrayList<QVector> reversed = new ArrayList<>();
        for(int i = this.policyRecorder.size() -1 ; i >=0; i--){
            reversed.add(this.policyRecorder.get(i));
        }
        double computeMaximum = -1.0;

        for(QVector qVec : reversed){
            //Needs the id of the state!
            double[] qValues = this.q.get(qVec.getState());
            //first iteration
            if(computeMaximum<0){
                qValues[qVec.getMove()] = reward;
            }
            else
            {
                //Qfunction
                qValues[qVec.getMove()] =  qValues[qVec.getMove()] * ( 1 - alpha ) + alpha * gamma * computeMaximum;
            }
            /**
             * iterates to find the max Q
             */
            double max = qValues[0];

            for (int i = 0; i < qValues.length ; i++){
                if(qValues[i]> max){
                    max = qValues[i];
                }
            }
            computeMaximum = max;
        }

    }
}
