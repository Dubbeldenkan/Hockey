package hockey;


import hockey.Coord;

public class GraphicalCircle {
    
    public GraphicalCircle(Coord coord, double radius) {
        this.coord = coord;
        this.radius = radius;
    }
    
    protected Coord coord;
    protected double radius;

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
    
    public Coord getCoord() {
        return coord;
    }

    public double getRadius() {
        return radius;
    }
    
}
