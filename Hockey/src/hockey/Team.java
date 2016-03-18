package hockey;

import java.util.ArrayList;

public class Team {
    private ArrayList<Player> players;
    private int points = 0;
    private boolean ready = false;
    private int teamNumber;

    public Team(ArrayList<Player> players, int teamNumber) {
        this.players = players;
        this.teamNumber = teamNumber;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
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

    public boolean isReady() {
        return ready;
    }
    
    public Player getTeamMember(int i){
        return players.get(i);
    }
}
