import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LifeParticle {
    private int x;
    private int y;
    private int index;
    private boolean alive;
    //    private List<LifeParticle> vecins;
    private int vecins;

    public LifeParticle(int x, int y, int index, boolean alive) {
        this.x = x;
        this.y = y;
        this.index = index;
        this.vecins = 0;
        this.alive = alive;
    }

    public LifeParticle clone(boolean alive) {
        return new LifeParticle(x, y, index, alive);
    }

    public void addVecin() {
        this.vecins++;
    }

    public void addVecinAndVicenVersa(LifeParticle vecin) {
        if(this.isAlive()) {
            vecin.addVecin();
        }
        if(vecin.isAlive()){
            this.addVecin();
        }
    }

    public int getVecins() {
        return this.vecins;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setVecins(int l) {
        this.vecins = l;
    }

    @Override
    public String toString() {
        return x + "," + y + " " + alive;
    }
}
