package tp6;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class MultitudeParticle {

    public static double R_MIN = 0.15;
    public static double R_MAX = 0.32;
    public static double V_MAX = 1.55;

    private int index;
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double radius;
    private double v;
    public boolean out;
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
        this.out = false;
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
            if (p != null && getDistance(p) - 2 * radius < Rc && x < 20) {
                p.addVecin(this);
                this.vecins.add(p);
            }
        }
    }

    public void addVecin(MultitudeParticle p) {
        this.vecins.add(p);
        double ve = V_MAX;
        double ex = (x - p.getX())/this.getDistance(p); //a - b va de b a a y necesito q vaya de p a mi => de p a mi mi - p
        double ey = (y - p.getY())/this.getDistance(p);
        radius = R_MIN;
        vx = ve * ex;
        vy = ve * ey;
        p.setRadius(R_MIN);
        p.setVx(ve * ex * -1);
        p.setVy(ve * ey * -1);
    }

    public boolean isOverlapping(double x, double y){
        return Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2) <= Math.pow(radius, 2);
    }

    public void checkWall(){ //hacer aujero
        double ve = V_MAX;
        if(x > 20 - radius && (y < 9.4 || y > 10.6) && x < 20) { //20 harcodeado = pos de la pared // chequear las puntas
            radius = R_MIN;
            vx = ve * -1;
            vy = 0;
        } else if(x > 20 - radius && y > 9.4 && x < 20 && isOverlapping(20, 9.4)) {
            double dist = Math.sqrt(Math.pow(x - 20, 2) + Math.pow(y - 9.4,2));
            double ex = (x - 20)/dist; //a - b va de b a a y necesito q vaya de p a mi => de p a mi mi - p
            double ey = (y - 9.4)/dist;
            radius = R_MIN;
            vx = ve * ex;
            vy = ve * ey;
        } else if(x > 20 - radius && y < 10.6 && x < 20 && isOverlapping(20, 10.6)) {
            double dist = Math.sqrt(Math.pow(x - 20, 2) + Math.pow(y - 9.4,2));
            double ex = (x - 20)/dist; //a - b va de b a a y necesito q vaya de p a mi => de p a mi mi - p
            double ey = (y - 10.6)/dist;
            radius = R_MIN;
            vx = ve * ex;
            vy = ve * ey;
        }
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
        if((x > 19 && y > 9.6 && y < 10.4) || x > 20){
            target = new Target(30, y);
        } else {
            target = new Target(19.5, 10);
        }
        //calcula el punto mas cercano de la salida
    }

    public double distToTarget(){
        return Math.sqrt(Math.pow(target.x - x, 2) + Math.pow(target.y - y, 2));
    }

    public MultitudeParticle clone() {
        MultitudeParticle c = new MultitudeParticle(index, x, y, vx, vy, radius);
        c.setV(v);
        c.setOut(out);
        return c;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    public boolean isOut() {
        return out;
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
        return Math.sqrt(vx * vx + vy * vy);
    }

    public void setV(double v) {
        this.v = v;
    }


}
