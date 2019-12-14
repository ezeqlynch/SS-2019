package tp5;

public class GranularMain {
    public static final double L = 0.4;
    public static final double W = 0.4;
    public static final double H = 1.5;
    public static double D = 0;
    public static double GAMMA = 70;

    public static void main(String[] args) {
        double deltaTime = 0.000001;

        SimulatorGranular slj1 = new SimulatorGranular(deltaTime, 1);

        slj1.simulate();

    }
}
