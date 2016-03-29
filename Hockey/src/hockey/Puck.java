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
            if((nextYPos < (25 - 25/(2*rinkInput.xSizeGoalDenominator))) ||
                (nextYPos > (25 + 25/(2*rinkInput.xSizeGoalDenominator))))
            {
                direction = (540 - direction)%360;
            }
            else if(nextXPos < (0 - 2*radius))
            {
                int i = 0;
            }
        }
        else if(((nextXPos + radius) > 100) &&
                ((direction < 90) || (direction > 270)))
        {
            if(((nextYPos < (25 - 25/(2*rinkInput.ySizeGoalDenominator))) ||
                (nextYPos > (25 + 25/(2*rinkInput.ySizeGoalDenominator)))))
            {
                direction = (540 - direction)%360;
            }
            else if(nextXPos > (100 + 2*radius))
            {
                int i = 0;
            }
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
