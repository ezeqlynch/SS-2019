package tp6;

public class MultitudeMain {
    public static void main(String[] args) {
        double deltaTime = 0.01;

        SimulatorMultitude slj1 = new SimulatorMultitude(300, deltaTime, 29);
//        deltaTime += 0.0001;
//
//        SimulatorMultitude slj2 = new SimulatorMultitude(1000, deltaTime, 2);
//        deltaTime += 0.0001;
//        SimulatorMultitude slj3 = new SimulatorMultitude(1000, deltaTime, 3);
//        deltaTime += 0.0001;
//        SimulatorMultitude slj4 = new SimulatorMultitude(1000, deltaTime, 4);
//        deltaTime += 0.0001;
//        SimulatorMultitude slj5 = new SimulatorMultitude(1000, deltaTime, 5);

        slj1.simulate();
//        slj2.simulate();
//        slj3.simulate();
//        slj4.simulate();
//        slj5.simulate();
    }
}
