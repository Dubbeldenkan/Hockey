
package hockey;

public class Coord {

    public Coord(double x, double y) {
        this.x = x;
        this.y = y;
    }

    private double x;
    private double y;
    
    /*public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }*/
    
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
}
