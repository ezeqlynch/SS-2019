package tp4.models;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class WeightedParticle {

    //-1 left, -2 right, -3 up, -4 down (walls)
    private int index;
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double ax;
    private double ay;

    private double radius;
    private double mass;

    private static NumberFormat formatter = new DecimalFormat("#0.000");

    public WeightedParticle(int index, double x, double y, double vx, double vy, double ax, double ay, double radius, double mass) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.ax = ax;
        this.ay = ay;
        this.radius = radius;
        this.mass = mass;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
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

    public double getAx() {
        return ax;
    }

    public void setAx(double ax) {
        this.ax = ax;
    }

    public double getAy() {
        return ay;
    }

    public void setAy(double ay) {
        this.ay = ay;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "WeightedParticle<" +index +
                "> p=(" + formatter.format(x) + "," + formatter.format(y) +
                "), v=(" + formatter.format(vx) + "," + formatter.format(vy) +
                "), a=(" + formatter.format(ax) + "," + formatter.format(ay) +
                "), r=" + radius + ", m=" + mass;
    }
    public String toStringDHM() {
        return "WeightedParticle<" +index +
                "> p= " + formatter.format(x) + ", v= " + formatter.format(vx) +", a= " + formatter.format(ax);
    }
}
