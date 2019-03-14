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
        int col = (int) Math.floor(x / (L / (double)M));
        return col;
    }

    public int getCellRow(double L, int M) {
        int row = (int) Math.floor(y / (L / (double)M));
        return row;
    }

    public double getDistance(Point op) {
        return Math.sqrt((Math.pow(x - op.getX(), 2)) + Math.pow(y - op.getY(), 2));
    }

    public double getDistance(Point op, int side, double length) {
        switch (side) {
            case 1:
                return Math.sqrt((Math.pow(x - (op.getX() + length), 2)) + Math.pow(y - (op.getY() - length), 2));
            case 2:
                return Math.sqrt((Math.pow(x - (op.getX()), 2)) + Math.pow(y - (op.getY() - length), 2));
            case 3:
                return Math.sqrt((Math.pow(x - (op.getX() + length), 2)) + Math.pow(y - (op.getY() + length), 2));
            case 4:
                return Math.sqrt((Math.pow(x - (op.getX() + length), 2)) + Math.pow(y - (op.getY()), 2));
            case 5:
                return Math.sqrt((Math.pow(x - (op.getX()), 2)) + Math.pow(y - (op.getY() + length), 2));
        }
        return Math.sqrt((Math.pow(x - op.getX(), 2)) + Math.pow(y - op.getY(), 2));
    }

    @Override
    public String toString() {
        return "["+x+", "+y+"]";
    }
}
