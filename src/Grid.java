    import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
    import java.util.LinkedList;
    import java.util.List;

public class Grid {
    private double L;
    private int M;
    private ArrayList<Particle>[][] grid;

    public Grid (double L, int M) {
        this.L = L;
        this.M = M;
        this.grid = new ArrayList[M][M];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }
    }

    public void populate(List<Particle> particles) {
        for (Particle p : particles) {
            grid[p.getPosition().getCellRow(L, M)][p.getPosition().getCellCol(L,M)].add(p);
        }
    }

    public ArrayList<Particle>[][] getGrid() {
        return grid;
    }

    public void calculateVecins(boolean vuelt) {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                for (Particle p : grid[i][j]) {
                    p.addSelfVecins(grid[i][j]);
                    if(vuelt){
                        p.addVecins(grid[Math.floorMod(i-1, M)][j]); //up
                        p.addVecins(grid[Math.floorMod(i-1, M)][Math.floorMod(j+1, M)]); //upright
                        p.addVecins(grid[i][Math.floorMod(j+1, M)]); //right
                        p.addVecins(grid[Math.floorMod(i+1, M)][Math.floorMod(j+1, M)]); //downright
                    }else{
                        if(i-1 >= 0)
                            p.addVecins(grid[i-1][j]); //up
                        if(i-1 >= 0 && j+1 < M)
                            p.addVecins(grid[i-1][j+1]); //upright
                        if(j+1 < M)
                            p.addVecins(grid[i][j+1]); //right
                        if(i+1 < M && j+1 < M) {
                            p.addVecins(grid[i+1][j+1]); //downright
                        }
//                    for(Particle v: p.getVecins()) {
//                        v.addVecin(p);
//                    }
                    }

                }
            }
        }
    }

}
