package tp6;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class MultitudeParticle {

    public static double R_MIN = 0.12;
    public static double R_MAX = 0.35;

    private int index;
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double radius;
    private double v;

    public Target target;

    private ArrayList<MultitudeParticle> vecins;

    private static NumberFormat formatter = new DecimalFormat("#0.000");

    public MultitudeParticle(int index, double x, double y, double vx, double vy, double radius) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
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

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getIndex() {
        return index;
    }

    public int getCellCol(double L, int M) {
        return Math.max(0, Math.min(M-1, (int) Math.floor(x / (L / (double)M))));
    }

    public int getCellRow(double L, int M) {
        return Math.max(0, Math.min(M-1, (int) Math.floor(y / (L / (double)M))));
    }

    public double getDistance(MultitudeParticle op) {
        return Math.sqrt((Math.pow(x - op.getX(), 2)) + Math.pow(y - op.getY(), 2));
    }

    public double getAngle(MultitudeParticle p) {
        return Math.atan2(y - p.getY(), x - p.getX());
    }

    public void addVecins(ArrayList<MultitudeParticle> vecins, double Rc) {
        for(MultitudeParticle p: vecins){
            if (p != null && getDistance(p) - 2 * radius < Rc) {
                p.addVecin(this);
                this.vecins.add(p);
            }
        }
    }

    public void addVecin(MultitudeParticle p) {
        this.vecins.add(p);
    }

    public void addSelfVecins(ArrayList<MultitudeParticle> vecins, double Rc) {
        for(MultitudeParticle p: vecins){
            if(p != null && p != this && getDistance(p) - 2 * radius < Rc) {
                addVecin(p);
            }
        }
    }

    public ArrayList<MultitudeParticle> getVecins() {
        return vecins;
    }

    public void calculateTarget(){
        //calcula el punto mas cercano de la salida
    }

    public MultitudeParticle clone() {
        MultitudeParticle c = new MultitudeParticle(index, x, y, vx, vy, radius);
        c.setV(v);
        return c;
    }

    @Override
    public String toString() {
        return "MultitudeParticle<" +index +
                "> p=(" + formatter.format(x) + "," + formatter.format(y) +
                "), v=(" + formatter.format(vx) + "," + formatter.format(vy) +
                "), r=" + radius;
    }
    public String toStringDHM() {
        return "MultitudeParticle<" +index +
                "> p= " + formatter.format(x) + ", v= " + formatter.format(vx);
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }


}
