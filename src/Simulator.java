import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Simulator {
    private List<List<LifeParticle>> step;
//    private LifeGrid2D grid;
    private LifeGrid grid;
    private int r1;
    private int r2;
    private int r3;
    private int r4;

//    private ruleset;

//    public Simulator(LifeGrid2D grid, String rules) {
////        this.grid = grid;
//        this.step = new ArrayList<>();
//        String[] rs = rules.split(",");
//        r1 = Integer.parseInt(rs[0]);
//        r2 = Integer.parseInt(rs[1]);
//        r3 = Integer.parseInt(rs[2]);
//        r4 = Integer.parseInt(rs[3]);
//    }

    public Simulator(LifeGrid grid, String rules) {
        this.grid = grid;
        this.step = new ArrayList<>();
        String[] rs = rules.split(",");
        r1 = Integer.parseInt(rs[0]);
        r2 = Integer.parseInt(rs[1]);
        r3 = Integer.parseInt(rs[2]);
        r4 = Integer.parseInt(rs[3]);
    }

    public List<List<LifeParticle>> simulate(int steps) {
        step.add(grid.getParticles());
        while (steps > 0) {
            List<LifeParticle> ps = new LinkedList<>();
//            System.out.println(step.get(step.size() - 1));
            grid.populate(step.get(step.size() - 1));
            grid.calculateVecins();
            for (LifeParticle p : grid.getParticles()) {
//                System.out.println(p.getX() + " " + p.getY() + " " + p.isAlive());
                int aliveVecins = p.getVecins();
                if (p.isAlive()) {
                    if (aliveVecins >= r1 && aliveVecins <= r2) {
                        ps.add(p.clone(true));
//                    } else {
//                        ps.add(p.clone(false));
                    }
                } else {
                    if(aliveVecins >= r3 && aliveVecins <= r4) {
                        ps.add(p.clone(true));
//                    } else {
//                        ps.add(p.clone(false));
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
