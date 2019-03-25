import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class LifeGrid2D implements LifeGrid{
    private int height;
    private int width;
    private int depth;
    private LifeParticle[][] grid;
    private List<LifeParticle> particles;
    private int[] dirs = {  -1,1,
                            -1,0,
                            -1,-1,
                            0,-1,
                            0,1,
                            1,1,
                            1,0,
                            1,-1};

    public LifeGrid2D(int H, int W) {
        this.height = H;
        this.width = W;
        this.grid = new LifeParticle[H][W];
        this.particles = new LinkedList<>();
    }

    public void populate(List<LifeParticle> ps) {
        List<LifeParticle> dedPart = new LinkedList<>();
        this.grid = new LifeParticle[height][width];
        for(LifeParticle p : ps) {
            grid[p.getX()][p.getY()] = p;
        }
        for(LifeParticle p : ps) {
            if(p.isAlive()) {

                p.setCenterDist(Math.sqrt((Math.pow((double) p.getX() - (double) height/2.0, 2) + Math.pow((double) p.getY() - (double) width/2.0, 2) + Math.pow((double) p.getZ() - (double) depth/2.0, 2))));
                int x = p.getX();
                int y = p.getY();
                for (int i = 0; i < dirs.length; i += 2) {
                    if (x + dirs[i] >= 0 && x + dirs[i] < height && y + dirs[i + 1] >= 0 && y + dirs[i + 1] < width) {
                        if(grid[x+dirs[i]][y + dirs[i + 1]] == null) {
                            LifeParticle deadP = new LifeParticle(x + dirs[i], y + dirs[i + 1], (x + dirs[i]) * height + (y + dirs[i + 1]), false);
                            grid[x + dirs[i]][y + dirs[i + 1]] = deadP;
                            dedPart.add(deadP);
                        }
                    }

                }
            }
        }
        ps.addAll(dedPart);
        this.particles = ps;
    }

    public void calculateVecins() {
        for(LifeParticle p : particles) {
            int x = p.getX();
            int y = p.getY();
            if(x-1 >= 0) {
                grid[x][y].addVecinAndVicenVersa(grid[x-1][y]);
            }
            if(x-1 >= 0 && y+1 < width) {
                grid[x][y].addVecinAndVicenVersa(grid[x-1][y+1]);
            }
            if(y+1 < width) {
                grid[x][y].addVecinAndVicenVersa(grid[x][y+1]);
            }
            if(x+1 < height && y+1 < width) {
                grid[x][y].addVecinAndVicenVersa(grid[x+1][y+1]);
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

    public int getDepth() {
        return 0;
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
