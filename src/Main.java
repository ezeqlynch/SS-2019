import java.io.*;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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
        int height = 1000;
        int width = 1000;
        Random r = new Random();
//        int particles = 10000;
//        double size = 20;
//        int cellSize = 1;
//        double rc = 1;
//        boolean edges = false;
//        double radius = 0.25;
        List<LifeParticle> ps = new LinkedList<>();
//        if(args.length > 1) {
//            particles = Integer.parseInt(args[0]);
//            size = Double.parseDouble(args[1]);
//            cellSize = Integer.parseInt(args[2]);
//            rc = Double.parseDouble(args[3]);
//            edges = Boolean.parseBoolean(args[4]);
//            radius = Double.parseDouble(args[5]);
         boolean[] b = new boolean[]{
                 true , true , true , true , true , true , false,
                 true , false, false, false, false, false, true ,
                 true , false, false, false, false, false, false,
                 false, true , false, false, false, false, true ,
                 false, false, false, true , true , false, false,
         };
//            for (int i = 0; i < height; i++) {
//                for (int j = 0; j < width; j++) {
//                    ps.add(new LifeParticle(i, j, i*height + j, b[i*height + j]));
//                }
//            }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                ps.add(new LifeParticle(i, j, i*height + j, r.nextBoolean()));
            }
        }

        for (int i = 50; i < 50 + 5; i++) {
            for (int j = 50; j < 50 + 7; j++) {
                ps.get(i*height + j).setAlive(b[(i-50)*7 + (j-50)]);
//                System.out.println((i-50)*5 + (j-50));
//                System.out.println(ps.get(i*height + j).getX() + " " +ps.get(i*height + j).getY());
            }
        }

//        } else if (args.length == 1){
//
//            try (Stream<String> stream = Files.lines(Paths.get(args[0]));
//                    BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
//
//                particles = Integer.parseInt(br.readLine());
//                size = Double.parseDouble(br.readLine());
//                cellSize = Integer.parseInt(br.readLine());
//                rc = Double.parseDouble(br.readLine());
//                edges = Boolean.parseBoolean(br.readLine());
//                Stream<String> particlesStream = stream.skip(5);
//                ps = particlesStream.map(e -> {
//                    String[] split = e.split(" ");
//                    return new Particle(Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Integer.parseInt(split[0]));
//                }).collect(Collectors.toList());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            for (int i = 0; i < particles ; i++) {
//                ps.add(new Particle(radius, Math.random() * size, Math.random() * size, i));
//            }
//        }

        LifeGrid2D g = new LifeGrid2D(height, width);
//        Grid g = new Grid(size, cellSize, rc);

        g.populate(ps);

//        for (ArrayList<Particle>[] a : g.getGrid())
//            System.out.println(Arrays.deepToString(a));
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
//        StringBuilder sbx2 = new StringBuilder();
//        sbx2.append(particles).append('\n').append(size).append('\n').append(cellSize).append('\n').append(rc).append('\n')
//            .append(edges).append('\n');
//        for(Particle p : ps) {
//            sbx2.append(p.getIndex()).append(' ').append(decimalFormat.format(p.getRadius())).append(' ')
//                    .append(decimalFormat.format(p.getPosition().getX())).append(" ")
//                    .append(decimalFormat.format(p.getPosition().getY())).append('\n');
//        }
//        File file = new File("./output.txt");
//        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))) {
//            writer.append(sbx2);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(sbx2);
//
//        g.calculateVecins();


        Simulator s = new Simulator(g);

        long t2 = System.nanoTime();
        List<List<LifeParticle>> list = s.simulate(100);
        long t1 = System.nanoTime();
        System.out.println(t1 - t2);

        printOvito(list);
//        print(list);


//        StringBuilder sbx = new StringBuilder();
//        StringBuilder sby = new StringBuilder();
//        sbx.append("x=[");
//        sby.append("y=[");
//        for(LifeParticle p : ps) {
//            if(p.isAlive()){
//                sbx.append(decimalFormat.format(p.getX())).append(",");
//                sby.append(decimalFormat.format(p.getY())).append(",");
//            }
//        }
////        System.out.println(ps.get(0).getPosition());
////        System.out.println(ps.get(0).getVecins());
//        StringBuilder sbxc = new StringBuilder();
//        StringBuilder sbyc = new StringBuilder();
//        StringBuilder vecins = new StringBuilder();
//        sbxc.append("xc=[");
//        sbyc.append("yc=[");
//        for(LifeParticle p0: ps) {
//            vecins.append(p0.getIndex()).append(' ');
//            for(LifeParticle p: p0.getVecins()) {
//                vecins.append(p.getIndex()).append(' ');
//            }
//            vecins.append('\n');
//        }
//
//
//        for(LifeParticle p: ps.get(4560).getVecins().stream().filter(LifeParticle::isAlive).collect(Collectors.toList())) {
//            sbxc.append(decimalFormat.format(p.getX())).append(",");
//            sbyc.append(decimalFormat.format(p.getY())).append(",");
//        }
////        File vecinInput = new File("./vecins.txt");
////        try(BufferedWriter writer = new BufferedWriter(new FileWriter(vecinInput, false))) {
////            writer.append(Long.toString(t1 - t2)).append('\n');
////            writer.append(vecins);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//        sbx.append("];");
//        sby.append("];");
//
//        sbxc.append("];");
//        sbyc.append("];");
////        ps.get(0).checkDistnces();
////        System.out.println("t = linspace(0,2*pi,100)'; ");
//        System.out.println("x0=" + decimalFormat.format(ps.get(4560).getX()));
//        System.out.println("y0=" + decimalFormat.format(ps.get(4560).getY()));
//        System.out.println(sbx.toString());
//        System.out.println(sby.toString());
//        System.out.println(sbxc.toString());
//        System.out.println(sbyc.toString());
////        System.out.println("circx="+decimalFormat.format(rc + 2*radius) + ".*sin(t) + " + decimalFormat.format(ps.get(0).getPosition().getX()) + ";");
////        System.out.println("circy="+decimalFormat.format(rc + 2*radius) + ".*cos(t) + " + decimalFormat.format(ps.get(0).getPosition().getY()) + ";");
//        System.out.println("plot(x,y,\".\",xc,yc,\"r.\", x0, y0, \".g\")");
    }

    public static void print(List<List<LifeParticle>> list) {

        int i = 0;
        for (List<LifeParticle> l : list) {
            StringBuilder sbx = new StringBuilder();
            StringBuilder sby = new StringBuilder();
            sbx.append("x").append(i).append("=[");
            sby.append("y").append(i).append("=[");
            for(LifeParticle p : l) {
                if(p.isAlive()){
                    sbx.append(p.getX()).append(",");
                    sby.append(p.getY()).append(",");
                }
            }
            sbx.append("];");
            sby.append("];");
            System.out.println(sbx);
            System.out.println(sby);
            i++;
        }
        for (int j = 0; j < i; j++) {
            System.out.println("plot(x"+j+",y"+j+",\"s\",\"markerfacecolor\",\"blue\")");
        }
    }

    public static void printOvito (List<List<LifeParticle>> list) {
        StringBuilder sbp = new StringBuilder();
        for(List<LifeParticle> l : list) {
            sbp.append(l.stream().filter(LifeParticle::isAlive).count()).append("\n\n");
            for(LifeParticle p: l) {
                if(p.isAlive()) {
                    sbp.append("1 ").append(p.getX()).append(' ').append(p.getY()).append('\n');
                }
            }
        }
//        System.out.println(sbp);
        File file = new File("./output.xyz");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))) {
            writer.append(sbp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
