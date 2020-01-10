package RLearning;

import java.util.List;

public class setQ {


    private double alpha;
    private double gamma;
    private int numberOfState;
    private int numberOfMoves;
    private double qinit;

    public setQ(int numberOfState, int numberOfMoves, double qinit){
        this.gamma = 0.5D;
        this.numberOfState =  numberOfState;
        this.numberOfMoves = numberOfMoves;
        this.qinit = qinit;
        this.qMat = new QValue(numberOfState, numberOfMoves);
        this.qMat.setInitially(qinit);
        this.alphaMat = new QValue(numberOfState, numberOfMoves);
    }

    public void setA(double alpha) {
        this.alphaMat.setInitially(alpha);
    }

    /**
     * @param stateId the id of the current state
     * @param moveId the id of the line which is going to be colored
     * @return
     */
    int getQValue(int stateId, int moveId){
        return this.qMat.get(stateId, moveId);
    }


    public double computeAlpha(int row, int col) {
        return this.alphaMat.get(row, col);
    }
    public double computeGamma() {
        return this.gamma;
    }

    private QValue qMat;
    private QValue alphaMat;

    public IndexValue actionWithMaxQAtState(int nextStateID, List<Integer> actionAfterColoring) {
        QVector rowVector = this.qMat.computeRow(nextStateID);
        return rowVector.indexWithMaxValue(actionAfterColoring);
    }

    public void setG(double gamma) {
        this.gamma = gamma;
    }
}
