public class BrownParticle {
    private double x;

    private double y;
    private double vx;
    private double vy;
    private double radius;
    private double mass;
    private boolean big;
    //-1 left, -2 right, -3 up, -4 down (walls)
    private int index;
    private Collision coll;
    private int nextCollIndex;
    private double timeToNext;

    public BrownParticle (double x, double y, double vx, double vy, boolean big, int index){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.big = big;
        if(big){
            radius = 0.05;
            mass = 100;
        } else {
            radius = 0.005;
            mass = 0.1;
        }
        this.index = index;
    }

    public BrownParticle clone() {
        return new BrownParticle(x, y, vx, vy, big, index);
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public boolean isBig() {
        return big;
    }

    public Collision getColl() {
        return coll;
    }

    public void setColl(Collision coll) {
        this.coll = coll;
    }

    public int getNextCollIndex() {
        return nextCollIndex;
    }

    public double getTimeToNext() {
        return timeToNext;
    }

    public void setNextColl(int nextCollIndex, double timeToNext) {
        if(timeToNext < this.timeToNext) {
            this.timeToNext = timeToNext;
            this.nextCollIndex = nextCollIndex;
        }
    }

    public int getIndex() {
        return index;
    }
}
