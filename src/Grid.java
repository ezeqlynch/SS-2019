import java.util.ArrayList;
import java.util.List;

public class Grid {
    private double L;
    private int M;
    private ArrayList<Particle>[][] grid;
    private double Rc;

    public Grid (double L, int M, double Rc) {
        this.L = L;
        this.M = M;
        this.Rc = Rc;
        this.grid = new ArrayList[M][M];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }
    }

    public void populate(List<Particle> particles) {
        for (Particle p : particles) {
//            System.out.println(p.getPosition().getCellRow(L, M));
//            System.out.println(p.getPosition().getCellCol(L,M));
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
                    p.addSelfVecins(grid[i][j], Rc);
                    if(vuelt) {
                        if(i-1 < 0 && j+1 >= M){
                            p.addVecins(grid[Math.floorMod(i-1, M)][Math.floorMod(j+1, M)], Rc, 1, L); //upright
                            p.addVecins(grid[Math.floorMod(i-1, M)][j], Rc, 2, L); //up
                            p.addVecins(grid[i][Math.floorMod(j+1, M)], Rc, 4, L); //right
                            p.addVecins(grid[Math.floorMod(i + 1, M)][Math.floorMod(j + 1, M)], Rc, 4, L); //downright
                        } else if(i-1 < 0) {
                            p.addVecins(grid[Math.floorMod(i-1, M)][Math.floorMod(j+1, M)], Rc, 2, L); //upright
                            p.addVecins(grid[Math.floorMod(i-1, M)][j], Rc, 2, L); //up
                            p.addVecins(grid[i][Math.floorMod(j+1, M)], Rc, 0, 0); //right
                            p.addVecins(grid[Math.floorMod(i + 1, M)][Math.floorMod(j + 1, M)], Rc, 0, 0); //downright
                        } else if(i+1 >= M && j+1 >= M) {
                            p.addVecins(grid[Math.floorMod(i-1, M)][Math.floorMod(j+1, M)], Rc, 4, L); //upright
                            p.addVecins(grid[Math.floorMod(i-1, M)][j], Rc, 0, 0); //up
                            p.addVecins(grid[i][Math.floorMod(j+1, M)], Rc, 4, L); //right
                            p.addVecins(grid[Math.floorMod(i + 1, M)][Math.floorMod(j + 1, M)], Rc, 3, L); //downright
                        } else if(j+1 >= M) {
                            p.addVecins(grid[Math.floorMod(i - 1, M)][Math.floorMod(j + 1, M)], Rc, 4, L); //upright
                            p.addVecins(grid[Math.floorMod(i - 1, M)][j], Rc, 0, 0); //up
                            p.addVecins(grid[i][Math.floorMod(j + 1, M)], Rc, 4, L); //right
                            p.addVecins(grid[Math.floorMod(i + 1, M)][Math.floorMod(j + 1, M)], Rc, 4, L); //downright
                        } else if(i+1 >= M) {
                            p.addVecins(grid[Math.floorMod(i - 1, M)][Math.floorMod(j + 1, M)], Rc, 0, 0); //upright
                            p.addVecins(grid[Math.floorMod(i - 1, M)][j], Rc, 0, 0); //up
                            p.addVecins(grid[i][Math.floorMod(j + 1, M)], Rc, 0, 0); //right
                            p.addVecins(grid[Math.floorMod(i + 1, M)][Math.floorMod(j + 1, M)], Rc, 5, L); //downright
                        } else {
                            p.addVecins(grid[Math.floorMod(i-1, M)][Math.floorMod(j+1, M)], Rc, 0, 0); //upright
                            p.addVecins(grid[Math.floorMod(i-1, M)][j], Rc, 0, 0); //up
                            p.addVecins(grid[i][Math.floorMod(j+1, M)], Rc, 0, 0); //right
                            p.addVecins(grid[Math.floorMod(i + 1, M)][Math.floorMod(j + 1, M)], Rc, 0, 0); //downright
                        }
                    } else {
                        if(i-1 >= 0)
                            p.addVecins(grid[i-1][j], Rc, 0, 0); //up
                        if(i-1 >= 0 && j+1 < M)
                            p.addVecins(grid[i-1][j+1], Rc, 0, 0); //upright
                        if(j+1 < M)
                            p.addVecins(grid[i][j+1], Rc, 0, 0); //right
                        if(i+1 < M && j+1 < M) {
                            p.addVecins(grid[i+1][j+1], Rc, 0, 0); //downright
                        }
                    }
                }
            }
        }
    }
}
