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
        SimulatorBrown sb = new SimulatorBrown(10);
        sb.simulate(600);
        printOvito(sb.getSteps());
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

    public static void printOvito (ArrayList<ArrayList<BrownParticle>> steps) {
        NumberFormat formatter = new DecimalFormat("#0.000");
        StringBuilder sbp = new StringBuilder();
        for (ArrayList<BrownParticle> list : steps){
            sbp.append(list.size()).append("\n\n");

            for(BrownParticle p : list) {
                sbp.append(p.getIndex()).append(' ').append(formatter.format(p.getX())).append(' ').append(formatter.format(p.getY())).append(' ').append(p.getRadius())
                        .append(' ').append(formatter.format(p.getVx())).append(' ').append(formatter.format(p.getVy())).append('\n');
            }
//        System.out.println(sbp);
        }
        File file = new File("./test.xyz");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))) {
            writer.append(sbp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
