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
    
    public int getDirection(){
        return direction;
    }
    
    public double getVelocity(){
        return velocity;
    }
    
    public double getForce(){
            return force;
    }

    public void setForce(double force) {
        this.force = force;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public float getMass() {
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
        // Todo: detta är fel. Det ska vara velocity = velocity + acceleration
        // och för att det ska funka måste force göras om till att vara en stöt
        velocity =+ acceleration;
        double distance = velocity*timeStepInS;
        double movementInX = distance*cos(direction);
        double movementInY = distance*sin(direction);
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
    
    public void checkCollisionWithObject(GameObject objectB, int stepTime)
    {
        if(checkIfObjectWillCollide(objectB, stepTime))
        {
            int collisionDirection = calculateDirectionToObject(objectB.getCoord());
            int objectACollisionDirectionDiff = (360 + direction - collisionDirection)%360;
            int objectBCollisionDirectionDiff = (360 + objectB.getDirection() - collisionDirection + 180)%360;

            double objectAOldP = velocity*mass;
            double objectBOldP = objectB.getMass()*objectB.getVelocity();
            
            double objectANewP = Math.abs(calculateInvertedForceRatio(objectACollisionDirectionDiff)*objectAOldP) + 
                    Math.abs(calculateForceRatio(objectBCollisionDirectionDiff)*objectBOldP);
            double objectBNewP = Math.abs(calculateInvertedForceRatio(objectBCollisionDirectionDiff)*objectBOldP) + 
                    Math.abs(calculateForceRatio(objectACollisionDirectionDiff)*objectAOldP);
            
            force = force - (objectAOldP - objectANewP);
            double objectBForce = objectB.getForce() - (objectBOldP - objectBNewP);
            
            int objectBDirection;
            if(objectACollisionDirectionDiff == 90 ||
                    objectACollisionDirectionDiff == 270)
            {
                objectBDirection = direction;
                direction = objectB.getDirection();
            }
            else if(objectBOldP == 0)
            {
                objectBDirection = collisionDirection;
                direction = (360 + (int) (collisionDirection + 
                        90*Math.signum(direction - collisionDirection)))%360;
            }
            //båda rör på sig och det är ingen 90 gradig krock
            else
            {
                double forceRatio = (objectBForce - force)/(objectBForce + force);
                if(forceRatio == 1)
                {
                    objectBDirection = direction;
                    direction = objectB.getDirection();
                }
                //då båda kropparna är i rörelse och inte har samma kraft
                else
                {
                    //todo: detta kanske borde kollas över
                    objectBDirection = (360 + direction + (int) forceRatio*objectBCollisionDirectionDiff)%360;
                    direction = (360 + objectB.getDirection() - (int) forceRatio*objectACollisionDirectionDiff)%360;
                }
            }
            objectB.setDirection(objectBDirection);
            objectB.setForce(objectBForce);
        }
    }
    
    private boolean checkIfObjectWillCollide(GameObject objectB, int stepTimeMs)
    {
        double stepTimeS = (double) stepTimeMs/1000;
        Coord coordA = new Coord(coord.getX() + stepTimeS*velocity*cos(direction), 
                coord.getY() + stepTimeS*velocity*sin(direction));
        Coord coordB = new Coord(objectB.getCoord().getX() + 
                stepTimeS*objectB.getVelocity()*cos(objectB.getDirection()), 
                objectB.getCoord().getY() +
                        stepTimeS*objectB.getVelocity()*sin(objectB.getDirection()));
        double distance = calculateDistance(coordA, coordB);
        boolean result = distance < (diameter/2 + objectB.getDiameter()/2);
        return result;
    }
    
    private double calculateInvertedForceRatio(int collisionDiff)
    {
        double forceRatio = calculateForceRatio(collisionDiff);
        double invertedForceRatio;
        if(forceRatio == 0)
        {
            invertedForceRatio = 0;
        }
        else
        {
            invertedForceRatio = forceRatio/Math.abs(forceRatio) - forceRatio;
        }
        return invertedForceRatio;
    }
    
    private double calculateForceRatio(int collisionDiff)
    {
        double collisionRatio = ((double) collisionDiff - 90)/90;
        if(collisionRatio > 2)
        {
            collisionRatio = collisionRatio - 2;
        }
        else if(collisionRatio > 0 && collisionRatio < 2)
        {
            collisionRatio = 0;
        }
        return collisionRatio;
    }
    
    private int calculateDirectionToObject(Coord objectCoord)
    {
        int angle;
        double xDiff = objectCoord.getX() - coord.getX();
        double yDiff = objectCoord.getY() - coord.getY();
        if(xDiff != 0)
        {
            angle = (int) (Math.atan(yDiff/xDiff)*180/Math.PI);
            angle = (360 + angle)%360;
        }
        else if(yDiff != 0)
        {
            angle = 90 + 90*(1 - (int) Math.signum(yDiff));
        }
        else
        {
            angle = 0;
        }
        if(xDiff < 0)
        {
            angle = (180 + angle)%360;
        }
        return angle;
    }
    
    private double calculateDistance(Coord coord1, Coord coord2)
    {
        double xDistance = Math.pow(coord1.getX() - coord2.getX(), 2);
        double yDistance = Math.pow(coord1.getY() - coord2.getY(), 2);
        double distance = Math.sqrt(xDistance + yDistance);
        return distance;
    }
    
    private double cos(double angle)
    {
        return Math.cos(angle*Math.PI/180);
    }
    
    private double sin(double angle)
    {
        return Math.sin(angle*Math.PI/180);
    }
}
