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
    }
}
