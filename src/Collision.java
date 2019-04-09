public class Collision {
    private int index1;
    private int index2;
    private double timeTo;

    public Collision(int i1, int i2, double timeTo) {
        index1 = i1;
        index2 = i2;
        this.timeTo = timeTo;
    }

    public double getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(double timeTo) {
        if(timeTo < this.timeTo){
            this.timeTo = timeTo;
        }
    }
}
