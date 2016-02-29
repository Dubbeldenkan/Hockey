package hockey;

public class GameObject {
    
        public GameObject(Coord coord, int radius, float mass) {
        this.coord = coord;
        this.radius = radius;
        this.mass = mass;
    }
    
    protected Coord coord;
    protected int radius; 
    protected float mass; // in kg
    protected int teamNumber;
    protected double force = 0;
    protected double velocity = 0;
    protected int direction;

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setWeight(float weight) {
        this.mass = weight;
    }

    public Coord getCoord() {
        return coord;
    }

    public int getRadius() {
        return radius;
    }

    public void setForce(double force) {
        this.force = force;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public float getWeight() {
        return mass;
    }
    
    public boolean isMoving()
    {
        return force > 0;
    }
    
    public void moveObject(long timeStepInMs)
    {
        //steel in ice
        double kineticFriction = 0.05;
        double staticFriction = 0.1;
        double timeStepInS = ((double) timeStepInMs)/1000;
        //double timeStepInS = 0.01;
        double g = 9.82;
        double staticFrictionForce = mass*g*staticFriction;
        double kineticFrictionForce = mass*g*kineticFriction;
        if(staticFrictionForce > force)
        {
            force = 0;
        }
        else
        {
            force = force - kineticFrictionForce;
        }
        double acceleration = force/mass;
        velocity =+ acceleration;
        double distance = velocity*timeStepInS;
        coord.moveX((float) (distance*Math.cos(direction*Math.PI/180)));
        coord.moveY((float) (distance*Math.sin(direction*Math.PI/180)));
    }
}
