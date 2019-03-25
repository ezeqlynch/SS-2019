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
    private int r12;
    private int r22;
    private int r32;
    private int r42;
    private int centerX;
    private int centerY;
    private int centerZ;



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

    public Simulator(LifeGrid grid, String rules, String rules2) {
        this.grid = grid;
        this.step = new ArrayList<>();
        String[] rs = rules.split(",");
        String[] rs2 = rules2.split(",");
        r1 = Integer.parseInt(rs[0]);
        r2 = Integer.parseInt(rs[1]);
        r3 = Integer.parseInt(rs[2]);
        r4 = Integer.parseInt(rs[3]);
        r12 = Integer.parseInt(rs2[0]);
        r22 = Integer.parseInt(rs2[1]);
        r32 = Integer.parseInt(rs2[2]);
        r42 = Integer.parseInt(rs2[3]);
        centerX = grid.getWidth() / 2;
        centerY = grid.getHeight() / 2;
        centerZ = grid.getDepth() / 2;

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
            if(steps % 20 == 0) {
                swapRules();
                System.out.print(steps);
            }
            steps--;
        }
        System.out.println();
        grid.populate(step.get(step.size() - 1));
        return step;
    }

    private void swapRules() {
        int aux = r1;
        r1 = r12;
        r12 = aux;
        aux = r2;
        r2 = r22;
        r22 = aux;
        aux = r3;
        r3 = r32;
        r32 = aux;
        aux = r4;
        r4 = r42;
        r42 = aux;
    }
}
