import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Simulator {
    private List<List<LifeParticle>> step;
    private LifeGrid2D grid;
//    private ruleset;

    public Simulator(LifeGrid2D grid) {
        this.grid = grid;
        this.step = new ArrayList<>();
    }

    public List<List<LifeParticle>> simulate(int steps) {
        step.add(grid.getParticles());
        while(steps > 0) {
            List <LifeParticle> ps = new LinkedList<>();
//            System.out.println(step.get(step.size() - 1));
            grid.populate(step.get(step.size() - 1));
            grid.calculateVecins();
            for(LifeParticle p : grid.getParticles()) {
//                System.out.println(p.getX() + " " + p.getY() + " " + p.isAlive());
                int aliveVecins = (int) p.getVecins().stream().filter(LifeParticle::isAlive).count();
                if(p.isAlive()) {
                    if (aliveVecins == 2 || aliveVecins == 3) {
                        ps.add(p.clone(true));
                    } else {
                        ps.add(p.clone(false));
                    }
                } else {
                    if(aliveVecins == 3) {
                        ps.add(p.clone(true));
                    } else {
                        ps.add(p.clone(false));
                    }
                }
            }
            step.add(ps);
            System.out.println(steps);
            steps--;
        }
        return step;
    }
}
