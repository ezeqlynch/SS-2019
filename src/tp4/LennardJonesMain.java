package tp4;

public class LennardJonesMain {
    public static void main(String[] args) {
        double deltaTime = 0.005;

        SimulatorLennardJones slj1 = new SimulatorLennardJones(1000, 10, deltaTime, 1);
        SimulatorLennardJones slj2 = new SimulatorLennardJones(1000, 10, deltaTime, 2);
        SimulatorLennardJones slj3 = new SimulatorLennardJones(1000, 10, deltaTime, 3);
        SimulatorLennardJones slj4 = new SimulatorLennardJones(1000, 10, deltaTime, 4);
        SimulatorLennardJones slj5 = new SimulatorLennardJones(1000, 10, deltaTime, 5);

        slj1.simulate();
//        slj2.simulate();
//        slj3.simulate();
//        slj4.simulate();
//        slj5.simulate();
    }
}
