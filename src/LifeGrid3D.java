import java.util.LinkedList;
import java.util.List;

public class LifeGrid3D implements LifeGrid {
    private int height;
    private int width;
    private int depth;
    private LifeParticle[][][] grid;
    private List<LifeParticle> particles;
    private int[] dirs = {
            -1,-1,1,
            -1,0,1,
            -1,1,1,
            0,-1,1,
            0,0,1,
            0,1,1,
            1,-1,1,
            1,0,1,
            1,1,1,
            -1,-1,-1,
            -1,-1,0,
            -1,0,-1,
            -1,0,0,
            -1,1,-1,
            -1,1,0,
            0,-1,-1,
            0,-1,0,
            0,0,-1,
            0,1,-1,
            0,1,0,
            1,-1,-1,
            1,-1,0,
            1,0,-1,
            1,0,0,
            1,1,-1,
            1,1,0,
            };

    public LifeGrid3D(int H, int W, int D) {
        this.height = H;
        this.width = W;
        this.depth = D;
        this.grid = new LifeParticle[H][W][D];
        this.particles = new LinkedList<>();
    }

    public void populate(List<LifeParticle> ps) {
        List<LifeParticle> dedPart = new LinkedList<>();
        this.grid = new LifeParticle[height][width][depth];
        for(LifeParticle p : ps) {
            grid[p.getX()][p.getY()][p.getZ()] = p;
        }
        for(LifeParticle p : ps) {
            if(p.isAlive()) {

                int x = p.getX();
                int y = p.getY();
                int z = p.getZ();
                p.setCenterDist(Math.sqrt((Math.pow((double) p.getX() - (double) height/2.0, 2) + Math.pow((double) p.getY() - (double) width/2.0, 2) + Math.pow((double) p.getZ() - (double) depth/2.0, 2))));
                for (int i = 0; i < dirs.length; i += 3) {
                    if (x + dirs[i] >= 0 && x + dirs[i] < height && y + dirs[i + 1] >= 0 && y + dirs[i + 1] < width && z + dirs[i + 2] >= 0 && z + dirs[i + 2] < depth) {
                        if(grid[x+dirs[i]][y + dirs[i + 1]][z+dirs[i + 2]] == null) {
                            LifeParticle deadP = new LifeParticle(x + dirs[i], y + dirs[i + 1], z + dirs[i + 2],(x + dirs[i]) * height + (y + dirs[i + 1]), false);
                            grid[x + dirs[i]][y + dirs[i + 1]][z + dirs[i + 2]] = deadP;
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
            int z = p.getZ();

            if(x-1 >= 0) {
                p.addVecinAndVicenVersa(grid[x-1][y][z]);
            }
            if(x-1 >= 0 && y+1 < width) {
                p.addVecinAndVicenVersa(grid[x-1][y+1][z]);
            }
            if(y+1 < width) {
                p.addVecinAndVicenVersa(grid[x][y+1][z]);
            }
            if(x+1 < height && y+1 < width) {
                p.addVecinAndVicenVersa(grid[x+1][y+1][z]);
            }
            if(z+1 < depth) {
                for (int i = 0; i < 27; i+=3) {
                    if (x + dirs[i] >= 0 && x + dirs[i] < height && y + dirs[i + 1] >= 0 && y + dirs[i + 1] < width) {
                        p.addVecinAndVicenVersa(grid[x + dirs[i]][y + dirs[i + 1]][z + dirs[i + 2]]);
                    }
                }
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
        return depth;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public LifeParticle[][][] getGrid() {
        return grid;
    }

    public void setGrid(LifeParticle[][][] grid) {
        this.grid = grid;
    }

    public List<LifeParticle> getParticles() {
        return particles;
    }
}
