package RLearning;

public class IndexValue {

    private int index;
    private double value;

    public IndexValue(){
    }
    public IndexValue(int index, double value) {
        this.index = index;
        this.value = value;
    }

    public boolean isValid() {
        return this.index != -1;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value){
        this.value = value;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
