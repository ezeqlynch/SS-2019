package tp5;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class GranularParticle {


    private static final double KN = 100000;
    private static final double GAMMA = 70;
    private int index;
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double ax;
    private double ay;
    private double prevVx;
    private double prevVy;
    private double radius;
    private double mass;
    private double v;
    private boolean wentDown;
    private double pressure;

    private ArrayList<GranularParticle> vecins;

    private static NumberFormat formatter = new DecimalFormat("#0.000");

    public GranularParticle(int index, double x, double y, double vx, double vy, double ax, double ay, double radius, double mass) {
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
        return Math.max(0, Math.min(M-1, (int) Math.floor(x / (L / (double)M))));
    }

    public int getCellRow(double L, int M) {
        return Math.max(0, Math.min(M-1, (int) Math.floor(y / (L / (double)M))));
    }

    public double getDistance(GranularParticle op) {
        return Math.sqrt((Math.pow(x - op.getX(), 2)) + Math.pow(y - op.getY(), 2));
    }

    public void addVecins(ArrayList<GranularParticle> vecins, double Rc) {
        for(GranularParticle p: vecins){
            if (p != null && getDistance(p) - (radius + p.getRadius()) < Rc) {
                p.addVecin(this);
                this.vecins.add(p);
            }
        }
    }

    public void addVecin(GranularParticle p) {
        this.vecins.add(p);
    }

    public void addSelfVecins(ArrayList<GranularParticle> vecins, double Rc) {
        for(GranularParticle p: vecins){
            if(p != null && p != this && getDistance(p) - (radius + p.getRadius()) < Rc) {

                addVecin(p);
            }
        }
    }

    public ArrayList<GranularParticle> getVecins() {
        return vecins;
    }



    public double calculateNormalForce(GranularParticle p, boolean xory) {
        double distance = getDistance(p);
        double eta = (radius + p.getRadius() - distance);
        double enx = (p.getX() - x) / getDistance(p);
        double eny = (p.getY() - y) / getDistance(p);
        double vxDiff = vx - p.getVx();
        double vyDiff = vy - p.getVy();
        double etaDot = vxDiff * enx + vyDiff * eny;
        double force = xory ? (- KN * eta - GAMMA * etaDot) * enx : (- KN * eta - GAMMA * etaDot) * eny;
        this.pressure += Math.abs(force);
        return force;
        // en = (enx, eny)
        // et = (-eny, enx)
        //http://wnbell.com/media/2005-07-SCA-Granular/BeYiMu2005.pdf
        // N = x2 - x1 / ||x2 - x1|| = en versor
        // V = v1 - v2 (vectores)
        // Erara(puntito) = V . enx
    }

    public void calculateTotalForce() {
        this.pressure = 0;
        ax = vecins.parallelStream().mapToDouble(p -> calculateNormalForce(p, true)).sum() / mass;
        ay = vecins.parallelStream().mapToDouble(p -> calculateNormalForce(p, false)).sum() / mass - 9.8;
        //paredes
        double fn = 0;
        if(x < radius) { //pared izq
             double eta = radius - x;
             double enx = -1; //eny = 0
             double etaDot = vx * enx;
             fn = (-KN * eta - GAMMA * etaDot) * enx;
             ax += fn / mass;
             this.pressure += Math.abs(fn);
        } else if(x > 0.3 - radius) { //pared der
            double eta = radius - (0.3 - x);
            double enx = 1; //eny = 0
            double etaDot = vx * enx;
            fn = (-KN * eta - GAMMA * etaDot) * enx;
            ax += fn / mass;
            this.pressure += Math.abs(fn);
        }
        if(!wentDown && y < radius && (x < 0.13 || x > 0.17)) { // pared abajo
            double eta = radius - y;
            double eny = -1; //eny = 0
            double etaDot = vy * eny;
            fn = (-KN * eta - GAMMA * etaDot) * eny;
            ay += fn / mass;
            this.pressure += Math.abs(fn);
        }
        this.pressure = this.pressure/(this.radius*2*Math.PI);
    }


    public GranularParticle clone() {
        GranularParticle c = new GranularParticle(index, x, y, vx, vy, ax, ay, radius, mass);
        c.setPrevVx(prevVx);
        c.setPrevVy(prevVy);
        c.setWentDown(wentDown);
        c.setPressure(pressure);
        return c;
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

    public double getPrevVx() {
        return prevVx;
    }

    public void setPrevVx(double prevVx) {
        this.prevVx = prevVx;
    }

    public double getPrevVy() {
        return prevVy;
    }

    public void setPrevVy(double prevVy) {
        this.prevVy = prevVy;
    }

    public boolean isWentDown() {
        return wentDown;
    }

    public void setWentDown(boolean wentDown) {
        this.wentDown = wentDown;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
    public double getPressure() {
        return pressure;
    }

}
