package tp5;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatorGranular {


    private double L;
    private double W;
    private double D;
    private double initVel;
    private double deltaTime;
    private GranularGrid grid;
    private ArrayList<GranularParticle> ps;
    private ArrayList<ArrayList<GranularParticle>> steps;
    private int index = 0;

    public SimulatorGranular(double L, double W, double D, double deltaTime, int index){
        this.L = L;
        this.D = D;
        this.W = W;
        this.deltaTime = deltaTime;
        this.steps = new ArrayList<>();
        this.index = index;
    }

    public void generateParticles(){
        grid = new GranularGrid(L, W);
        ps = new ArrayList<>();

        Random a = new Random();
        Random r = new Random();
        double overlapped = 0;
        for (int i = 0; overlapped < 100; i++) {
            double radius = (0.01 * a.nextDouble() + 0.02) / 2;
            double x, y;
            overlapped = 0;
            do{
                x = r.nextDouble() * (W - 2 * radius) + radius;
                y = r.nextDouble() * (L  - 2 * radius) * 0.9 + radius; //30% superior
                overlapped++;
            } while(isOverlappingLJ(x, y, radius, ps) && overlapped < 100);
            if(overlapped < 100)
                ps.add(new GranularParticle(i, x, y, 0, 0, 0, -9.8, radius,  0.01));
        }
//        ps.add(new GranularParticle(1, 0.1, 0.2, 0, 0, 0, -9.8, 0.011,  0.01));
//        ps.add(new GranularParticle(2, 0.12, 0.1, 0, 0, 0, -9.8, 0.012,  0.01));

        grid.populate(ps);
        grid.calculateVecins();
        ps.parallelStream().forEach(p -> {
            //current.getVx() - this.calculateXAcceleration(current) * (deltaTime/2);
            p.calculateTotalForce();
            p.setPrevVx(p.getVx() - p.getAx() * (deltaTime / 2));
            p.setPrevVy(p.getVy() - p.getAy() * (deltaTime / 2));
        });
    }

    public void simulate() {
//        int saveCounter = (int) Math.max(1.0, Math.floor((1.0/deltaTime)/30.0)); // para q 30 farmes => 1 segundo
        int saveCounter = 10;
        System.out.println(saveCounter);
        this.generateParticles();
        steps.add(cloneList(ps));
        int step = 1;
        ArrayList<GranularParticle> sim = cloneList(ps);

        while(true) {

            // paredes
            // actualizar las fuerzas
            sim.parallelStream().forEach(GranularParticle::calculateTotalForce);
            // actualizar posiciones y velocidades
            sim.parallelStream().forEach(this::verletLeapFrogUpdate);

            // chequear ratio izq/der
            // guardar estado y chequear tiempo
            // clonar lista
            sim = cloneList(sim);
            // calcular vecinos (a lo mejor se puede calcular cada x pasos)
            grid.populate(sim);
            grid.calculateVecins();
            if(step % saveCounter == 0) {
                steps.add(cloneList(sim));
            }
            if(steps.size() > 10000){
                calcPositions();
                steps.clear();
                return;
            }
            if(step % 1000 == 0){
                System.out.println(step);
            }
            step++;
        }
    }

    private void verletLeapFrogUpdate(GranularParticle p) {
        double interVX = p.getPrevVx() + deltaTime * p.getAx();
        double interVY = p.getPrevVy() + deltaTime * p.getAy();
        p.setX(p.getX() + deltaTime*interVX);
        p.setY(p.getY() + deltaTime*interVY);
        p.setVx((p.getPrevVx() + interVX)/2);
        p.setVy((p.getPrevVy() + interVY)/2);
        p.setPrevVx(interVX);
        p.setPrevVy(interVY);
        p.setWentDown(p.getY() < 0);
        if(p.getY() < -0.4){
            p.setY(Math.random() * 0.2 + 0.75);
            p.setPrevVx(0);
            p.setPrevVy(0);
            p.setVx(0);
            p.setVy(0);
            p.setAx(0);
            p.setAy(0);
            p.setWentDown(false);
        }
    }


    public ArrayList<GranularParticle> cloneList(ArrayList<GranularParticle> ps) {
        ArrayList<GranularParticle> l = new ArrayList<>();
        for (GranularParticle p : ps) {
            l.add(p.clone());
        }
        return l;
    }

    public void calcPositions() {
        try(FileWriter fw = new FileWriter("starting28-test-"+index+".xyz", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            NumberFormat f = new DecimalFormat("#0.00000");
            for(ArrayList<GranularParticle> a : steps) {
                out.println(a.size());
                out.println();
                for (GranularParticle p : a) {
                    out.println(p.getIndex() + " " + f.format(p.getX() > 999 ? 999 : p.getX()) + " " + f.format(p.getY()> 999? 999:p.getY()) +
                            " " + p.getRadius());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isOverlappingLJ(double x, double y, double radius, List<GranularParticle> b) {
        return b.stream().anyMatch(p -> Math.pow(x - p.getX(), 2) + Math.pow(y - p.getY(), 2) <= Math.pow(radius + p.getRadius(), 2));
    }

}
