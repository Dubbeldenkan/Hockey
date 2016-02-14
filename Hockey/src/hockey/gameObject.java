package hockey;

public class gameObject {
    
        public gameObject(Coordinates coord, int radius, float weight) {
        this.coord = coord;
        this.radius = radius;
        this.weight = weight;
    }
    
    protected Coordinates coord;
    protected int radius; 
    protected float weight; // in kg
    
}
