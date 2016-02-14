package hockey;

import java.util.ArrayList;

public class Team {
    private ArrayList<Player> players;
    private int points = 0;
    private boolean ready = false;

    public Team(ArrayList<Player> players) {
        this.players = players;
    }
    
}
