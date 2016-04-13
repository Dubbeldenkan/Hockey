
package hockey;

public class Coord {

    public Coord(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Coord(Coord coord)
    {
        this.x = coord.getX();
        this.y = coord.getY();
    }

    private double x;
    private double y;
    
    public void moveX(double x){
        this.x += x;
    }
    
    public void moveY(double y){
        this.y += y;
    }
    
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    public Coord getValues()
    {
        return new Coord(x, y);
    }
}
