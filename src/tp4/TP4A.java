package tp4;

import tp4.models.IntegrationMethod;

public class TP4A {

    public static void main(String[] args) {
        double deltaTime = 0.005;
        DampedHarmonicMotion analytic = new DampedHarmonicMotion(deltaTime, IntegrationMethod.ANALYTIC);
        DampedHarmonicMotion verlet = new DampedHarmonicMotion(deltaTime, IntegrationMethod.VERLET_LEAP_FROG);
        DampedHarmonicMotion beeman = new DampedHarmonicMotion(deltaTime, IntegrationMethod.BEEMAN);
        DampedHarmonicMotion gpc5 = new DampedHarmonicMotion(deltaTime, IntegrationMethod.GPC5);
        analytic.simulate();
        verlet.simulate();
        beeman.simulate();
        gpc5.simulate();
        for (int i = 0; i < analytic.frames.size(); i++) {
            System.out.println(analytic.frames.get(i).toString());
            System.out.println(verlet.frames.get(i).toString());
            System.out.println(beeman.frames.get(i).toString());
            System.out.println(gpc5.frames.get(i).toString());
            System.out.println();
        }
    }
}
