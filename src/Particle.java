import java.util.*;

public class Particle {
    private double radius;
    private Point position;
    private List<Particle> vecins;

    public Particle(double radius, double x, double y) {
        this.radius = radius;
        this.position = new Point(x, y);
        this.vecins = new LinkedList<>();
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return position.toString();
    }

    public List<Particle> getVecins() {
        return vecins;
    }

    public void addVecins(ArrayList<Particle> vecins) {
        for(Particle p: vecins){
            p.addVecin(this);
        }
        this.vecins.addAll(vecins);
    }

    public void addSelfVecins(ArrayList<Particle> vecins) {
        for(Particle p: vecins){
            if(p != this) {
                addVecin(p);
            }
        }
    }

    public void addVecin(Particle vecin) {
        this.vecins.add(vecin);
    }
}
