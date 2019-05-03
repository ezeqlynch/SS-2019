package tp4;

import tp4.models.DHMFrame;
import tp4.models.IntegrationMethod;
import tp4.models.WeightedParticle;

import java.util.ArrayList;
import java.util.List;

public class DampedHarmonicMotion {

    public List<DHMFrame> frames;
    private IntegrationMethod method;

    //params de la teoria
    private double b = 100; //constante viscosa
    private double m = 70; //masa
    private double k = 10000; //constante resorte
    private double A = 1; //amplitud

    private double w;
    private double gamma;

    private double currentTime = 0;
    private double simulationTime = 5;

    //CI de la teoria
    private WeightedParticle current;
    private double deltaTime;

    //Algorithm variables
    private double prevInterVX;
    private double prevAX;
    private double[] particleXCoefficients = null;

    private final double[] GPC5_COEFFICIENTS = new double[]{ 3.0/16, 251.0/360, 1.0, 11.0/18, 1.0/6, 1.0/60 };

    public DampedHarmonicMotion(double deltaTime, IntegrationMethod method) {
        this.method = method;
        this.w = k/m;
        this.gamma = b/(2*m);
        this.frames = new ArrayList<>();
        this.deltaTime = deltaTime;
        this.current = new WeightedParticle(0,A,0, (-A)*b/(2*m),0,0,0,1, m, 0, 0);

        this.prevInterVX = current.getVx() - this.calculateXAcceleration(current) * (deltaTime/2);

        double prevX =  current.getX() - current.getVx() * deltaTime;
        double prevVX = current.getVx() - this.calculateXAcceleration(current) * deltaTime;
        prevAX = calculateXAcceleration(new WeightedParticle(-1,prevX,0,prevVX,0,0,0,1,m, 0, 0));


    }

    public void simulate(){
        while(currentTime < simulationTime){
            this.frames.add(new DHMFrame(currentTime,this.current));
            switch (method){
                case ANALYTIC:
                    analyticUpdate();
                    break;
                case VERLET_LEAP_FROG:
                    verletLeapFrogUpdate();
                    break;
                case BEEMAN:
                    beemanUpdate();
                    break;
                case GPC5:
                    gearPredictorCorrector5Update();
                    break;
            }
            currentTime += deltaTime;
        }
//        this.frames.forEach((frame)->{
//            System.out.println(frame.toString());
//        });

    }

    private void gearPredictorCorrector5Update(){
        if (particleXCoefficients == null)
            particleXCoefficients = initializeGPCO5Axis(current.getMass(), current.getX(), current.getVx());

        // predict
        double[] predictedXCoefficients = predictCoefficientsWithGPC5(particleXCoefficients);

        // evaluate deltaR2X
        double AX = (-1*k*predictedXCoefficients[0] - b*predictedXCoefficients[1])/m;
        double deltaR2X = ((AX - predictedXCoefficients[2]) * Math.pow(deltaTime, 2)) / 2;

        // correct coefficients
        particleXCoefficients = correctCoefficientsWithGPC5(deltaR2X, predictedXCoefficients);

        current = new WeightedParticle(current.getIndex(),
                particleXCoefficients[0], 0,
                particleXCoefficients[1], 0,
                particleXCoefficients[2], 0,
                current.getRadius(), current.getMass(), 0, 0);
    }

    private double[] predictCoefficientsWithGPC5 (double[] r) {
        double t1 = deltaTime;
        double t2 = Math.pow(deltaTime, 2) / 2;
        double t3 = Math.pow(deltaTime, 3) / 6;
        double t4 = Math.pow(deltaTime, 4) / 24;
        double t5 = Math.pow(deltaTime, 5) / 120;

        return new double[] {
                r[0] + r[1] * t1 + r[2] * t2 + r[3] * t3 + r[4] * t4 + r[5] * t5,  // position
                r[1] + r[2] * t1 + r[3] * t2 + r[4] * t3 + r[5] * t4,            // speed
                r[2] + r[3] * t1 + r[4] * t2 + r[5] * t4,                      // acceleration
                r[3] + r[4] * t1 + r[5] * t2,                                // r3
                r[4] + r[5] * t1,                                          // r4
                r[5],                                                    // r5
        };
    }

    private double[] correctCoefficientsWithGPC5 (double deltaR2, double[] predictedCoefficients) {
        return new double[]{
                predictedCoefficients[0] + GPC5_COEFFICIENTS[0] * deltaR2,
                predictedCoefficients[1] + GPC5_COEFFICIENTS[1] * deltaR2 * 1 / Math.pow(deltaTime, 1),
                predictedCoefficients[2] + GPC5_COEFFICIENTS[2] * deltaR2 * 2 / Math.pow(deltaTime, 2),
                predictedCoefficients[3] + GPC5_COEFFICIENTS[3] * deltaR2 * 6 / Math.pow(deltaTime, 3),
                predictedCoefficients[4] + GPC5_COEFFICIENTS[4] * deltaR2 * 24 / Math.pow(deltaTime, 4),
                predictedCoefficients[5] + GPC5_COEFFICIENTS[5] * deltaR2 * 120 / Math.pow(deltaTime, 5),
        };
    }

    private double[] initializeGPCO5Axis(double mass, double r0, double r1) {
        double r2 = (-k / m) * r0 - (b / m) * r1;
        double r3 = (-k / m) * r1 - (b / m) * r2;
        double r4 = (-k / m) * r2 - (b / m) * r3;
        double r5 = (-k / m) * r3 - (b / m) * r4;
        return new double[] { r0, r1, r2, r3, r4, r5 };
    }

    private void verletLeapFrogUpdate() {
        double interVX = prevInterVX + ((deltaTime/current.getMass())*calculateXForce(current));
        double newPX = current.getX() + deltaTime*interVX;
        double newVX = (prevInterVX + interVX)/2;
        current = new WeightedParticle( current.getIndex(),
                newPX, 0, newVX, 0, 0, 0,
                current.getRadius(), current.getMass(), 0, 0);
        prevInterVX = interVX;
    }

    private void beemanUpdate() {
        double newAX = calculateXAcceleration(current);
        double tempVX = current.getVx();

        // Calculate new position and predicted velocity
        double newPX = current.getX()
                + current.getVx()*deltaTime
                + ((2.0/3.0) * newAX  - prevAX/6.0) * Math.pow(deltaTime, 2);
        double predictedVX = current.getVx()
                + (3.0/2.0) * newAX * deltaTime
                - (1.0/2.0) * prevAX * deltaTime;

        // Update armonicParticle with new position and predicted velocity
        current = new WeightedParticle( current.getIndex(), newPX, 0, predictedVX, 0, newAX,0, current.getRadius(), current.getMass(), 0, 0);

        // Calculate t+DT acceleration
        double fAx = calculateXAcceleration(current);
        double newVX = tempVX
                + (1.0/3.0) * fAx * deltaTime
                + (5.0/6.0) * newAX * deltaTime
                - (1.0/6.0) * prevAX * deltaTime;
        // Update particle with aproximated speed
        current.setVx(newVX);

        prevAX = newAX;
    }

    private void analyticUpdate() {
        double expTerm = Math.exp((-gamma) * (currentTime+deltaTime));
        double cosineTerm = Math.cos(Math.sqrt(w - gamma*gamma) * (currentTime+deltaTime));
        double posX = A * expTerm * cosineTerm;

        double sineTerm = Math.sin(Math.sqrt(w - gamma*gamma) * (currentTime+deltaTime));
        double velX = (-1) * gamma * A * expTerm * cosineTerm + Math.sqrt(w - gamma*gamma) * A * expTerm * -sineTerm;

        double aX = (-1)*w*posX;

        current = new WeightedParticle( current.getIndex(), posX, 0, velX, 0,aX,0, current.getRadius(),current.getMass(),0, 0);
    }

    private double calculateXForce(WeightedParticle p) {
        return (-1) * k * p.getX() - b * p.getVx();
    }

    private double calculateXAcceleration(WeightedParticle p) {
        return this.calculateXForce(p) / p.getMass();
    }
}
