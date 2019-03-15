import java.io.*;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        /***
         * N
         * L
         * 0 r x y
         * 1 r x y
         */
        int particles = 10000;
        double size = 20;
        int cellSize = 1;
        double rc = 1;
        boolean edges = false;
        double radius = 0.25;
        List<Particle> ps = new LinkedList<>();
        if(args.length > 1) {
            particles = Integer.parseInt(args[0]);
            size = Double.parseDouble(args[1]);
            cellSize = Integer.parseInt(args[2]);
            rc = Double.parseDouble(args[3]);
            edges = Boolean.parseBoolean(args[4]);
            radius = Double.parseDouble(args[5]);
            for (int i = 0; i < particles ; i++) {
                ps.add(new Particle(radius, Math.random() * size, Math.random() * size, i));
            }
        } else if (args.length == 1){

            try (Stream<String> stream = Files.lines(Paths.get(args[0]));
                    BufferedReader br = new BufferedReader(new FileReader(args[0]))) {

                particles = Integer.parseInt(br.readLine());
                size = Double.parseDouble(br.readLine());
                cellSize = Integer.parseInt(br.readLine());
                rc = Double.parseDouble(br.readLine());
                edges = Boolean.parseBoolean(br.readLine());
                Stream<String> particlesStream = stream.skip(5);
                ps = particlesStream.map(e -> {
                    String[] split = e.split(" ");
                    return new Particle(Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Integer.parseInt(split[0]));
                }).collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            for (int i = 0; i < particles ; i++) {
                ps.add(new Particle(radius, Math.random() * size, Math.random() * size, i));
            }
        }


        Grid g = new Grid(size, cellSize, rc);

        g.populate(ps);

//        for (ArrayList<Particle>[] a : g.getGrid())
//            System.out.println(Arrays.deepToString(a));
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        StringBuilder sbx2 = new StringBuilder();
        sbx2.append(particles).append('\n').append(size).append('\n').append(cellSize).append('\n').append(rc).append('\n')
            .append(edges).append('\n');
        for(Particle p : ps) {
            sbx2.append(p.getIndex()).append(' ').append(decimalFormat.format(p.getRadius())).append(' ')
                    .append(decimalFormat.format(p.getPosition().getX())).append(" ")
                    .append(decimalFormat.format(p.getPosition().getY())).append('\n');
        }
        File file = new File("./output.txt");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))) {
            writer.append(sbx2);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(sbx2);

        long t2 = System.nanoTime();
        g.calculateVecins(edges);
        long t1 = System.nanoTime();
        System.out.println(t1 - t2);

        StringBuilder sbx = new StringBuilder();
        StringBuilder sby = new StringBuilder();
        sbx.append("x=[");
        sby.append("y=[");
        for(Particle p : ps) {
            sbx.append(decimalFormat.format(p.getPosition().getX())).append(",");
            sby.append(decimalFormat.format(p.getPosition().getY())).append(",");
        }
//        System.out.println(ps.get(0).getPosition());
//        System.out.println(ps.get(0).getVecins());
        StringBuilder sbxc = new StringBuilder();
        StringBuilder sbyc = new StringBuilder();
        StringBuilder vecins = new StringBuilder();
        sbxc.append("xc=[");
        sbyc.append("yc=[");
        for(Particle p0: ps) {
            vecins.append(p0.getIndex()).append(' ');
            for(Particle p: p0.getVecins()) {
                vecins.append(p.getIndex()).append(' ');
            }
            vecins.append('\n');
        }


        for(Particle p: ps.get(0).getVecins()) {
            sbxc.append(decimalFormat.format(p.getPosition().getX())).append(",");
            sbyc.append(decimalFormat.format(p.getPosition().getY())).append(",");
        }
        File vecinInput = new File("./vecins.txt");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(vecinInput, false))) {
            writer.append(Long.toString(t1 - t2)).append('\n');
            writer.append(vecins);
        } catch (IOException e) {
            e.printStackTrace();
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
        System.out.println("circx="+decimalFormat.format(rc + 2*radius) + ".*sin(t) + " + decimalFormat.format(ps.get(0).getPosition().getX()) + ";");
        System.out.println("circy="+decimalFormat.format(rc + 2*radius) + ".*cos(t) + " + decimalFormat.format(ps.get(0).getPosition().getY()) + ";");
        System.out.println("plot(x,y,\"o\",xc,yc,\"ro\", circx, circy, \"k\", x0, y0, \"og\")");
    }
}
