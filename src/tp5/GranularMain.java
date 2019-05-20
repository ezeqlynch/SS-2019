package tp5;

public class GranularMain {
    public static void main(String[] args) {
        double deltaTime = 0.0001;

        SimulatorGranular slj1 = new SimulatorGranular(1, 0.3, 0.5,deltaTime, 1);

        slj1.simulate();
    }
}
