import java.util.*;

public class SimulatorBrown {
    private int n;
    private ArrayList<BrownParticle> ps;
    private int collIndex1;
    private int collIndex2;
    private ArrayList<ArrayList<BrownParticle>> steps;

    public SimulatorBrown(int n) {
        this.n = n;
        this.ps = new ArrayList<>();
        steps = new ArrayList<>();
    }

    public void generateParticles() {
        ps.add(new BrownParticle(0.25, 0.25, 0, 0, true, 0));
        Random r = new Random();
        Random a = new Random();
        for (int i = 1; i < n; i++) {
            double radius = r.nextDouble();
            double angle = a.nextDouble();
            double vx = Math.sqrt(radius) * 0.1 * Math.cos(angle * 2 * Math.PI);
            double vy = Math.sqrt(radius) * 0.1 * Math.sin(angle * 2 * Math.PI);
            double x, y;
            do {
                x = r.nextDouble() * 0.49 + 0.005;
                y = r.nextDouble() * 0.49 + 0.005;
            } while(Calculator.isOverlapping(x, y, ps));
            ps.add(new BrownParticle(x, y, vx, vy, false, i));
        }
    }

    public double findNextCollision() {
//        List<BrownParticle> ls = copy of list;
        ArrayList<BrownParticle> nl = (ArrayList<BrownParticle>) ps.clone();
        double nextColl = 1000;
        for (int i = 0; i < n; i++) {
            BrownParticle p = ps.get(i);
            nl.remove(p);
            double tcoll = 1000;
            for(BrownParticle p2 : nl){
                tcoll = Calculator.timeToCollision(p, p2);
                p.setNextColl(p2.getIndex(), tcoll);
                p2.setNextColl(p.getIndex(), tcoll);
                if(tcoll > 1e-6 && tcoll < nextColl) {
                    nextColl = tcoll;
                    collIndex1 = p.getIndex();
                    collIndex2 = p2.getIndex();
                }
            }
            if(p.getVx() > 0) { //left wall
                tcoll = (0.5 - p.getRadius() - p.getX()) / p.getVx();
                p.setNextColl(-2, tcoll);
                if(tcoll > 1e-6 && tcoll < nextColl) {
                    nextColl = tcoll;
                    collIndex1 = p.getIndex();
                    collIndex2 = -2;
                }
            } else if( p.getVx() < 0) { //right wall
                tcoll = (p.getRadius() - p.getX()) / p.getVx();
                p.setNextColl(-1, tcoll);
                if(tcoll > 1e-6 && tcoll < nextColl) {
                    nextColl = tcoll;
                    collIndex1 = p.getIndex();
                    collIndex2 = -1;
                }
            }
            if(p.getVy() > 0) { //down wall
                tcoll = (0.5 - p.getRadius() - p.getY()) / p.getVy();
                p.setNextColl(-4, tcoll);
                if(tcoll > 1e-6 && tcoll < nextColl) {
                    nextColl = tcoll;
                    collIndex1 = p.getIndex();
                    collIndex2 = -4;
                }
            } else if( p.getVy() < 0) { //up wall
                tcoll = (p.getRadius() - p.getY()) / p.getVy();
                p.setNextColl(-3, tcoll);
                if(tcoll > 1e-6 && tcoll < nextColl) {
                    nextColl = tcoll;
                    collIndex1 = p.getIndex();
                    collIndex2 = -3;
                }
            }
        }
        return nextColl;
    }

    public void evolveParticles(double time) {
        ps.stream().parallel().forEach(p -> {
            p.setX(p.getX() + p.getVx() * time);
            p.setY(p.getY() + p.getVy() * time);
        });
    }

    public void makeCollision() {
        BrownParticle p1 = ps.get(collIndex1);
        if(collIndex2 < 0){
            switch (collIndex2){
                case -1:
                case -2: p1.setVx(p1.getVx() * -1);
                case -3: //up
                case -4: p1.setVy(p1.getVy() * -1);//down
            }
            return;
        }
        BrownParticle p2 = ps.get(collIndex2);
        double J = Calculator.calculateJ(p1, p2);
        double Jx = J * (p2.getX() - p1.getX()) / (p1.getRadius() + p2.getRadius());
        double Jy = J * (p2.getY() - p1.getY()) / (p1.getRadius() + p2.getRadius());
        p1.setVx(p1.getVx() + Jx/p1.getMass());
        p1.setVy(p1.getVy() + Jy/p1.getMass());
        p2.setVx(p2.getVx() - Jx/p2.getMass());
        p2.setVy(p2.getVy() - Jy/p2.getMass());
    }

    public void simulate(double totalTime) {
        this.generateParticles();
        int step = 0;
        while(totalTime > 0){
            double time = this.findNextCollision();
            this.evolveParticles(time);
            if(collIndex1 == 0 && collIndex2 < 0){
                return;
            }
            this.makeCollision();
            steps.add(step, cloneList());
            ps = steps.get(step);
            totalTime -= time;
            step++;
            System.out.println(totalTime);
        }

    }

    public ArrayList<BrownParticle> cloneList() {
        ArrayList<BrownParticle> l = new ArrayList<>();
        for (BrownParticle p : ps) {
            l.add(p.clone());
        }
        return l;

    }
    public List<BrownParticle> getParticles() {
        return ps;
    }

    public ArrayList<ArrayList<BrownParticle>> getSteps() {
        return steps;
    }

}
