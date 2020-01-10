package RLearning;

import java.io.Serializable;
import java.util.*;

public class QVector extends Vector implements Serializable {

    private int dim;
    private Map<Integer, Double> qValues = new HashMap();
    private double DEFAULT_VALUE;

    public QVector(int d){
        this.dim = d;
        DEFAULT_VALUE = 0.0;
    }

    /** @see{https://stackoverflow.com/questions/7519339/hashmap-to-return-default-value-for-non-found-keys}
     *  @param col the column which is going to be investigated
     *  @return
     */
    public Object get(int col){
        return (Double)this.qValues.getOrDefault(col, this.DEFAULT_VALUE);
    }


    public void setAll(double qinit) {
        this.DEFAULT_VALUE = qinit;
        Iterator it = this.qValues.keySet().iterator();

        while(it.hasNext()) {
            Integer index = (Integer)it.next();
            this.qValues.put(index, this.DEFAULT_VALUE);
        }

    }

    public IndexValue indexWithMaxValue() {
        IndexValue iv = new IndexValue();
        iv.setIndex(-1);
        iv.setValue(-1.0D / 0.0);
        Iterator var2 = this.qValues.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<Integer, Double> entry = (Map.Entry)var2.next();
            if ((Integer)entry.getKey() < this.dim) {
                double value = (Double)entry.getValue();
                if (value > iv.getValue()) {
                    iv.setValue(value);
                    iv.setIndex((Integer)entry.getKey());
                }
            }
        }

        if (!iv.isValid()) {
            iv.setValue(this.DEFAULT_VALUE);
        } else if (iv.getValue() < this.DEFAULT_VALUE) {
            for(int i = 0; i < this.dim; ++i) {
                if (!this.qValues.containsKey(i)) {
                    iv.setValue(this.DEFAULT_VALUE);
                    iv.setIndex(i);
                    break;
                }
            }
        }

        return iv;
    }

    public IndexValue indexWithMaxValue(List<Integer> indices) {
        if (indices == null) {
            return this.indexWithMaxValue();
        } else {
            IndexValue iv = new IndexValue();
            iv.setIndex(-1);
            iv.setValue(-1.0D / 0.0);
            Iterator var3 = indices.iterator();

            while(var3.hasNext()) {
                Integer index = (Integer)var3.next();
                double value = (Double)this.qValues.getOrDefault(index, -1.0D / 0.0);
                if (value > iv.getValue()) {
                    iv.setIndex(index);
                    iv.setValue(value);
                }
            }

            return iv;
        }
    }
}
