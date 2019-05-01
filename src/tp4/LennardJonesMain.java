package tp4;

public class LennardJonesMain {
    public static void main(String[] args) {
        double deltaTime = 0.001;

        SimulatorLennardJones slj = new SimulatorLennardJones(1000, 10, deltaTime);

        slj.simulate();
    }
}
