package hockey;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private Paint paint;
    private Timer repaintTimer;
    //private ArrayList<Team> teams = new ArrayList<>();;
    private Team team0;
    private Team team1;
    private Puck puck;
    private int teamSize = 3;
    final private int paintFreq = 1;

    public HockeyMain() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                // sätt upp banan
                paint = new Paint(teamSize);
                JFrame frame = new JFrame("Hockey");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(paint);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                
                //testa att sätta ut action som en separat funktion
                repaintTimer = new Timer(1000 / paintFreq, new ActionListener() {
                @Override
                    public void actionPerformed(ActionEvent e) {
                        transferData2Paint();
                        paint.repaint();
                    }
                });
                //repaintTimer = new Timer(1000 / paintFreq, null);
                repaintTimer.setInitialDelay(0);
                repaintTimer.setRepeats(true);
                repaintTimer.setCoalesce(true);

                // initiera
                ArrayList<Player> playerListTeam1 = new ArrayList<>();
                playerListTeam1.add(new Player(new Coord(35, 25)));
                playerListTeam1.add(new Player(new Coord(35, 50)));
                playerListTeam1.add(new Player(new Coord(35, 75)));
                team0 = new Team(playerListTeam1, 0);
                ArrayList<Player> playerListTeam2 = new ArrayList<>();
                playerListTeam2.add(new Player(new Coord(65, 25)));
                playerListTeam2.add(new Player(new Coord(65, 50)));
                playerListTeam2.add(new Player(new Coord(65, 75)));
                team1 = new Team(playerListTeam2, 1);
                
                paint.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "endTurn");
                paint.getActionMap().put("endTurn", new KeyActionEvent("END_TURN"));

                //initiera puck
                puck = new Puck(new Coord(50, 50));
                
                repaintTimer.start();
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
                setDirectionAndForceValues(team0);
                paint.resetTextField(team0);
                boolean anyObjectMovement = true;
                while(anyObjectMovement)
                {
                    long stepTime = 10;
                    moveAllObjects(stepTime);
                    transferData2Paint();
                    paint.repaint();
                    anyObjectMovement = checkAnyMovement();
                    /*try {
                        Thread.sleep(stepTime);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HockeyMain.class.getName()).log(Level.SEVERE, null, ex);
                    }*/
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
                for(int i = 0; i < team0.getPlayers().size(); i++)
                {
                    team0.getTeamMember(i).moveObject(timeStep);
                }
                for(int i = 0; i < team1.getPlayers().size(); i++)
                {
                    team1.getTeamMember(i).moveObject(timeStep);
                }
                puck.moveObject(timeStep);
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
                paint.setPuck(new GraphicalCircle(puck.getCoord(), puck.getRadius()));
            }
        });
    }
}
