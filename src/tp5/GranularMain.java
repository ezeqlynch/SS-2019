package tp5;

public class GranularMain {
    public static final double L = 0.4;
    public static final double W = 0.4;
    public static final double H = 1.5;
    public static double[] wall1;
    public static double[] wall2;
    public static double[] wall3;
    public static double[] wall4;
    public static double D = 0.15;
    public static double GAMMA = 70;

    public static void main(String[] args) {
        double deltaTime = 0.00001;

        SimulatorGranular slj1 = new SimulatorGranular(deltaTime, 5);

        slj1.simulate();
    }
}
