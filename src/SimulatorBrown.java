import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class SimulatorBrown {
    private int n;
    private ArrayList<BrownParticle> ps;
    private int collIndex1;
    private int collIndex2;
    private double maxSpeed;
    private ArrayList<ArrayList<BrownParticle>> steps;
    private ArrayList<Collision> colls;

    public SimulatorBrown(int n, double maxSpeed) {
        this.n = n;
        this.maxSpeed = maxSpeed;
    }

    public void generateParticles() {
        ps.add(new BrownParticle(0.25, 0.25, 0, 0, true, 0));
        Random r = new Random();
        Random a = new Random();
        for (int i = 1; i < n; i++) {
            double radius = r.nextDouble();
            double angle = a.nextDouble();
            double vx = Math.sqrt(radius) * maxSpeed * Math.cos(angle * 2 * Math.PI);
            double vy = Math.sqrt(radius) * maxSpeed * Math.sin(angle * 2 * Math.PI);
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
                if(tcoll < nextColl) {
                    nextColl = tcoll;
                    collIndex1 = p.getIndex();
                    collIndex2 = p2.getIndex();
                }
            }
            if(p.getVx() > 0) { //left wall
                tcoll = (0.5 - p.getRadius() - p.getX()) / p.getVx();
                p.setNextColl(-2, tcoll);
                if(tcoll > 1e-9 && tcoll < nextColl) {
                    nextColl = tcoll;
                    collIndex1 = p.getIndex();
                    collIndex2 = -2;
                }
            } else if( p.getVx() < 0) { //right wall
                tcoll = (p.getRadius() - p.getX()) / p.getVx();
                p.setNextColl(-1, tcoll);
                if(tcoll > 1e-9 && tcoll < nextColl) {
                    nextColl = tcoll;
                    collIndex1 = p.getIndex();
                    collIndex2 = -1;
                }
            }
            if(p.getVy() > 0) { //down wall
                tcoll = (0.5 - p.getRadius() - p.getY()) / p.getVy();
                p.setNextColl(-4, tcoll);
                if(tcoll > 1e-9 && tcoll < nextColl) {
                    nextColl = tcoll;
                    collIndex1 = p.getIndex();
                    collIndex2 = -4;
                }
            } else if( p.getVy() < 0) { //up wall
                tcoll = (p.getRadius() - p.getY()) / p.getVy();
                p.setNextColl(-3, tcoll);
                if(tcoll > 1e-9 && tcoll < nextColl) {
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
                case -2: p1.setVx(p1.getVx() * -1);break;
                case -3: //up
                case -4: p1.setVy(p1.getVy() * -1);break;//down
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

    public void simulate(double totalTime, int index) {
        long timen = System.nanoTime();
        this.ps = new ArrayList<>();
        this.steps = new ArrayList<>();
        this.colls = new ArrayList<>();
        this.generateParticles();
        int step = 1;
        steps.add(cloneList());
        double totalTimeFixed = totalTime;
        while(totalTime > 0){
            double time = this.findNextCollision();
            this.evolveParticles(time);
            if(collIndex1 == 0 && collIndex2 < 0){
                System.out.println(totalTime);
                System.out.println(System.nanoTime() - timen);
                Main.printOvito(steps, colls, index, n, totalTimeFixed - totalTime);
                if(index == 0){
                    calcPositions(index, totalTimeFixed - totalTime);
                }
                return;
            }
            this.makeCollision();
            steps.add(step, cloneList());
            totalTime -= time;
            colls.add(new Collision(collIndex1, collIndex2, totalTimeFixed - totalTime));
            step++;
            if(step % 5000 == 0){
                System.out.println(totalTime);
            }
        }
        System.out.println(totalTime);
        System.out.println(System.nanoTime() - timen);
        if(index == 0){
            calcPositions(index, totalTimeFixed - totalTime);
        }
        Main.printOvito(steps, colls, index, n, totalTimeFixed - totalTime);
    }

    public void calcPositions(int index, double totTime) {
        NumberFormat ftime = new DecimalFormat("#0.000");
        int frames = (int)totTime * 60;
        try(FileWriter fw = new FileWriter(n + "-"+index+"-test-" +ftime.format((double)steps.size()/frames) + ".xyz", false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            NumberFormat f = new DecimalFormat("#0.000000");
            int counter = -1;
            for(ArrayList<BrownParticle> a : steps) {
                    out.println(a.size() + 2);
                    out.println();
                out.println("-1 0 0 0 1");
                out.println("-2 0.5 0.5 0 1");
                for (BrownParticle p : a) {
                        if(counter >= 0 && (colls.get(counter).getIndex1() == p.getIndex() || colls.get(counter).getIndex2() == p.getIndex())) {
                            out.println(p.getIndex() + " " + f.format(p.getX()) + " " + f.format(p.getY()) + " " + p.getRadius() + " 1");
                        } else {
                            out.println(p.getIndex() + " " + f.format(p.getX()) + " " + f.format(p.getY()) + " " + p.getRadius() + " 0.3");
                        }

                    }
                    counter++;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<BrownParticle> cloneList() {
        ArrayList<BrownParticle> l = new ArrayList<>();
        for (BrownParticle p : ps) {
            l.add(p.clone());
        }
        return l;
    }

    public ArrayList<Collision> getColls() {
        return colls;
    }

    public List<BrownParticle> getParticles() {
        return ps;
    }

    public ArrayList<ArrayList<BrownParticle>> getSteps() {
        return steps;
    }

}
