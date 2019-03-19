import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LifeParticle {
    private int x;
    private int y;
    private int index;
    private boolean alive;
    private List<LifeParticle> vecins;
    private List<Boolean> steps;

    public LifeParticle(int x, int y, int index, boolean alive) {
        this.x = x;
        this.y = y;
        this.index = index;
        this.vecins = new LinkedList<>();
        this.steps= new ArrayList<>();
        this.alive = alive;
    }

    public LifeParticle clone(boolean alive) {
        return new LifeParticle(x, y, index, alive);
    }

    public void addVecin(LifeParticle vecin) {
        this.vecins.add(vecin);
    }

    public void addVecinAndVicenVersa(LifeParticle vecin) {
        vecin.addVecin(this);
        this.addVecin(vecin);
    }

    public List<LifeParticle> getVecins() {
        return this.vecins;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public List<Boolean> getSteps() {
        return steps;
    }

    public void addStep(boolean step) {
        this.steps.add(step);
    }

    public void setVecins(List<LifeParticle> l) {
        this.vecins = l;
    }

    @Override
    public String toString() {
        return x + "," + y + " " + alive;
    }
}
