package tp4;

import tp4.models.DHMFrame;
import tp4.models.IntegrationMethod;
import tp4.models.WeightedParticle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class TP4A {

    public static void main(String[] args) {
        double deltaTime = 0.005;

        LennardJonesGrid g = new LennardJonesGrid(400, 5);
        ArrayList<WeightedParticle> ps = new ArrayList<>();

        ps.add(new WeightedParticle(0, 199.0, 150.0, 0, 0, 0, 0, 0.1, 0.1));
        Random a = new Random();
        Random r = new Random();
        for (int i = 1; i < 10000; i++) {
            double angle = a.nextDouble();
            double vx = 10.0 * Math.cos(angle * 2 * Math.PI);
            double vy = 10.0 * Math.sin(angle * 2 * Math.PI);
            double x, y;
            x = r.nextDouble() * 400;
            y = r.nextDouble() * 400;
            ps.add(new WeightedParticle(i, x, y, vx, vy, 0, 0, 0.1,  0.1));
        }
        g.populate(ps);

        g.calculateVecins();


        DampedHarmonicMotion analytic = new DampedHarmonicMotion(deltaTime, IntegrationMethod.ANALYTIC);
        DampedHarmonicMotion verlet = new DampedHarmonicMotion(deltaTime, IntegrationMethod.VERLET_LEAP_FROG);
        DampedHarmonicMotion beeman = new DampedHarmonicMotion(deltaTime, IntegrationMethod.BEEMAN);
        DampedHarmonicMotion gpc5 = new DampedHarmonicMotion(deltaTime, IntegrationMethod.GPC5);
        analytic.simulate();
        verlet.simulate();
        beeman.simulate();
        gpc5.simulate();
        StringBuilder sbx1 = new StringBuilder();
        StringBuilder sbx2 = new StringBuilder();
        StringBuilder sbx3 = new StringBuilder();
        StringBuilder sbx4 = new StringBuilder();
        sbx1.append("x1=[");
        sbx2.append("x2=[");
        sbx3.append("x3=[");
        sbx4.append("x4=[");
        for (int i = 0; i < analytic.frames.size(); i++) {
            sbx1.append(analytic.frames.get(i).getX()).append(",");
            sbx2.append(verlet.frames.get(i).getX()).append(",");
            sbx3.append(beeman.frames.get(i).getX()).append(",");
            sbx4.append(gpc5.frames.get(i).getX()).append(",");
            System.out.println(analytic.frames.get(i).toString());
            System.out.println(verlet.frames.get(i).toString());
            System.out.println(beeman.frames.get(i).toString());
            System.out.println(gpc5.frames.get(i).toString());
            System.out.println();
        }
        sbx1.append("];");
        sbx2.append("];");
        sbx3.append("];");
        sbx4.append("];");


        System.out.println(sbx1);
        System.out.println(sbx2);
        System.out.println(sbx3);
        System.out.println(sbx4);





        printFile(analytic.frames,verlet.frames,beeman.frames,gpc5.frames);
    }

    public static void printFile (List<DHMFrame> analyticFrames,List<DHMFrame> verletFrames,List<DHMFrame> beemanFrames,List<DHMFrame> gpc5Frames) {
        NumberFormat f = new DecimalFormat("#0.000000");
        NumberFormat timeFormat = new DecimalFormat("#0.000");
        StringBuilder sbp = new StringBuilder();
        for (int i = 0; i < analyticFrames.size(); i++) {
            sbp.append(timeFormat.format(analyticFrames.get(i).timestamp)).append(' ')
                    .append(f.format(analyticFrames.get(i).particle.getVx())).append(' ')
                    .append(f.format(verletFrames.get(i).particle.getVx())).append(' ')
                    .append(f.format(beemanFrames.get(i).particle.getVx())).append(' ')
                    .append(f.format(gpc5Frames.get(i).particle.getVx())).append('\n');
        }
        File file = new File("DHM.stat");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))) {
            writer.append(sbp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
