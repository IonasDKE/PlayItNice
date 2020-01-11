package RLearning;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class QValue {
    private Map<Integer, QVector> rows = new HashMap();
    private int rowCount;
    private int colCount;
    private double def;

    QValue(int statecount, int movecount){
        this.rowCount = statecount;
        this.colCount = movecount;
        this.def = 0.0D;
    }

    /**
     * @param row the
     * @param col
     * @param q
     */
    public void insert(int row, int col, double q){
        //creates the row
        QVector r = this.computeRow(row);
        //inserts the q at each col of the row
        //Vector is a java library
        r.set(col, q);

        if(col>=this.colCount)
            this.colCount = col+1;
        if(row>=this.rowCount)
            this.rowCount = row+1;
    }

    public QVector computeRow(int row){
        QVector r = getRows().get(row);
        if(r ==null){
            r = new QVector(this.colCount);
            //r.setId(row);
            this.rows.put(row, r);
        }
        return r;
    }

    public Map<Integer, QVector> getRows(){
        return this.rows;
    }

    /**
     * @param row the state which is current
     * @param col the move that is being made
     * @return the value at a given state given a move
     */
    public int get(int row, int col) {
        QVector r = this.computeRow(row);
        return (int) r.get(col);
    }

    public void setInitially(double qinit) {
        this.def = qinit;
        Iterator var3 = this.rows.values().iterator();

        while(var3.hasNext()) {
            QVector row = (QVector) var3.next();
            row.setAll(qinit);
        }
    }
}
