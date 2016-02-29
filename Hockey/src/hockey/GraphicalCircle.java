package hockey;


import hockey.Coord;

public class GraphicalCircle {
    
    public GraphicalCircle(Coord coord, int radius) {
        this.coord = coord;
        this.radius = radius;
    }
    
    protected Coord coord;
    protected int radius;

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
    
    public Coord getCoord() {
        return coord;
    }

    public int getRadius() {
        return radius;
    }
    
}
