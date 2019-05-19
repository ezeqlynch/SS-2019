package tp4;

public class LennardJonesMain {
    public static void main(String[] args) {
        double deltaTime = 0.001;

        SimulatorLennardJones slj1 = new SimulatorLennardJones(1000, 10, deltaTime, 1);
//        deltaTime += 0.0001;
//
        SimulatorLennardJones slj2 = new SimulatorLennardJones(1000, 10, deltaTime, 2);
//        deltaTime += 0.0001;
        SimulatorLennardJones slj3 = new SimulatorLennardJones(1000, 10, deltaTime, 3);
//        deltaTime += 0.0001;
        SimulatorLennardJones slj4 = new SimulatorLennardJones(1000, 10, deltaTime, 4);
//        deltaTime += 0.0001;
        SimulatorLennardJones slj5 = new SimulatorLennardJones(1000, 10, deltaTime, 5);

        slj1.simulate();
//        slj2.simulate();
//        slj3.simulate();
//        slj4.simulate();
//        slj5.simulate();
    }
}
