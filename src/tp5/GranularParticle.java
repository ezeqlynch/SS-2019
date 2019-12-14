package tp5;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class GranularParticle {


    private static final double KN = 1e5;
    private int index;
    private double x;
    private double y;
    private double z;
    private double vx;
    private double vy;
    private double vz;
    private double ax;
    private double ay;
    private double az;
    private double prevVx;
    private double prevVy;
    private double prevVz;
    private double radius;
    private double mass;
    private double v;
    private boolean wentDown;
    private double pressure;
    private double kineticEnergy;

    private ArrayList<GranularParticle> vecins;

    private static NumberFormat formatter = new DecimalFormat("#0.000");

    public GranularParticle(int index, double x, double y, double z, double vx, double vy, double vz, double ax, double ay, double az, double radius, double mass) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.ax = ax;
        this.ay = ay;
        this.az = az;
        this.radius = radius;
        this.mass = mass;
        this.kineticEnergy = 0.5 * mass * (vx*vx+vy*vy+vz*vz);
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

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
        this.kineticEnergy = 0.5 * mass * (vx*vx+vy*vy+vz*vz);
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
        this.kineticEnergy = 0.5 * mass * (vx*vx+vy*vy+vz*vz);
    }

    public double getVz() {
        return vz;
    }

    public void setVz(double vz) {
        this.vz = vz;
        this.kineticEnergy = 0.5 * mass * (vx*vx+vy*vy+vz*vz);
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

    public double getAz() {
        return az;
    }

    public void setAz(double az) {
        this.az = az;
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
        this.kineticEnergy = 0.5 * mass * (vx*vx+vy*vy+vz*vz);
    }

    public int getIndex() {
        return index;
    }


    //repensar vecinos con cubitos
    public int getCellCol(double L, int M) {
        return Math.max(0, Math.min(M-1, (int) Math.floor(x / (L / (double)M))));
    }

    public int getCellRow(double L, int M) {
        return Math.max(0, Math.min(M-1, (int) Math.floor(y / (L / (double)M))));
    }


    // not sure
    public int getCellHeight(double H, int M) {
        return Math.max(0, Math.min(M-1, (int) Math.floor(z / (H / (double)M))));
    }


    public double getDistance(GranularParticle op) {
        return Math.sqrt((Math.pow(x - op.getX(), 2)) + Math.pow(y - op.getY(), 2) + Math.pow(z - op.getZ(), 2));
    }


    //deberia quedar igual?
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



    public double calculateNormalForce(GranularParticle p, int axis) {
        double distance = getDistance(p);
        double eta = (radius + p.getRadius() - distance);
        double enx = (p.getX() - x) / distance;
        double eny = (p.getY() - y) / distance;
        double enz = (p.getZ() - z) / distance;
        double vxDiff = vx - p.getVx();
        double vyDiff = vy - p.getVy();
        double vzDiff = vz - p.getVz();
        double etaDot = vxDiff * enx + vyDiff * eny + vzDiff * enz;
        double force = 0;
        switch (axis){
            case 0: //x
                force = (- KN * eta - GranularMain.GAMMA * etaDot) * enx;
                break;
            case 1: //y
                force = (- KN * eta - GranularMain.GAMMA * etaDot) * eny;
                break;
            case 2: //z
                force = (- KN * eta - GranularMain.GAMMA * etaDot) * enz;
                break;
        }
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
        ax = vecins.parallelStream().mapToDouble(p -> calculateNormalForce(p, 0)).sum() / mass;
        ay = vecins.parallelStream().mapToDouble(p -> calculateNormalForce(p, 1)).sum() / mass;
        az = vecins.parallelStream().mapToDouble(p -> calculateNormalForce(p, 2)).sum() / mass - 9.8;
        //paredes TODO: REHACER
        double fn = 0;
        if(x < radius) { //paredes x
             double eta = radius - x;
             double enx = -1; //eny = 0
             double etaDot = vx * enx;
             fn = (-KN * eta - GranularMain.GAMMA * etaDot) * enx;
             ax += fn / mass;
             this.pressure += Math.abs(fn);
        } else if(x > GranularMain.W - radius) { //paredes x
            double eta = radius - (GranularMain.W - x);
            double enx = 1; //eny = 0
            double etaDot = vx * enx;
            fn = (-KN * eta - GranularMain.GAMMA * etaDot) * enx;
            ax += fn / mass;
            this.pressure += Math.abs(fn);
        }
        if(y < radius) { //paredes y
            double eta = radius - y;
            double eny = -1; //enx = 0
            double etaDot = vy * eny;
            fn = (-KN * eta - GranularMain.GAMMA * etaDot) * eny;
            ay += fn / mass;
            this.pressure += Math.abs(fn);
        } else if(y > GranularMain.L - radius) { //paredes y
            double eta = radius - (GranularMain.L - y);
            double eny = 1; //enx = 0
            double etaDot = vy * eny;
            fn = (-KN * eta - GranularMain.GAMMA * etaDot) * eny;
            ay += fn / mass;
            this.pressure += Math.abs(fn);
        }
        //TODO: hacer ranura
        if(!wentDown && z < radius /*&& (x < GranularMain.W/2 - GranularMain.D/2 || x > GranularMain.W/2 + GranularMain.D/2)*/) { // pared abajo
            double eta = radius - z;
            double enz = -1; //eny = 0
            double etaDot = vz * enz;
            fn = (-KN * eta - GranularMain.GAMMA * etaDot) * enz;
            az += fn / mass;
            this.pressure += Math.abs(fn);
        } /*else if(!wentDown && y < radius){ //bordes
            GranularParticle leftEdge = new GranularParticle(-1, GranularMain.W/2 - GranularMain.D/2,0,0,0,0,0,0,0,0,0,0);
            GranularParticle rightEdge = new GranularParticle(-1, GranularMain.W/2 + GranularMain.D/2,0,0,0,0,0,0,0,0,0,0);
            if(radius - getDistance(leftEdge) > 0){
                double distance = getDistance(leftEdge);
                double eta = radius - distance;
                double enx = (leftEdge.getX() - x) / distance;
                double eny = (leftEdge.getY() - y) / distance;
                double etaDot = vx * enx + vy * eny;
                double forcex = (- KN * eta - GranularMain.GAMMA * etaDot) * enx;
                double forcey = (- KN * eta - GranularMain.GAMMA * etaDot) * eny;
                this.pressure += Math.abs(forcex) + Math.abs(forcey);
                ax += forcex / mass;
                ay += forcey / mass;
            }
            if(radius - getDistance(rightEdge) > 0){
                double distance = getDistance(rightEdge);
                double eta = radius - distance;
                double enx = (rightEdge.getX() - x) / distance;
                double eny = (rightEdge.getY() - y) / distance;
                double etaDot = vx * enx + vy * eny;
                double forcex = (- KN * eta - GranularMain.GAMMA * etaDot) * enx;
                double forcey = (- KN * eta - GranularMain.GAMMA * etaDot) * eny;
                this.pressure += Math.abs(forcex) + Math.abs(forcey);
                ax += forcex / mass;
                ay += forcey / mass;
            }
        }*/
        this.pressure = this.pressure/(this.radius*2*Math.PI);
    }


    public GranularParticle clone() {
        GranularParticle c = new GranularParticle(index, x, y, z, vx, vy, vz, ax, ay, az, radius, mass);
        c.setPrevVx(prevVx);
        c.setPrevVy(prevVy);
        c.setPrevVz(prevVz);
        c.setWentDown(wentDown);
        c.setPressure(pressure);
        return c;
    }

    @Override
    public String toString() {
        return "WeightedParticle<" +index +
                "> p=(" + formatter.format(x) + "," + formatter.format(y) + "," + formatter.format(z) +
                "), v=(" + formatter.format(vx) + "," + formatter.format(vy) + "," + formatter.format(vz) +
                "), a=(" + formatter.format(ax) + "," + formatter.format(ay) + "," + formatter.format(az) +
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

    public double getPrevVz() {
        return prevVz;
    }

    public void setPrevVz(double prevVz) {
        this.prevVz = prevVz;
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
    public double getKineticEnergy() {
        return kineticEnergy;
    }

}
