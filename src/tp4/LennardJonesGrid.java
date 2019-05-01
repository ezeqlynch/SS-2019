package tp4;

import tp4.models.WeightedParticle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LennardJonesGrid {

    private ArrayList<WeightedParticle>[][] grid;
    private double Rc;
    private double L;
    private int M;

    public LennardJonesGrid(double L, double Rc) {
        this.L = L;
        this.Rc = Rc;
        this.M = 80;
        this.grid = new ArrayList[M/2][M];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }
    }

    public void populate(List<WeightedParticle> particles) {
        this.grid = new ArrayList[M/2][M];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }

        particles.forEach(p -> grid[p.getCellRow(L, M / 2)][p.getCellCol(L * 2, M)].add(p));
    }


    public void calculateVecins() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                for (WeightedParticle p : grid[i][j]) {
                    if (p != null) {
                        p.addSelfVecins(grid[i][j], Rc);
                        if (j == 39) {
                            if (i - 1 >= 0)
                                p.addVecins(grid[i - 1][j], Rc); //up
                            if (i >= 18 && i <= 21) {
                                if (i == 18) {
                                    p.addVecins(grid[i + 1][j + 1], Rc); //downright
                                } else if (i == 21) {
                                    p.addVecins(grid[i - 1][j + 1], Rc); //upright
                                } else {
                                    p.addVecins(grid[i - 1][j + 1], Rc); //upright
                                    p.addVecins(grid[i][j + 1], Rc); //right
                                    p.addVecins(grid[i + 1][j + 1], Rc); //downright
                                }
                            }
                        } else {
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
    }

    public ArrayList<WeightedParticle> [][] getGrid() {
        return grid;
    }
}
