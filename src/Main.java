import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SimulatorBrown sb = new SimulatorBrown(100);
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            sb.simulate(600, i);

        }
//        printOvito(sb.getSteps(), sb.getColls());
    }

    public static void print(List<BrownParticle> list) {
        NumberFormat formatter = new DecimalFormat("#0.000");
        StringBuilder sbx = new StringBuilder();
        StringBuilder sby = new StringBuilder();
        sbx.append("x=[");
        sby.append("y=[");
        for(BrownParticle p : list) {
            sbx.append(formatter.format(p.getX())).append(",");
            sby.append(formatter.format(p.getY())).append(",");
        }
        sbx.append("];");
        sby.append("];");
        System.out.println(sbx);
        System.out.println(sby);
    }

    public static void printStats ( List<List<LifeParticle>> list, String rules, int index, long time) {
//        StringBuilder sbp = new StringBuilder();
//        sbp.append(time).append('\n');
//        for(List<LifeParticle> l: list) {
//            if(l.stream().noneMatch(LifeParticle::isAlive)){
//                break;
//            }
//            int count = (int) l.stream().filter(LifeParticle::isAlive).count();
//            double max = l.stream().filter(LifeParticle::isAlive).mapToDouble(LifeParticle::getCenterDist).max().getAsDouble();
//            sbp.append(count).append(" ");
//            sbp.append(max).append(" ");
//            double avg = l.stream().filter(LifeParticle::isAlive).mapToDouble(LifeParticle::getCenterDist).average().getAsDouble();
//            sbp.append(avg).append(" ");
//            double stdev = Math.sqrt(l.stream().filter(LifeParticle::isAlive).mapToDouble(e -> Math.pow(e.getCenterDist() - avg, 2)).reduce(0, (a, b) -> a + b) / count);
//            sbp.append(stdev).append("\n");
//        }
//
//        File file = new File("./" + rules.split(",")[0] + rules.split(",")[1] + rules.split(",")[2]+ rules.split(",")[3] + "-stats-"+index+".stats");
//        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))) {
//            writer.append(sbp);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void printOvito (ArrayList<ArrayList<BrownParticle>> steps, ArrayList<Collision> colls, int index, int n, double totTime) {
        NumberFormat f = new DecimalFormat("#0.000000");
        StringBuilder sbp = new StringBuilder();
        sbp.append(steps.get(0).size()).append("\n\n");

        for(BrownParticle p : steps.get(0)) {
            sbp.append(p.getIndex()).append(' ').append(f.format(p.getX())).append(' ').append(f.format(p.getY())).append(' ').append(p.getRadius())
                    .append(' ').append(f.format(p.getVx())).append(' ').append(f.format(p.getVy())).append('\n');
        }
        sbp.append("\n").append(steps.size()).append('\n');
        int counter = 1;
        for (Collision coll : colls){
            sbp.append(coll.getTimeOf()).append('\n');
            BrownParticle bp = steps.get(counter).get(0);
            sbp.append(bp.getIndex()).append(" ").append(f.format(bp.getX())).append(' ').append(f.format(bp.getY())).append('\n');
            BrownParticle coll1 = steps.get(counter).get(coll.getIndex1());
            sbp.append(coll1.getIndex()).append(' ').append(f.format(coll1.getVx())).append(' ').append(f.format(coll1.getVy())).append('\n');
            if(coll.getIndex2() >= 0) {
                BrownParticle coll2 = steps.get(counter).get(coll.getIndex2());
                sbp.append(coll2.getIndex()).append(' ').append(f.format(coll2.getVx())).append(' ').append(f.format(coll2.getVy())).append('\n');
            } else {
                sbp.append(coll.getIndex2()).append('\n');
            }
            sbp.append('\n');
            counter++;
        }
        NumberFormat ftime = new DecimalFormat("#0.000");
        int frames = (int)totTime * 60;
        File file = new File(n + "-"+index+"-test-" +ftime.format((double)steps.size()/frames) + ".stat");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))) {
            writer.append(sbp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
