package tp5;

public class GranularMain {
    public static final double L = 1.5;
    public static final double W = 0.4;
    public static final double D = 0;

    public static void main(String[] args) {
        double deltaTime = 0.00001;

        SimulatorGranular slj1 = new SimulatorGranular(deltaTime, 1);
        SimulatorGranular slj2 = new SimulatorGranular(deltaTime, 2);
        SimulatorGranular slj3 = new SimulatorGranular(deltaTime, 3);

        slj1.simulate();
        slj2.simulate();
        slj3.simulate();
    }
}
