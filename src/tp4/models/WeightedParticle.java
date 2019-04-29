package tp4.models;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class WeightedParticle {

    private int index;
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double ax;
    private double ay;

    private double radius;
    private double mass;

    private ArrayList<WeightedParticle> vecins;

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
        this.vecins = new ArrayList<>();
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

    public int getCellCol(double L, int M) {
        return (int) Math.floor(x / (L / (double)M));
    }

    public int getCellRow(double L, int M) {
        return (int) Math.floor(y / (L / (double)M));
    }

    public double getDistance(WeightedParticle op) {
        return Math.sqrt((Math.pow(x - op.getX(), 2)) + Math.pow(y - op.getY(), 2));
    }

    public void addVecins(ArrayList<WeightedParticle> vecins, double Rc) {
        for(WeightedParticle p: vecins){
            if (getDistance(p) - 2 * radius < Rc) {
                p.addVecin(this);
                this.vecins.add(p);
            }
        }
    }

    public void addVecin(WeightedParticle p) {
        this.vecins.add(p);
    }

    public void addSelfVecins(ArrayList<WeightedParticle> vecins, double Rc) {
        for(WeightedParticle p: vecins){
            if(p != this && getDistance(p) - 2 * radius < Rc) {
                addVecin(p);
            }
        }
    }

    public ArrayList<WeightedParticle> getVecins() {
        return vecins;
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
