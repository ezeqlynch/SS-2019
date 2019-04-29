package tp4.models;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DHMFrame {

    public double timestamp;
    public WeightedParticle particle;

    private static NumberFormat formatter = new DecimalFormat("#0.00000");


    public DHMFrame(double timestamp, WeightedParticle particle) {
        this.timestamp = timestamp;
        this.particle = particle;
    }

    public String getX() {
        return formatter.format(particle.getX());
    }

    @Override
    public String toString() {
        return formatter.format(timestamp) +"s: " + particle.toStringDHM();
    }
}
