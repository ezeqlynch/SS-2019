public class Collision {
    private int index1;
    private int index2;
    private double timeOf;

    public Collision(int i1, int i2, double timeOf) {
        index1 = i1;
        index2 = i2;
        this.timeOf = timeOf;
    }

    public double getTimeOf() {
        return timeOf;
    }

    public void setTimeOf(double timeOf) {
        if(timeOf < this.timeOf){
            this.timeOf = timeOf;
        }
    }

    public int getIndex1() {
        return index1;
    }

    public int getIndex2() {
        return index2;
    }
}
