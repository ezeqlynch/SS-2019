import java.util.LinkedList;
import java.util.List;

public interface LifeGrid {
    void populate(List<LifeParticle> ps);
    void calculateVecins();
    List<LifeParticle> getParticles();
    int getHeight();
    int getWidth();
    int getDepth();
}
