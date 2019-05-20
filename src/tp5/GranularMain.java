package tp5;

public class GranularMain {
    public static final double L = 1.5;
    public static final double W = 0.4;
    public static final double D = 0.25;

    public static void main(String[] args) {
        double deltaTime = 0.0001;

        SimulatorGranular slj1 = new SimulatorGranular(deltaTime, 1);

        slj1.simulate();
    }
}
