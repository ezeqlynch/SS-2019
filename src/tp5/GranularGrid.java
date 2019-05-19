package tp5;

import tp4.models.WeightedParticle;

import java.util.ArrayList;
import java.util.List;

public class GranularGrid {

    private ArrayList<GranularParticle>[][] grid;
    private double Rc;
    private double L;
    private double W;
    private int ML;
    private int MW;

    public GranularGrid(double L, double W) {
        this.L = L;
        this.W = W;
        this.Rc = 0;
        this.ML = 30;
        this.MW = 9;
        this.grid = new ArrayList[ML][MW];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }
    }

    public void populate(List<GranularParticle> particles) {
        this.grid = new ArrayList[ML][MW];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }
        particles.stream().filter(p-> p.getY() < L).forEach(p -> grid[p.getCellRow(L, ML)][p.getCellCol(W, MW)].add(p));
    }


    public void calculateVecins() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                for (GranularParticle p : grid[i][j]) {
                    if (p != null) {
                        p.addSelfVecins(grid[i][j], Rc);
                        if (i - 1 >= 0)
                            p.addVecins(grid[i - 1][j], Rc); //up
                        if (i - 1 >= 0 && j + 1 < grid[0].length)
                            p.addVecins(grid[i - 1][j + 1], Rc); //upright
                        if (j + 1 < grid[0].length)
                            p.addVecins(grid[i][j + 1], Rc); //right
                        if (i + 1 < grid.length && j + 1 < grid[0].length)
                            p.addVecins(grid[i + 1][j + 1], Rc); //downright
                    }
                }
            }
        }
    }

    public ArrayList<GranularParticle> [][] getGrid() {
        return grid;
    }
}
