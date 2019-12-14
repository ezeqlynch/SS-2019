package tp5;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatorGranular {


    private double L;
    private double W;
    private double H;
    private double D;
    private double initVel;
    private double deltaTime;
    private GranularGrid grid;
    private ArrayList<GranularParticle> ps;
    private ArrayList<GranularParticle> sim;
    private ArrayList<ArrayList<GranularParticle>> steps;
    private ArrayList<Double> timedown;
    private int index = 0;


    public SimulatorGranular(double deltaTime, int index){
        this.L = GranularMain.L;
        this.D = GranularMain.D;
        this.W = GranularMain.W;
        this.H = GranularMain.H;
        this.deltaTime = deltaTime;
        this.steps = new ArrayList<>();
        this.timedown = new ArrayList<>();
        this.index = index;
    }

    public void generateParticles(){
        grid = new GranularGrid(L, W, H);
        ps = new ArrayList<>();

        Random a = new Random();
        Random r = new Random();
        double overlapped = 0;
        for (int i = 0; overlapped < 1000; i++) {
            double radius = (0.01 * a.nextDouble() + 0.02) / 2;
            double x, y, z;
            overlapped = 0;
            do{
                x = r.nextDouble() * (W - 2 * radius) + radius;
                y = r.nextDouble() * (L  - 2 * radius) + radius;
                z = r.nextDouble() * (H  - 2 * radius) + radius;
                overlapped++;
            } while(isOverlappingLJ(x, y, z, radius, ps) && overlapped < 1000);
            if(overlapped < 1000)
                ps.add(new GranularParticle(i, x, y, z,0,0, 0, 0, 0, -9.8, radius,  0.01));
            if(ps.size() > 1000)
                break;
        }
//        ps.add(new GranularParticle(1, 0.1, 0.2, 0, 0, 0, -9.8, 0.011,  0.01));
//        ps.add(new GranularParticle(2, 0.12, 0.1, 0, 0, 0, -9.8, 0.012,  0.01));
        System.out.println(ps.size());
        grid.populate(ps);
        grid.calculateVecins();
        ps.parallelStream().forEach(p -> {
            //current.getVx() - this.calculateXAcceleration(current) * (deltaTime/2);
            p.calculateTotalForce();
            p.setPrevVx(p.getVx() - p.getAx() * (deltaTime / 2));
            p.setPrevVy(p.getVy() - p.getAy() * (deltaTime / 2));
            p.setPrevVz(p.getVz() - p.getAz() * (deltaTime / 2));
        });
    }

    public void simulate() {
        int saveCounter = (int) Math.max(1.0, Math.floor((1.0/deltaTime)/60.0)); // para q 30 farmes => 1 segundo
//        int saveCounter = 10;
        System.out.println(saveCounter);
        this.generateParticles();
        steps.add(cloneList(ps));
        int step = 1;
        sim = cloneList(ps);

        while(true) {

            // paredes
            // actualizar las fuerzas
            sim.parallelStream().forEach(GranularParticle::calculateTotalForce);
            // actualizar posiciones y velocidades
            final int stepAux = step;
            sim.parallelStream().forEach(p -> verletLeapFrogUpdate(p, stepAux));

            // chequear ratio izq/der
            // guardar estado y chequear tiempo
            // clonar lista
            sim = cloneList(sim);
            // calcular vecinos (a lo mejor se puede calcular cada x pasos)
            grid.populate(sim);
            grid.calculateVecins();



            if(step % saveCounter == 0) {
//                double egy = sim.parallelStream().mapToDouble(GranularParticle::getKineticEnergy).average().getAsDouble();
////                if(egy < 1e-6 && steps.size() > 10){ //al principio es 0
////                    calcPositions();
////                    return;
////                }
                steps.add(cloneList(sim));
                if(steps.size() % 60 == 0)
                    System.out.println(steps.size());
            }
            if(steps.size() > 600){
                calcPositions();
                return;
            }
            step++;
        }
    }

    private void verletLeapFrogUpdate(GranularParticle p, int step) {
        double interVX = p.getPrevVx() + deltaTime * p.getAx();
        double interVY = p.getPrevVy() + deltaTime * p.getAy();
        double interVZ = p.getPrevVz() + deltaTime * p.getAz();
        p.setX(p.getX() + deltaTime*interVX);
        p.setY(p.getY() + deltaTime*interVY);
        p.setZ(p.getZ() + deltaTime*interVZ);
        p.setVx((p.getPrevVx() + interVX)/2);
        p.setVy((p.getPrevVy() + interVY)/2);
        p.setVz((p.getPrevVz() + interVZ)/2);
        p.setPrevVx(interVX);
        p.setPrevVy(interVY);
        p.setPrevVz(interVZ);
        if(!p.isWentDown() && p.getZ() < 0){
            p.setWentDown(p.getZ() < 0);
            timedown.add(step * deltaTime);
        }
        if(p.getZ() < -0.4){
            double x; double y; double z; Random r = new Random();
            do{
                x = r.nextDouble() * (W - 2 * p.getRadius()) + p.getRadius();
                y = r.nextDouble() * (L  - 2 * p.getRadius()) + p.getRadius(); //75% superior
                z = r.nextDouble() * (H  - 2 * p.getRadius()) * 0.4 + H * 0.6; //75% superior
            } while(isOverlappingLJ(x, y, z, p.getRadius(), sim));
            p.setX(x);
            p.setY(y);
            p.setZ(z);
            p.setPrevVx(0);
            p.setPrevVy(0);
            p.setPrevVz(0);
            p.setVx(0);
            p.setVy(0);
            p.setVz(0);
            p.setAx(0);
            p.setAy(0);
            p.setAz(0);
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
        String name = GranularMain.D + "-" + index + "-" + System.currentTimeMillis();
        try(FileWriter fw = new FileWriter(name+".xyz", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            DecimalFormat f = new DecimalFormat("#0.00000");
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
            otherSymbols.setDecimalSeparator('.');
            otherSymbols.setGroupingSeparator('.');
            f.setDecimalFormatSymbols(otherSymbols);
            for(ArrayList<GranularParticle> a : steps) {
                out.println(a.size());
                out.println();
                for (GranularParticle p : a) {
                    out.println(p.getIndex() + " " + f.format(p.getX() > 999 ? 999 : p.getX()) + " " + f.format(p.getY()> 999? 999:p.getY()) + " " + f.format(p.getZ()> 999? 999:p.getZ()) +
                            " " + p.getRadius() +
                            " " + 125.0);
//                            + " " + p.getKineticEnergy());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        try(FileWriter fw = new FileWriter(name+".stats", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            NumberFormat f = new DecimalFormat("#0.00000");
            out.println(steps.get(0).size());
            for(ArrayList<GranularParticle> a : steps) {
                out.println(a.parallelStream().mapToDouble(GranularParticle::getKineticEnergy).average().getAsDouble());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(timedown.size() > 0){

            try(FileWriter fw = new FileWriter(name+"-times.stats", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
                NumberFormat f = new DecimalFormat("#0.00000");
                out.println(f.format(timedown.get(0)));
                for (int i = 1; i < timedown.size(); i++) {
                    out.println(f.format(-timedown.get(i-1) + timedown.get(i)));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isOverlappingLJ(double x, double y, double z, double radius, List<GranularParticle> b) {
        return b.stream().anyMatch(p -> Math.pow(x - p.getX(), 2) + Math.pow(y - p.getY(), 2) + Math.pow(z - p.getZ(), 2) <= Math.pow(radius + p.getRadius(), 2));
    }

}
