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
        int ej = 2; //CHANGE TO RUN EJ 2 OR 3
        if(ej == 2){
            double deltaTime = 0.01;
            DampedHarmonicMotion analytic = new DampedHarmonicMotion(deltaTime, IntegrationMethod.ANALYTIC);
            DampedHarmonicMotion verlet = new DampedHarmonicMotion(deltaTime, IntegrationMethod.VERLET_LEAP_FROG);
            DampedHarmonicMotion beeman = new DampedHarmonicMotion(deltaTime, IntegrationMethod.BEEMAN);
            DampedHarmonicMotion gpc5 = new DampedHarmonicMotion(deltaTime, IntegrationMethod.GPC5);
            double start = 0;
            double end = 0;
            start = System.nanoTime();
            analytic.simulate();
            end = System.nanoTime();
            System.out.println("Tiempo1: " + (end-start));

            start = System.nanoTime();
            verlet.simulate();
            end = System.nanoTime();
            System.out.println("Tiempo2: " + (end-start));

            start = System.nanoTime();
            beeman.simulate();
            end = System.nanoTime();
            System.out.println("Tiempo3: " + (end-start));

            start = System.nanoTime();
            gpc5.simulate();
            end = System.nanoTime();
            System.out.println("Tiempo4: " + (end-start));

            printFile(analytic.frames,verlet.frames,beeman.frames,gpc5.frames, deltaTime);
        }
        if (ej == 3){
            ArrayList<Double> deltaTimes = new ArrayList<>();
            ArrayList<Double> verletECM = new ArrayList<>();
            ArrayList<Double> beemanECM = new ArrayList<>();
            ArrayList<Double> gpc5ECM = new ArrayList<>();
            for (double deltaTime = 0.00001; deltaTime < 0.075; deltaTime+=0.00001) {
                System.out.println("Analizing: " + deltaTime + ", " + (deltaTime/0.076)*100 + "%");
                DampedHarmonicMotion analytic = new DampedHarmonicMotion(deltaTime, IntegrationMethod.ANALYTIC);
                DampedHarmonicMotion verlet = new DampedHarmonicMotion(deltaTime, IntegrationMethod.VERLET_LEAP_FROG);
                DampedHarmonicMotion beeman = new DampedHarmonicMotion(deltaTime, IntegrationMethod.BEEMAN);
                DampedHarmonicMotion gpc5 = new DampedHarmonicMotion(deltaTime, IntegrationMethod.GPC5);
                analytic.simulate();
                verlet.simulate();
                beeman.simulate();
                gpc5.simulate();
                deltaTimes.add(deltaTime);
                verletECM.add(calculateECM(analytic.frames,verlet.frames));
                beemanECM.add(calculateECM(analytic.frames,beeman.frames));
                gpc5ECM.add(calculateECM(analytic.frames,gpc5.frames));
            }
            printEj3(deltaTimes,verletECM,beemanECM,gpc5ECM);


        }
    }

    public static double calculateECM(List<DHMFrame> realFrames,
                               List<DHMFrame> predictedFrames){
        double ecm = 0;
        for (int i = 0; i < realFrames.size(); i++) {
            ecm += Math.pow(predictedFrames.get(i).particle.getX()-realFrames.get(i).particle.getX(),2);
        }
        ecm /= realFrames.size();
        return ecm;
    }

    public static void printFile (List<DHMFrame> analyticFrames,
                                  List<DHMFrame> verletFrames,
                                  List<DHMFrame> beemanFrames,
                                  List<DHMFrame> gpc5Frames,
                                  double deltaTime) {
//        NumberFormat f = new DecimalFormat("#0.0000000000");
//        NumberFormat timeFormat = new DecimalFormat("#0.0000000");
//        StringBuilder sbp = new StringBuilder();
//        sbp.append(timeFormat.format(deltaTime)).append(' ')
//                .append(analyticFrames.size()).append('\n');
//        sbp.append(f.format(calculateECM(analyticFrames,verletFrames))).append(' ')
//                .append(f.format(calculateECM(analyticFrames,beemanFrames))).append(' ')
//                .append(f.format(calculateECM(analyticFrames,gpc5Frames))).append('\n');
//        for (int i = 0; i < analyticFrames.size(); i++) {
//            sbp.append(f.format(analyticFrames.get(i).particle.getX())).append(' ')
//                .append(f.format(verletFrames.get(i).particle.getX())).append(' ')
//                .append(f.format(beemanFrames.get(i).particle.getX())).append(' ')
//                .append(f.format(gpc5Frames.get(i).particle.getX())).append('\n');
//        }
        NumberFormat f = new DecimalFormat("#0.0000000000");
        NumberFormat timeFormat = new DecimalFormat("#0.00000");
        StringBuilder sbp = new StringBuilder();
        sbp.append(timeFormat.format(deltaTime)).append(' ')
                .append(analyticFrames.size()).append('\n');
        sbp.append(calculateECM(analyticFrames,verletFrames)).append(' ')
                .append(calculateECM(analyticFrames,beemanFrames)).append(' ')
                .append(calculateECM(analyticFrames,gpc5Frames)).append('\n');
        for (int i = 0; i < analyticFrames.size(); i++) {
            sbp.append(analyticFrames.get(i).particle.getX()).append(' ')
                .append(verletFrames.get(i).particle.getX()).append(' ')
                .append(beemanFrames.get(i).particle.getX()).append(' ')
                .append(gpc5Frames.get(i).particle.getX()).append('\n');
        }
        File file = new File("DHM_" + timeFormat.format(deltaTime) + ".stat");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))) {
            writer.append(sbp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printEj3 (List<Double> times,
                                  List<Double> verletECM,
                                  List<Double> beemanECM,
                                  List<Double> gpc5ECM) {
        StringBuilder sbp = new StringBuilder();
        for (int i = 0; i < times.size(); i++) {
            sbp.append(times.get(i)).append(' ')
                    .append(verletECM.get(i)).append(' ')
                    .append(beemanECM.get(i)).append(' ')
                    .append(gpc5ECM.get(i)).append('\n');
        }
        File file = new File("DHM_ECM_" + times.get(0) + "-" + times.get(times.size()-1) + ".stat");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))) {
            writer.append(sbp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
