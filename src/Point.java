public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getCellCol(double L, int M) {
        int col = (int) Math.floor(y / (L / (double)M));
        return col;
    }

    public int getCellRow(double L, int M) {
        int row = (int) Math.floor(x / (L / (double)M));
        return row;
    }

    @Override
    public String toString() {
        return "[ "+x+", "+y+"]";
    }
}
