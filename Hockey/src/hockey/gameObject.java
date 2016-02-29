package hockey;

public class GameObject {
    
        public GameObject(Coord coord, int diameter, float mass) {
        this.coord = coord;
        this.diameter = diameter;
        this.mass = mass;
    }
    
    protected Coord coord;
    protected int diameter; 
    protected float mass; // in kg
    protected int teamNumber;
    protected double force = 0;
    protected double velocity = 0;
    protected int direction;

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public void setRadius(int radius) {
        this.diameter = radius;
    }

    public void setWeight(float weight) {
        this.mass = weight;
    }

    public Coord getCoord() {
        return coord;
    }

    public int getRadius() {
        return diameter;
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
        double movementInX = distance*Math.cos(direction*Math.PI/180);
        double movementInY = distance*Math.sin(direction*Math.PI/180); //todo: är det fel riktning?
        collision(movementInX, movementInY);
        coord.moveX((float) (movementInX));
        coord.moveY((float) (movementInY));
    }
    
    private void collision(double movementInX, double movementInY)
    {
        if((coord.getX() - ((double) diameter)/2 < 0) && 
                ((direction > 90) || (direction > 270)))
        {
            direction = (direction)%360;
            //kolla på formel för reflektion
        }
        else if((coord.getX() + ((double) diameter)/2 > 100) &&
                ((direction > 90) || (direction < 270)))
        {
            direction = direction - (direction - 180);
        }
        
        /*if((coord.getY() - ((double) diameter)/2 < 0) && 
                ((direction > 90) || (direction > 270)))
        {
            direction = (direction + 180)%360;
        }
        else if(coord.getY() + ((double) diameter)/2 > 100)
        {
            direction = direction - (direction - 180);
        }*/
    }
}
