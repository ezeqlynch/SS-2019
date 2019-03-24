import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LifeParticle {
    private int x;
    private int y;
    private int z;
    private int index;
    private boolean alive;
    //    private List<LifeParticle> vecins;
    private int vecins;

    public LifeParticle(int x, int y, int index, boolean alive) {
        this.x = x;
        this.y = y;
        this.z = 0;
        this.index = index;
        this.vecins = 0;
        this.alive = alive;
    }

    public LifeParticle(int x, int y, int z, int index, boolean alive) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.index = index;
        this.vecins = 0;
        this.alive = alive;
    }

    public LifeParticle clone(boolean alive) {
        return new LifeParticle(x, y, z, index, alive);
    }

    public void addVecin() {
        this.vecins++;
    }

    public void addVecinAndVicenVersa(LifeParticle vecin) {
        if(this.isAlive()) { // vecin nunca va a ser null si estoy vivo por como esta implementado
            vecin.addVecin();
        }
        if(vecin != null && vecin.isAlive()){
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
        return /*x + "," + y + " " + */alive ? "Tr  ": "F   ";
    }

    public int getZ() {
        return z;
    }
}
