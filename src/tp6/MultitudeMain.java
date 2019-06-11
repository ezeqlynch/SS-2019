package tp6;

public class MultitudeMain {
    public static void main(String[] args) {
        double deltaTime = 0.01;
        int runsNum = 5;

        for (double velocity = 20; velocity < 21; velocity+=1) {
            MultitudeParticle.V_MAX = velocity;
            for (int i = 1; i <= runsNum; i++) {
                SimulatorMultitude simu = new SimulatorMultitude(300, deltaTime, i);
                simu.simulate();
            }
        }

    }
}
