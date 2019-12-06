package AI;

import View.Line;

public class WeightedEdge implements Comparable<WeightedEdge>{

    private int weight ;
    private Line line;

    /**
     *  This parameter takes in a line and a weight which is equals
     *  to the evaluation function
     *
     * @param line takes in a  line
     * @param weight and a weight
     */
    public WeightedEdge(Line line, int weight) {
        this.line = line ;
        this.weight = weight ;
    }

    /**
     * @return the line associated to a Weighted Edge
     */
    public Line getLine(){
        return this.line;
    }

    /**
     * @return the score of a weighted edhe
     */
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public void setLine(Line line){
        this.line = line;
    }

    /**
     * @param edge another weighted edge
     * @return the difference between the weights of two edges
     */
    @Override
    public int compareTo(WeightedEdge edge) {
        return this.weight - edge.weight;
    }
}

