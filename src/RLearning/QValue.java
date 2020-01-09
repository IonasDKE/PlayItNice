package RLearning;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class QValue {
    private Map<Integer, Vector> rows = new HashMap();
    private int rowCount;
    private int colCount;
    QValue(){

    }

    /**
     * @param row the
     * @param col
     * @param q
     */
    public void insert(int row, int col, double q){
        //creates the row
        Vector r = this.computeRow(row);
        //inserts the q at each col of the row
        //Vector is a java library
        r.set(col, q);

        if(col>=this.colCount)
            this.colCount = col+1;
        if(row>=this.rowCount)
            this.rowCount = row+1;
    }

    public Vector computeRow(int row){
        Vector r = getRows().get(row);
        if(r ==null){
            r = new Vector(this.colCount);
            //r.setId(row);
            this.rows.put(row, r);
        }
        return r;
    }

    public Map<Integer, Vector> getRows(){
        return this.rows;
    }

}
