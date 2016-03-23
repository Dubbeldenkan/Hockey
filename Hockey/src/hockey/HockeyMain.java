package hockey;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class HockeyMain {

    private Server server;
    private Client client;
    private Paint paint;
    private Timer repaintTimer;
    private Timer endTurnRepaintTimer;
    private Team team0;
    private Team team1;
    private Puck puck;
    final private boolean thisIsAServer;
    private int teamSize = 3;
    final private int paintFreq = 1;
    final private int endTurnStepTime = 10;

    public HockeyMain(boolean thisIsAServer) {
        this.thisIsAServer = thisIsAServer;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                // sätt upp banan
                paint = new Paint(teamSize, thisIsAServer);
                JFrame frame = new JFrame("Hockey");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(paint);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                repaintTimer = new Timer(1000 / paintFreq, new ActionListener() {
                @Override
                    public void actionPerformed(ActionEvent e) {
                        transferData2Paint();
                        paint.repaint();
                    }
                });
                repaintTimer.setInitialDelay(0);
                repaintTimer.setRepeats(true);
                repaintTimer.setCoalesce(true);

                endTurnRepaintTimer = new Timer(endTurnStepTime, new ActionListener() {
                @Override
                    public void actionPerformed(ActionEvent e) {
                        endTurnLoop();
                    }
                });
                endTurnRepaintTimer.setInitialDelay(0);
                endTurnRepaintTimer.setRepeats(false);
                endTurnRepaintTimer.setCoalesce(true);
                
                // initiera
                ArrayList<Player> playerListTeam1 = new ArrayList<>();
                playerListTeam1.add(new Player(new Coord(35, 12), 0));
                playerListTeam1.add(new Player(new Coord(35, 25), 3));
                playerListTeam1.add(new Player(new Coord(35, 38), 8));
                team0 = new Team(playerListTeam1, 0);
                ArrayList<Player> playerListTeam2 = new ArrayList<>();
                playerListTeam2.add(new Player(new Coord(65, 12), 2));
                playerListTeam2.add(new Player(new Coord(65, 25), 5));
                playerListTeam2.add(new Player(new Coord(65, 38), 9));
                team1 = new Team(playerListTeam2, 1);
                
                paint.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "endTurn");
                paint.getActionMap().put("endTurn", new KeyActionEvent("END_TURN"));

                //initiera puck
                puck = new Puck(new Coord(50, 25));
                
                if(thisIsAServer)
                {
                    try
                    {
                       server = new Server(6066);
                    }catch(IOException e)
                    {
                       e.printStackTrace();
                    }
                    server.run();
                }
                else
                {
                    client = new Client("localhost", 6066);
                }
                
                repaintTimer.start();
            }
        });
    }
    
    class KeyActionEvent extends AbstractAction {

        public KeyActionEvent(String keyStrokeName) {
            putValue(Action.NAME, keyStrokeName);
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (getValue(Action.NAME).equals("END_TURN")) {
                endTurn();
            }
        }
    }

    private void endTurn()
    {
        if(thisIsAServer)
        {
            setDirectionAndForceValues(team0);
            paint.resetTextField(team0);
        }
        else
        {
            setDirectionAndForceValues(team1);
            paint.resetTextField(team1);
        }
        endTurnRepaintTimer.setRepeats(true);
        endTurnRepaintTimer.start();
    }

    private void endTurnLoop()
    {
        if(checkAnyMovement())
        {
            moveAllObjects(endTurnStepTime);
            transferData2Paint();
            paint.repaint();
        }
        else
        {
            endTurnRepaintTimer.setRepeats(false);
        }
    }

    private void setDirectionAndForceValues(Team team)
    {
        int[] directionValues = paint.getDirectionValues(team);
        int[] forceValues = paint.getForceValues(team);
        for(int i = 0; i < team.getPlayers().size(); i++)
        {
            team.getTeamMember(i).setDirection(directionValues[i]);
            // *100 är för att göra om det till N från kN
            team.getTeamMember(i).setForce(forceValues[i]*100);
        }
    }

    private void moveAllObjects(long timeStep)
    {
        ArrayList<Player> playerArray = new ArrayList<>();
        playerArray.addAll(team0.getPlayers());
        playerArray.addAll(team1.getPlayers());
        for (Player player : playerArray) {
            player.moveObject(timeStep);
            checkCollisionWithOtherObjectForPlayer(player);
        }
        puck.moveObject(timeStep);
        checkCollisionWithObjectForPuck();
    }

    private void checkCollisionWithOtherObjectForPlayer(Player player)
    {
        ArrayList<Player> playerArray = new ArrayList<>();
        playerArray.addAll(team0.getPlayers());
        playerArray.addAll(team1.getPlayers());
        for (Player otherPlayer : playerArray) 
        {
            if(!player.equals(otherPlayer))
            {
                player.checkCollisionWithObject(otherPlayer, endTurnStepTime);
            }
        }
        player.checkCollisionWithObject(puck, endTurnStepTime);
    }

    private void checkCollisionWithObjectForPuck()
    {
        ArrayList<Player> playerArray = new ArrayList<>();
        playerArray.addAll(team0.getPlayers());
        playerArray.addAll(team1.getPlayers());
        for (Player otherPlayer : playerArray) 
        {
            puck.checkCollisionWithObject(otherPlayer, endTurnStepTime);
        }
    }

    private boolean checkAnyMovement()
    {
        for(int i = 0; i < team0.getPlayers().size(); i++)
        {
            if(team0.getTeamMember(i).isMoving())
            {
                return true;
            }
        }
        for(int i = 0; i < team1.getPlayers().size(); i++)
        {
            if(team1.getTeamMember(i).isMoving())
            {
                return true;
            }
        }
        if(puck.isMoving())
        {
            return true;
        }
        return false;
    }

    private void transferData2Paint()
    {
        paint.setTeamA(team0);
        paint.setTeamB(team1);
        paint.setPuck(new GraphicalCircle(puck.getCoord(), puck.getDiameter()));
    }
}
