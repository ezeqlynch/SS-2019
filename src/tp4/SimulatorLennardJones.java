package tp4;

import tp4.models.WeightedParticle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatorLennardJones {

    private int n;
    private double initVel;
    private double deltaTime;
    private LennardJonesGrid grid;
    private ArrayList<WeightedParticle> ps;
    private ArrayList<ArrayList<WeightedParticle>> steps;
    private int index = 0;

    public SimulatorLennardJones(int n, double initVel, double deltaTime, int index){
        this.n = n;
        this.initVel = initVel;
        this.deltaTime = deltaTime;
        this.steps = new ArrayList<>();
        this.index = index;
    }

    public void generateParticles(){
        grid = new LennardJonesGrid(200, 5);
        ps = new ArrayList<>();

        Random a = new Random();
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            double angle = a.nextDouble();
            double vx = initVel * Math.cos(angle * 2 * Math.PI);
            double vy = initVel * Math.sin(angle * 2 * Math.PI);
            double x, y;
            do{
                x = r.nextDouble() * 198 + 1;
                y = r.nextDouble() * 198 + 1;
            } while(isOverlappingLJ(x, y, ps));
            ps.add(new WeightedParticle(i, x, y, vx, vy, 0, 0, 0,  0.1, 2, 1));
        }
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
        int saveCounter = (int) Math.max(1.0, Math.floor((1.0/deltaTime)/30.0)); // para q 30 farmes => 1 segundo
        System.out.println(saveCounter);
        this.generateParticles();
        steps.add(cloneList(ps));
        int step = 1;
        ArrayList<WeightedParticle> sim = cloneList(ps);

        while(true) {

            // paredes
            // actualizar las fuerzas
            sim.parallelStream().forEach(WeightedParticle::calculateTotalForce);
            sim.parallelStream().forEach(WeightedParticle::calculatePotentialEnergyTotal);
            // actualizar posiciones y velocidades
            sim.parallelStream().forEach(this::verletLeapFrogUpdate);

            // chequear ratio izq/der
            long right = sim.parallelStream().filter(p -> p.getX() > 200).count();
            if(right > n/2){
                calcPositions();
                return;
            }
            // guardar estado y chequear tiempo
            // clonar lista
            sim = cloneList(sim);
            // calcular vecinos (a lo mejor se puede calcular cada x pasos)
            grid.populate(sim);
            grid.calculateVecins();
            if(step % saveCounter == 0) {
                steps.add(cloneList(sim));
            }
            if(steps.size() > 2000){
                calcPositions();
                steps = new ArrayList<>();
                return;
            }
            if(step % 1000 == 0){
                System.out.println(step);
                System.out.println(right);
            }
            step++;
        }
    }

    private void verletLeapFrogUpdate(WeightedParticle p) {
        double interVX = p.getPrevVx() + deltaTime * p.getAx();
        double interVY = p.getPrevVy() + deltaTime * p.getAy();
        p.setX(p.getX() + deltaTime*interVX);
        p.setY(p.getY() + deltaTime*interVY);
        p.setVx((p.getPrevVx() + interVX)/2);
        p.setVy((p.getPrevVy() + interVY)/2);
        p.setPrevVx(interVX);
        p.setPrevVy(interVY);
    }


    public ArrayList<WeightedParticle> cloneList(ArrayList<WeightedParticle> ps) {
        ArrayList<WeightedParticle> l = new ArrayList<>();
        for (WeightedParticle p : ps) {
            l.add(p.clone());
        }
        return l;
    }

    public void calcPositions() {
        try(FileWriter fw = new FileWriter(n + "-test-"+index+".xyz", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            NumberFormat f = new DecimalFormat("#0.000");
            for(ArrayList<WeightedParticle> a : steps) {
                out.println(a.size());
                out.println();
                for (WeightedParticle p : a) {
                    out.println(p.getIndex() + " " + f.format(p.getX() > 999 ? 999 : p.getX()) + " " + f.format(p.getY()> 999? 999:p.getY()) +
                            " " + f.format(p.getVx()) + " " + f.format(p.getVy()) + " " + f.format(p.getV()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isOverlappingLJ(double x, double y, List<WeightedParticle> b) {
        return b.stream().anyMatch(p -> Math.pow(x - p.getX(), 2) + Math.pow(y - p.getY(), 2) <= Math.pow(1 /*rm*/ + p.getRadius(), 2));
    }

}
