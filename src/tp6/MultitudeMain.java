package tp6;

public class MultitudeMain {
    public static void main(String[] args) {
        double deltaTime = 0.01;
        int runsNum = 1;

        for (double velocity = 4.5; velocity < 10.1; velocity+=0.5) {
            MultitudeParticle.V_MAX = velocity;
            for (int i = 1; i <= runsNum; i++) {
                SimulatorMultitude simu = new SimulatorMultitude(300, deltaTime, i);
                simu.simulate();
            }
        }

    }
}
