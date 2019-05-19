package tp4.models;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class WeightedParticle {

    private int index;
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double ax;
    private double ay;
    private double prevVx;
    private double prevVy;
    private double e;
    private double rm;
    private double radius;
    private double mass;
    private double v;

    private ArrayList<WeightedParticle> vecins;

    private static NumberFormat formatter = new DecimalFormat("#0.000");

    public WeightedParticle(int index, double x, double y, double vx, double vy, double ax, double ay, double radius, double mass, double e, double rm) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.ax = ax;
        this.ay = ay;
        this.radius = radius;
        this.mass = mass;
        this.e = e;
        this.rm = rm;
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

    public double getDistance(WeightedParticle op) {
        return Math.sqrt((Math.pow(x - op.getX(), 2)) + Math.pow(y - op.getY(), 2));
    }

    public void addVecins(ArrayList<WeightedParticle> vecins, double Rc) {
        for(WeightedParticle p: vecins){
            if (p != null && getDistance(p) - 2 * radius < Rc) {
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
            if(p != null && p != this && getDistance(p) - 2 * radius < Rc) {

                addVecin(p);
            }
        }
    }

    public ArrayList<WeightedParticle> getVecins() {
        return vecins;
    }

    public void calculatePotentialEnergyTotal() {
        v = vecins.parallelStream().mapToDouble(this::calculatePotentialEnergy).sum();
    }

    public double calculatePotentialEnergy(WeightedParticle p) {
        double dist = getDistance(p);
        return e * (Math.pow(rm / dist, 12) - 2 * Math.pow(rm / dist, 6));
    }

    public double calculateForce(WeightedParticle p) {
        double dist = getDistance(p);
        return 12.0 * e/rm * (Math.pow(rm / dist, 13) - Math.pow(rm / dist, 7));
    }

    public void calculateTotalForce() {
        ax = vecins.parallelStream().mapToDouble(p -> calculateForce(p) * Math.cos(getAngle(p))).sum() / mass;
        ay = vecins.parallelStream().mapToDouble(p -> calculateForce(p) * Math.sin(getAngle(p))).sum() / mass;
        if(x < 5) {
            ax += (12.0 * e/rm * (Math.pow(rm / x, 13) - Math.pow(rm / x, 7))) / mass;
        } else if (x > 200 && x < 205){
            if(!(y > 95 && y < 105)){
                ax += (12.0 * e/rm * (Math.pow(rm / (x - 200.0), 13) - Math.pow(rm / (x - 200.0), 7))) / mass;
            } else {
                WeightedParticle auxPtop = new WeightedParticle(-1, 200, 95, 0, 0, 0, 0, 0, 0, 2, 1);
                WeightedParticle auxPbot = new WeightedParticle(-1, 200, 105, 0, 0, 0, 0, 0, 0, 2, 1);
                if(getDistance(auxPtop) < 5.0) {
                    ax += calculateForce(auxPtop) * Math.cos(getAngle(auxPtop)) / mass;
                    ay += calculateForce(auxPtop) * Math.sin(getAngle(auxPtop)) / mass;
                }
                if(getDistance(auxPbot) < 5.0) {
                    ax += calculateForce(auxPbot) * Math.cos(getAngle(auxPbot)) / mass;
                    ay += calculateForce(auxPbot) * Math.sin(getAngle(auxPbot)) / mass;
                }
            }
        } else if (x > 195 && x < 200) {
            if(!(y > 95 && y < 105)){
                ax -= (12.0 * e/rm * (Math.pow(rm / (200.0 - x), 13) - Math.pow(rm / (200.0 - x), 7))) / mass;
            } else {
                WeightedParticle auxPtop = new WeightedParticle(-1, 200, 95, 0, 0, 0, 0, 0, 0, 2, 1);
                WeightedParticle auxPbot = new WeightedParticle(-1, 200, 105, 0, 0, 0, 0, 0, 0, 2, 1);
                if(getDistance(auxPtop) < 5.0) {
                    ax += calculateForce(auxPtop) * Math.cos(getAngle(auxPtop)) / mass;
                    ay += calculateForce(auxPtop) * Math.sin(getAngle(auxPtop)) / mass;
                }
                if(getDistance(auxPbot) < 5.0) {
                    ax += calculateForce(auxPbot) * Math.cos(getAngle(auxPbot)) / mass;
                    ay += calculateForce(auxPbot) * Math.sin(getAngle(auxPbot)) / mass;
                }
            }
        } else if (x > 395) {
            ax -= (12.0 * e/rm * (Math.pow(rm / (400.0 - x), 13) - Math.pow(rm / (400.0 - x), 7))) / mass;
        }
        if(y < 5) {
            ay += (12.0 * e/rm * (Math.pow(rm / y, 13) - Math.pow(rm / y, 7))) / mass;
        } else if (y > 195) {
            ay -= (12.0 * e/rm * (Math.pow(rm / (200.0 - y), 13) - Math.pow(rm / (200.0 - y), 7))) / mass;
        }
    }

    public double getAngle(WeightedParticle p) {
        return Math.atan2(y - p.getY(), x - p.getX());
    }

    public WeightedParticle clone() {
        WeightedParticle c = new WeightedParticle(index, x, y, vx, vy, ax, ay, radius, mass, e, rm);
        c.setPrevVx(prevVx);
        c.setPrevVy(prevVy);
        c.setV(v);
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

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }
}
