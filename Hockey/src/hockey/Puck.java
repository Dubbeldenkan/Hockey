package hockey;

public class Puck extends GameObject{

    private final RinkInput rinkInput;
    private boolean goalDone;
    private int teamThatScored;
    
    public Puck(Coord coord) {
        super(coord, 3, 30);
        this.rinkInput = new RinkInput();
    }
    
    public void reset()
    {
        goalDone = false;
        resetObject();
    }
    
    public boolean isGoalDone()
    {
        return goalDone;
    }
    
    public int getTeamThatScored()
    {
        return teamThatScored;
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
                goalDone = true;
                teamThatScored = 1;
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
                goalDone = true;
                teamThatScored = 0;
            }
        }
        if((((nextYPos - radius) < 0)  && 
                (direction > 180)) || 
                (((nextYPos - radius) < (25 - rinkInput.ySizeGoalDenominator/2)) &&
                ((nextXPos > 100) || (nextXPos < 0))))
        {
            direction = (360 - direction)%360;
        }
        else if((((nextYPos + radius) > 50) &&
                (direction < 180)) || 
                (((nextYPos + radius) > (25 + rinkInput.ySizeGoalDenominator/2)) &&
                ((nextXPos > 100) || (nextXPos < 0))))
        {
            direction = (360 - direction)%360;
        }
    }
}
