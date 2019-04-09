import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class LifeMain {

    public static void main(String[] args) {
        /***
         * N
         * L
         * 0 r x y
         * 1 r x y
         */
        int height = Integer.parseInt(args[0]);
        int width = Integer.parseInt(args[1]);
        int depth = Integer.parseInt(args[2]);
        String rules = args[3];
        int runs = Integer.parseInt(args[4]);
        int frames = Integer.parseInt(args[5]);
        boolean[] b = new boolean[]{
                true , true , true , true , true , true , false,
                true , false, false, false, false, false, true ,
                true , false, false, false, false, false, false,
                false, true , false, false, false, false, true ,
                false, false, false, true , true , false, false,
        };
//       ps.add(new LifeParticle(i, j, i*height + j, b[i*height + j]));
//            for (int i = 0; i < height; i++) {
//                for (int j = 0; j < width; j++) {
//                    ps.add(new LifeParticle(i, j, i*height + j, b[i*height + j]));
//                }
//            }


//        Simulator s = new Simulator(g3, rules);
        long grandtime1 = System.nanoTime();
        for (int index = 0; index < runs; index++) {
            List<LifeParticle> ps = new LinkedList<>();

            if(depth == 0) {
                for (int i = height * 3 / 8 ; i < 5 * height / 8; i++) {
                    for (int j = width * 3 / 8; j < 5 * width / 8; j++) {
                        if(Math.random() < 0.5){
                            ps.add(new LifeParticle(i, j, i*height + j, true));
                        }
                    }
                }
            } else {
                for (int i = height * 3 / 8; i < 5 * height / 8; i++) {
                    for (int j = width * 3 / 8; j < 5 * width / 8; j++) {
                        for (int k = depth * 3 / 8; k < 5 * width / 8; k++) {
                            if (Math.random() < 0.5) {
                                ps.add(new LifeParticle(i, j, k, i * height + j, true));
                            }
                        }
                    }
                }
            }

            Simulator s;
            if(depth == 0) {
                LifeGrid g2 = new LifeGrid2D(height, width);
                g2.populate(ps);
                s = new Simulator(g2, rules, rules);
            } else {
                LifeGrid g3 = new LifeGrid3D(height, width, depth);
                g3.populate(ps);
                s = new Simulator(g3, rules, rules);
            }

            long t2 = System.nanoTime();
            List<List<LifeParticle>> list = s.simulate(frames);
            long t1 = System.nanoTime();
            long time = t1 - t2;
            System.out.println(index + ": " +time);

            printStats(list, rules, index, time);
//            if(index == 0)
            printOvito(list, rules, index);
//            print(list);
        }
        long grandtime2 = System.nanoTime();
        System.out.println((grandtime2 - grandtime1) / 1000000);

    }

    public static void print(List<List<LifeParticle>> list) {

        for (List<LifeParticle> l : list) {
            StringBuilder sbx = new StringBuilder();
            sbx.append("x").append("=[");
            for(LifeParticle p : l) {
                if(p.isAlive()){
                    sbx.append(p.getCenterDist()).append(",");
                }
            }
            sbx.append("];");
            System.out.println(sbx);
        }
    }

    public static void printStats ( List<List<LifeParticle>> list, String rules, int index, long time) {
        StringBuilder sbp = new StringBuilder();
        sbp.append(time).append('\n');
        for(List<LifeParticle> l: list) {
            if(l.stream().noneMatch(LifeParticle::isAlive)){
                break;
            }
            int count = (int) l.stream().filter(LifeParticle::isAlive).count();
            double max = l.stream().filter(LifeParticle::isAlive).mapToDouble(LifeParticle::getCenterDist).max().getAsDouble();
            sbp.append(count).append(" ");
            sbp.append(max).append(" ");
            double avg = l.stream().filter(LifeParticle::isAlive).mapToDouble(LifeParticle::getCenterDist).average().getAsDouble();
            sbp.append(avg).append(" ");
            double stdev = Math.sqrt(l.stream().filter(LifeParticle::isAlive).mapToDouble(e -> Math.pow(e.getCenterDist() - avg, 2)).reduce(0, (a, b) -> a + b) / count);
            sbp.append(stdev).append("\n");
        }

        File file = new File("./" + rules.split(",")[0] + rules.split(",")[1] + rules.split(",")[2]+ rules.split(",")[3] + "-stats-"+index+".stats");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))) {
            writer.append(sbp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printOvito (List<List<LifeParticle>> list, String rules, int index) {
        StringBuilder sbp = new StringBuilder();
        for(List<LifeParticle> l : list) {
            if(l.stream().noneMatch(LifeParticle::isAlive)){
                break;
            }
            sbp.append(l.stream().filter(LifeParticle::isAlive).count()).append("\n\n");
            for(LifeParticle p: l) {
                if(p.isAlive()) {
                    sbp.append("1 ").append(p.getX()).append(' ').append(p.getY()).append(' ').append(p.getZ()).append('\n');
                }
            }
        }
//        System.out.println(sbp);
        File file = new File("./" + rules.split(",")[0] + rules.split(",")[1] + rules.split(",")[2]+ rules.split(",")[3] + "" + index + ".xyz");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))) {
            writer.append(sbp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
