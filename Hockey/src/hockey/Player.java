package hockey;

public class Player extends GameObject{

    public Player(Coord coord, int shirtNumber) {
        super(coord, 5, 60);
        this.shirtNumber = shirtNumber;
    }
    
    private final int shirtNumber;
    
    public int getShirtNumber()
    {
        return shirtNumber;
    }
    
    public boolean samePlayer(int tempShirtNumber)
    {
        return (tempShirtNumber == this.shirtNumber);
    }
}
