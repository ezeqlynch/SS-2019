package tp6;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatorMultitude {

    private double L = 20;
    private int n;
    private double deltaTime;
    private MultitudeGrid grid;
    private ArrayList<MultitudeParticle> ps;
    private ArrayList<ArrayList<MultitudeParticle>> steps;
    private double tau = 0.4; //tiempo de rellegado al max r
    private int index = 0;

    public SimulatorMultitude(int n, double deltaTime, int index){
        this.n = n;
        this.deltaTime = deltaTime;
        this.steps = new ArrayList<>();
        this.index = index;
    }

    public void generateParticles(){
        grid = new MultitudeGrid(L);
        ps = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            double x, y;
            do{
                x = r.nextDouble() * (L - 2*MultitudeParticle.R_MIN) + MultitudeParticle.R_MIN;
                y = r.nextDouble() * (L - 2*MultitudeParticle.R_MIN) + MultitudeParticle.R_MIN;
            } while(isOverlappingM(x, y, MultitudeParticle.R_MIN, ps));
            ps.add(new MultitudeParticle(i, x, y, 0, 0, MultitudeParticle.R_MIN));
        }
        grid.populate(ps);
        grid.calculateVecins();
    }

    public void simulate() {
        int saveCounter = (int) Math.max(1.0, Math.floor((1.0/deltaTime)/30.0)); // para q 30 farmes => 1 segundo
//        int saveCounter = 1;
        System.out.println(saveCounter);
        this.generateParticles();
        steps.add(cloneList(ps));
        int step = 1;
        ArrayList<MultitudeParticle> sim = cloneList(ps);

        while(true) {

            // actualizar posiciones y velocidades
            sim.parallelStream().forEach(MultitudeParticle::calculateTarget);
            sim.parallelStream().forEach(this::updootPartiquel);

            // guardar estado y chequear tiempo
            // clonar lista
            sim = cloneList(sim);
            // calcular vecinos (a lo mejor se puede calcular cada x pasos)
            grid.populate(sim);
            grid.calculateVecins();
            if(step % saveCounter == 0) {
                steps.add(cloneList(sim));
            }
            if(steps.size() > 1000){
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

    private void updootPartiquel(MultitudeParticle p){
        //calcula velocidades bassado en sus vecinos o su radio y dsp calcula las nuevas posiciones
        double vx = p.getVx();
        double vy = p.getVy();
        if(p.getRadius() < MultitudeParticle.R_MAX && p.getRadius() != MultitudeParticle.R_MIN){ // si el radio es menor q el max
            double vd = MultitudeParticle.V_MAX * (p.getRadius() - MultitudeParticle.R_MIN) / (MultitudeParticle.R_MAX - MultitudeParticle.R_MIN);
            double ex = (p.target.x - p.getX())/p.distToTarget();
            double ey = (p.target.y - p.getY())/p.distToTarget();
            p.setVx(vd * ex);
            p.setVy(vd * ey);
        }
        //check walls aca?
        
        double r = p.getRadius() + MultitudeParticle.R_MAX/(tau/deltaTime);
        p.setRadius(r > MultitudeParticle.R_MAX ? MultitudeParticle.R_MAX : r);
        p.setX(p.getX() + vx * deltaTime);
        p.setY(p.getY() + vy * deltaTime);
    }

    public ArrayList<MultitudeParticle> cloneList(ArrayList<MultitudeParticle> ps) {
        ArrayList<MultitudeParticle> l = new ArrayList<>();
        for (MultitudeParticle p : ps) {
            l.add(p.clone());
        }
        return l;
    }

    public void calcPositions() {
        try(FileWriter fw = new FileWriter(n + "-test-"+index+".xyz", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            NumberFormat f = new DecimalFormat("#0.000");
            for(ArrayList<MultitudeParticle> a : steps) {
                out.println(a.size());
                out.println();
                for (MultitudeParticle p : a) {
                    out.println(p.getIndex() + " " + f.format(p.getX() > 999 ? 999 : p.getX()) + " " + f.format(p.getY()> 999? 999:p.getY()) +
                            " " + f.format(p.getRadius()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isOverlappingM(double x, double y, double radius, List<MultitudeParticle> b) {
        return b.stream().anyMatch(p -> Math.pow(x - p.getX(), 2) + Math.pow(y - p.getY(), 2) <= Math.pow(radius + p.getRadius(), 2));
    }
    
   
}
