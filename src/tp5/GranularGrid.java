package tp5;

import tp4.models.WeightedParticle;

import java.util.ArrayList;
import java.util.List;

public class GranularGrid {

    private ArrayList<GranularParticle>[][][] grid;
    private double Rc;
    private double L;
    private double W;
    private double H;
    private int ML;
    private int MW;
    private int MH;

    public GranularGrid(double L, double W, double H) {
        this.L = L; //0.4 era
        this.W = W; //0.4 era
        this.H = H; //1.5 era
        this.Rc = 0;
        this.ML = 20;
        this.MW = 20;
        this.MH = 50;
        this.grid = new ArrayList[ML][MW][MH];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                for (int k = 0; k < grid[0][0].length; k++) {
                    grid[i][j][k] = new ArrayList<>();
                }
            }
        }
    }

    public void populate(List<GranularParticle> particles) {
        this.grid = new ArrayList[ML][MW][MH];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                for (int k = 0; k < grid[0][0].length; k++) {
                    grid[i][j][k] = new ArrayList<>();
                }
            }
        }
        particles.parallelStream().forEach(p -> grid[p.getCellRow(L, ML)][p.getCellCol(W, MW)][p.getCellHeight(H, MH)].add(p));
    }


    public void calculateVecins() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                for (int k = 0; k < grid[0][0].length; k++) {
                    int il = i;
                    int jl = j;
                    int kl = k;
                    grid[i][j][k].forEach(p -> {
                        if (p != null) {
                            p.addSelfVecins(grid[il][jl][kl], Rc);
                            if (il - 1 >= 0)
                                p.addVecins(grid[il - 1][jl][kl], Rc); //up (1 cube)
                            if (il - 1 >= 0 && jl + 1 < grid[0].length)
                                p.addVecins(grid[il - 1][jl + 1][kl], Rc); //upright (1 cube)
                            if (jl + 1 < grid[0].length)
                                p.addVecins(grid[il][jl + 1][kl], Rc); //right (1 cube)
                            if (il + 1 < grid.length && jl + 1 < grid[0].length)
                                p.addVecins(grid[il + 1][jl + 1][kl], Rc); //downright (1 cube)
                            if(kl + 1 < grid.length)
                                p.addVecins(grid[il][jl][kl+1], Rc); //higher face (9 cubes)
                        }
                    });
                }
            }
        }
    }

    public ArrayList<GranularParticle> [][][] getGrid() {
        return grid;
    }
}
