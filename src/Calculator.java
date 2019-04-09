import java.util.List;

public class Calculator {

    public static boolean isOverlapping(double x, double y, List<BrownParticle> b) {
        // <= pq tengo q negar la segunda parte para ver si overlapean
        // solo chequeo cuando creo de las chicas -> el radio es 0.005
        return b.stream().anyMatch(p -> Math.pow(x - p.getX(), 2) + Math.pow(y - p.getY(), 2) <= Math.pow(0.005 + p.getRadius(), 2));
    }

    public static double timeToCollision(BrownParticle a, BrownParticle b) {
        double[] deltar = {a.getX() - b.getX(), a.getY() - b.getY()};
        double[] deltav = {a.getVx() - b.getVx(), a.getVy() - b.getVy()};
        double deltavr = deltav[0] * deltar[0] + deltav[1] * deltar[1];
        if(deltavr >= 0) {
            return 1000; //asumimos q en 1000 segundos va a haber alguna colision
        }
        double deltarsq = Math.pow(deltar[0], 2) + Math.pow(deltar[1], 2);
        double deltavsq = Math.pow(deltav[0], 2) + Math.pow(deltav[1], 2);
        double sigma = a.getRadius() + b.getRadius();
        double d = deltavr * deltavr - deltavsq * (deltarsq - sigma * sigma);
        if(d < 0) {
            return 1000;
        }
        return -(deltavr + Math.sqrt(d))/deltavsq;
    }

    public static double calculateJ(BrownParticle a, BrownParticle b) {
        double[] deltav = {a.getVx() - b.getVx(), a.getVy() - b.getVy()};
        double[] deltar = {a.getX() - b.getX(), a.getY() - b.getY()};
        double deltavr = deltav[0] * deltar[0] + deltav[1] * deltar[1];
        double sigma = a.getRadius() + b.getRadius();
        double massSum = a.getMass() + b.getMass();
        return (2 * a.getMass() * b.getMass() * deltavr) / (sigma * massSum);
    }
}
