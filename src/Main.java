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
        int height = 100;
        int width = 100;
        int depth = 100;
        String rules = "8,15,12,15";

        List<LifeParticle> ps = new LinkedList<>();
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

        for (int i = 25; i < 75; i++) {
            for (int j = 25; j < 75; j++) {
                for (int k = 25; k < 75; k++) {
                    if(Math.random() < 0.5){
                        ps.add(new LifeParticle(i, j, k, i*height + j, true));
                    }
                }
            }
        }

        LifeGrid g3 = new LifeGrid3D(height, width, depth);
//        LifeGrid g2 = new LifeGrid2D(height, width);

        g3.populate(ps);

        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);



        Simulator s = new Simulator(g3, rules);

        long t2 = System.nanoTime();
        List<List<LifeParticle>> list = s.simulate(100);
        long t1 = System.nanoTime();
        System.out.println(t1 - t2);

        printOvito(list, rules);

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

    public static void printOvito (List<List<LifeParticle>> list, String rules) {
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
        File file = new File("./"+rules+".xyz");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))) {
            writer.append(sbp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
