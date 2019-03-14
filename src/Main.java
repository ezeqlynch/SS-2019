import java.text.DecimalFormat;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        /***
         * N
         * L
         * 0 r x y
         * 1 r x y
         */
        Grid g = new Grid(100.0, 10, 10);
        List<Particle> ps = new LinkedList<>();
//        ps.add(new Particle(0, 98, 98));
        for (int i = 0; i < 2000; i++) {
            ps.add(new Particle(0, Math.random() * 100, Math.random() * 100, i));
        }

        g.populate(ps);

//        for (ArrayList<Particle>[] a : g.getGrid())
//            System.out.println(Arrays.deepToString(a));

        g.calculateVecins(true);
        StringBuilder sbx = new StringBuilder();
        StringBuilder sby = new StringBuilder();
        sbx.append("x=[");
        sby.append("y=[");
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        for(Particle p : ps) {
            sbx.append(decimalFormat.format(p.getPosition().getX())).append(",");
            sby.append(decimalFormat.format(p.getPosition().getY())).append(",");
        }
//        System.out.println(ps.get(0).getPosition());
//        System.out.println(ps.get(0).getVecins());
        StringBuilder sbxc = new StringBuilder();
        StringBuilder sbyc = new StringBuilder();
        sbxc.append("xc=[");
        sbyc.append("yc=[");

        for(Particle p: ps.get(0).getVecins()) {
            sbxc.append(decimalFormat.format(p.getPosition().getX())).append(",");
            sbyc.append(decimalFormat.format(p.getPosition().getY())).append(",");
        }
        sbx.append("];");
        sby.append("];");

        sbxc.append("];");
        sbyc.append("];");

//        ps.get(0).checkDistnces();
        System.out.println("t = linspace(0,2*pi,100)'; ");
        System.out.println("x0=" + decimalFormat.format(ps.get(0).getPosition().getX()));
        System.out.println("y0=" + decimalFormat.format(ps.get(0).getPosition().getY()));
        System.out.println(sbx.toString());
        System.out.println(sby.toString());
        System.out.println(sbxc.toString());
        System.out.println(sbyc.toString());
        System.out.println("circx=10.*sin(t) + " + decimalFormat.format(ps.get(0).getPosition().getX()) + ";");
        System.out.println("circy=10.*cos(t) + " + decimalFormat.format(ps.get(0).getPosition().getY()) + ";");
        System.out.println("plot(x,y,\"o\",xc,yc,\"ro\", circx, circy, \"k\", x0, y0, \"og\")");
    }
}
