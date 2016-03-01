package hockey;

public class GameObject {
    
        public GameObject(Coord coord, int diameter, float mass) {
        this.coord = coord;
        this.diameter = diameter;
        this.mass = mass;
    }
    
    protected Coord coord;
    protected double diameter; 
    protected float mass; // in kg
    protected int teamNumber;
    protected double force = 0;
    protected double velocity = 0;
    protected int direction;

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    public void setWeight(float weight) {
        this.mass = weight;
    }

    public Coord getCoord() {
        return coord;
    }

    public double getDiameter() {
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
        collisionToRink(movementInX, movementInY);
        coord.moveX((float) (movementInX));
        coord.moveY((float) (movementInY));
    }
    
    private void collisionToRink(double movementInX, double movementInY)
    {
        double nextXPos = coord.getX() + movementInX;
        double nextYPos = coord.getY() + movementInY;
        double sizeOfArc = 7.5;
        double radius = diameter/2;
        
        //vänster högre hörn
        if(direction < 315 && direction > 135 && 
                (nextXPos < sizeOfArc + radius) &&
                (nextYPos < sizeOfArc - nextXPos))
        {
            int wallNormal = (((int) (Math.atan((nextYPos - sizeOfArc)/
                    (nextXPos - sizeOfArc))*180/Math.PI)) + 360)%360;
            setDirectionInCorner(wallNormal);
        }
        //vänster nedre hörn
        else if(direction < 225 && direction > 45 && 
                (nextXPos < sizeOfArc + radius) &&
                (nextYPos > 50 - sizeOfArc + nextXPos))
        {
            int wallNormal = (((int) (Math.atan((nextYPos + sizeOfArc - 50)/
                    (nextXPos - sizeOfArc))*180/Math.PI)) + 360)%360;
            setDirectionInCorner(wallNormal);
        }
        //högra över hörn
        else if(((direction < 45) || (direction > 225)) && 
                (nextXPos > 100 - sizeOfArc - radius) &&
                (nextYPos < sizeOfArc - (100 - nextXPos)))
        {
            int wallNormal = (((int) (Math.atan((nextYPos - sizeOfArc)/
                    (nextXPos + sizeOfArc - 100))*180/Math.PI)) + 360)%360;
            setDirectionInCorner(wallNormal);
        }
        //högra nedre hörn
        else if(((direction < 135) || (direction > 315)) && 
                (nextXPos > 100 - sizeOfArc - radius) &&
                (nextYPos > 50 - sizeOfArc + (100 - nextXPos)))
        {
            int wallNormal = (((int) (Math.atan((nextYPos + sizeOfArc - 50)/
                    (nextXPos + sizeOfArc - 100))*180/Math.PI)) + 360)%360;
            setDirectionInCorner(wallNormal);
        }
        else
        {
            if(((nextXPos - radius) < 0) && 
                    ((direction > 90) || (direction < 270)))
            {
                direction = (540 - direction)%360;
            }
            else if(((nextXPos + radius) > 100) &&
                    ((direction < 90) || (direction > 270)))
            {
                direction = (540 - direction)%360;
            }

            if(((nextYPos - radius) < 0) && 
                    (direction > 180))
            {
                direction = (360 - direction)%360;
            }
            else if(((nextYPos + radius) > 50) &&
                    (direction < 180))
            {
                direction = (360 - direction)%360;
            }
        }
    }
    
    private void setDirectionInCorner(int wallNormal)
    {
        int wallDirection = (wallNormal + 270)%360;
        int diff = (360 + direction - wallDirection)%360;
        direction = (360 + direction - 2*diff)%360;
    }
    
    public void checkCollisionWithObject(GameObject object)
    {
        if(calculateDistance(this.getCoord(), object.getCoord()) < 
                (diameter/2 + object.getDiameter()/2))
        {
            int dummyFörAttDebuga = 1;
            //kolla hur en fysikalisk stöt ser ut
        }
    }
    
    private double calculateDistance(Coord coord1, Coord coord2)
    {
        double xDistance = Math.pow(coord1.getX() - coord2.getX(), 2);
        double yDistance = Math.pow(coord1.getY() - coord2.getY(), 2);
        double distance = Math.sqrt(xDistance + yDistance);
        return distance;
    }
}
