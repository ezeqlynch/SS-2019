import java.util.*;

public class Particle {
    private double radius;
    private Point position;
    private List<Particle> vecins;
    private int index;

    public Particle(double radius, double x, double y, int index) {
        this.radius = radius;
        this.position = new Point(x, y);
        this.vecins = new LinkedList<>();
        this.index = index;
    }

    public Point getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return position.toString();
    }

    public List<Particle> getVecins() {
        return vecins;
    }

    public void addVecins(ArrayList<Particle> vecins, double Rc, int side, double length) {
        for(Particle p: vecins){
            if(side == 0) {
                if(getDistance(p) < Rc) {
                    p.addVecin(this);
                    this.vecins.add(p);
                }
            } else {
                if(position.getDistance(p.getPosition(), side, length) < Rc) {
                    p.addVecin(this);
                    this.vecins.add(p);
                }
            }

        }
    }


    public void addSelfVecins(ArrayList<Particle> vecins, double Rc) {
        for(Particle p: vecins){
            if(p != this && getDistance(p) < Rc) {
                addVecin(p);
            }
        }
    }

    public void checkDistnces() {
        for(Particle p: vecins) {
            System.out.println(getDistance(p));
        }
    }

    public double getDistance(Particle op) {
        return position.getDistance(op.getPosition());
    }
    public void addVecin(Particle vecin) {
        this.vecins.add(vecin);
    }

    public int getIndex() {
        return index;
    }
}
