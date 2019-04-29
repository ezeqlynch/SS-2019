package tp4;

import tp4.models.WeightedParticle;

import java.util.ArrayList;
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
        this.grid = new ArrayList[M][M];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }
    }

    public void populate(List<WeightedParticle> particles) {
        for (WeightedParticle p : particles) {
            grid[p.getCellRow(L, M)][p.getCellCol(L,M)].add(p);

        }
    }


    public void calculateVecins() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                for (WeightedParticle p : grid[i][j]) {
                    p.addSelfVecins(grid[i][j], Rc);
                    if(j == 39) {
                        if(i-1 >= 0)
                            p.addVecins(grid[i-1][j], Rc); //up
                        if (i >= 38 && i <= 41) {
                            if (i == 38) {
                                p.addVecins(grid[i+1][j+1], Rc); //downright
                            } else if (i == 41) {
                                p.addVecins(grid[i-1][j+1], Rc); //upright
                            } else {
                                p.addVecins(grid[i-1][j+1], Rc); //upright
                                p.addVecins(grid[i][j+1], Rc); //right
                                p.addVecins(grid[i+1][j+1], Rc); //downright
                            }
                        }
                    } else {
                        if(i-1 >= 0)
                            p.addVecins(grid[i-1][j], Rc); //up
                        if(i-1 >= 0 && j+1 < M)
                            p.addVecins(grid[i-1][j+1], Rc); //upright
                        if(j+1 < M)
                            p.addVecins(grid[i][j+1], Rc); //right
                        if(i+1 < M && j+1 < M)
                            p.addVecins(grid[i+1][j+1], Rc); //downright
                    }
                }
            }
        }
    }
}
