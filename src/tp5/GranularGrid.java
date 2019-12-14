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
        this.L = L; //1.5 era
        this.W = W; //0.4 era
        this.H = H; //0.4 era
        this.Rc = 0;
        this.ML = 9;
        this.MW = 9;
        this.MH = 30;
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
        particles.stream().filter(p-> p.getY() < L).forEach(p -> grid[p.getCellRow(L, ML)][p.getCellCol(W, MW)][p.getCellHeight(H, MH)].add(p));
    }


    public void calculateVecins() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                for (int k = 0; k < grid[0][0].length; k++) {
                    for (GranularParticle p : grid[i][j][k]) {
                        if (p != null) {
                            p.addSelfVecins(grid[i][j][k], Rc);
                            if (i - 1 >= 0)
                                p.addVecins(grid[i - 1][j][k], Rc); //up (1 cube)
                            if (i - 1 >= 0 && j + 1 < grid[0].length)
                                p.addVecins(grid[i - 1][j + 1][k], Rc); //upright (1 cube)
                            if (j + 1 < grid[0].length)
                                p.addVecins(grid[i][j + 1][k], Rc); //right (1 cube)
                            if (i + 1 < grid.length && j + 1 < grid[0].length)
                                p.addVecins(grid[i + 1][j + 1][k], Rc); //downright (1 cube)
                            if(k + 1 < grid.length)
                                p.addVecins(grid[i][j][k+1], Rc); //higher face (9 cubes)
                        }
                    }
                }
            }
        }
    }

    public ArrayList<GranularParticle> [][][] getGrid() {
        return grid;
    }
}
