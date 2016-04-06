package hockey;

import java.util.ArrayList;

public class Team {
    private ArrayList<Player> players;
    private int points = 0;
    private int teamNumber;
    private int teamSize;

    public Team(ArrayList<Player> players, int teamNumber) {
        this.players = players;
        this.teamNumber = teamNumber;
        this.teamSize = players.size();
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void addPoint() {
        this.points = points + 1;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    
    public Player getPlayer(int index)
    {
        return players.get(index);
    }

    public int getPoints() {
        return points;
    }
    
    public int getTeamNumber(){
        return teamNumber;
    }
    
    public Player getTeamMember(int i){
        return players.get(i);
    }
    
    public void setPlayersValues(double[] forceVector, int[] directionVector)
    {
        for(int index = 0; index < teamSize; index++)
        {
            players.get(index).setForce(forceVector[index]);
            players.get(index).setDirection(directionVector[index]);
        }
    }
    
    public double[] getForceVector()
    {
        double[] vector = new double[teamSize];
        for(int index = 0; index < teamSize; index++)
        {
            vector[index] = players.get(index).getForce();
        }
        return vector;
    }
    
    public int[] getDirectionVector()
    {
        int[] vector = new int[teamSize];
        for(int index = 0; index < teamSize; index++)
        {
            vector[index] = players.get(index).getDirection();
        }
        return vector;
    }
    
    public void resetTeam()
    {
        for(Player player :players)
        {
            player.resetObject();
        }
    }
}
