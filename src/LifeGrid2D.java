import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class LifeGrid2D {
    private int height;
    private int width;
    private LifeParticle[][] grid;
    private List<LifeParticle> particles;

    public LifeGrid2D(int H, int W) {
        this.height = H;
        this.width = W;
        this.grid = new LifeParticle[H][W];
        this.particles = new LinkedList<>();
    }

    public void populate(List<LifeParticle> ps) {
        for(LifeParticle p : ps) {
            grid[p.getX()][p.getY()] = p;
        }
        this.particles = ps;
    }

    public void calculateVecins() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(i-1 >= 0) {
                    grid[i][j].addVecinAndVicenVersa(grid[i-1][j]);
                }
                if(i-1 >= 0 && j+1 < width) {
                    grid[i][j].addVecinAndVicenVersa(grid[i-1][j+1]);
                }
                if(j+1 < width) {
                    grid[i][j].addVecinAndVicenVersa(grid[i][j+1]);
                }
                if(i+1 < height && j+1 < width) {
                    grid[i][j].addVecinAndVicenVersa(grid[i+1][j+1]);
                }
//                grid[i][j].addVecin(grid[i+1][j]);
//                grid[i][j].addVecin(grid[i+1][j-1]);
//                grid[i][j].addVecin(grid[i][j-1]);
//                grid[i][j].addVecin(grid[i-1][j-1]);
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public LifeParticle[][] getGrid() {
        return grid;
    }

    public void setGrid(LifeParticle[][] grid) {
        this.grid = grid;
    }

    public List<LifeParticle> getParticles() {
        return particles;
    }
}
