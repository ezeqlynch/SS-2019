package tp6;

import tp4.models.WeightedParticle;

import java.util.ArrayList;
import java.util.List;

public class MultitudeGrid {

    private ArrayList<MultitudeParticle>[][] grid;
    private double Rc;
    private double L;
    private int M;

    public MultitudeGrid(double L) {
        this.L = L;
        this.Rc = 0;
        this.M = 80;
        this.grid = new ArrayList[M][M];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }
    }

    public void populate(List<MultitudeParticle> particles) {
        this.grid = new ArrayList[M][M];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }
        particles.stream().filter(p-> p.getY() < L).forEach(p -> grid[p.getCellRow(L, M)][p.getCellCol(L, M)].add(p));
    }


    public void calculateVecins() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                for (MultitudeParticle p : grid[i][j]) {
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

    public ArrayList<MultitudeParticle> [][] getGrid() {
        return grid;
    }
}
