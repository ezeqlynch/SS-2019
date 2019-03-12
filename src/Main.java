import java.util.*;

public class Main {

    public static void main(String[] args) {

        Grid g = new Grid(100.0, 10);
        List<Particle> ps = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            ps.add(new Particle(0, Math.random() * 100, Math.random() * 100));
        }

        g.populate(ps);

        for (ArrayList<Particle>[] a : g.getGrid())
            System.out.println(Arrays.deepToString(a));

        g.calculateVecins(true);
        StringBuilder sbx = new StringBuilder();
        StringBuilder sby = new StringBuilder();
        sbx.append("x=[");
        sby.append("y=[");
        for(Particle p : ps) {
            sbx.append(p.getPosition().getX() + ",");
            sby.append(p.getPosition().getY() + ",");
        }
        System.out.println(ps.get(0).getPosition());
        System.out.println(ps.get(0).getVecins());
        StringBuilder sbxc = new StringBuilder();
        StringBuilder sbyc = new StringBuilder();
        sbxc.append("xc=[");
        sbyc.append("yc=[");

        for(Particle p: ps.get(0).getVecins()) {
            sbxc.append(p.getPosition().getX() + ",");
            sbyc.append(p.getPosition().getY() + ",");
        }
        sbx.append("]");
        sby.append("]");

        sbxc.append("]");
        sbyc.append("]");

        System.out.println(sbx.toString());
        System.out.println(sby.toString());
        System.out.println(sbxc.toString());
        System.out.println(sbyc.toString());

    }
}
