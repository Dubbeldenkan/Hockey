package hockey;

public class Puck extends GameObject{

    private RinkInput rinkInput;
    
    public Puck(Coord coord) {
        super(coord, 3, 30);
        this.rinkInput = new RinkInput();
    }
    
    @Override
    protected void collisionAgainstNoCorner(double nextXPos, double nextYPos, double radius)
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
