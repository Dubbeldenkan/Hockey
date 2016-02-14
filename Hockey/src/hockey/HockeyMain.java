package hockey;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class HockeyMain {

    private Paint paint;
    private Timer repaintTimer;
    private ArrayList<Team> teams = new ArrayList<>();;
    private Puck puck;
    final private int paintFreq = 25;

    public HockeyMain() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                // sätt upp banan
                paint = new Paint();
                JFrame frame = new JFrame("Hockey");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(paint);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                repaintTimer = new Timer(1000 / paintFreq, (ActionEvent e) -> {
                    paint.repaint();
                });
                repaintTimer = new Timer(1000 / paintFreq, null);
                repaintTimer.setInitialDelay(0);
                repaintTimer.setRepeats(true);
                repaintTimer.setCoalesce(true);

                // initiera
                ArrayList<Player> playerListTeam1 = new ArrayList<>();
                playerListTeam1.add(new Player(new Coordinates(50, 25)));
                playerListTeam1.add(new Player(new Coordinates(50, 50)));
                playerListTeam1.add(new Player(new Coordinates(50, 75)));
                teams.add(new Team(playerListTeam1));
                ArrayList<Player> playerListTeam2 = new ArrayList<>();
                playerListTeam2.add(new Player(new Coordinates(100, 25)));
                playerListTeam2.add(new Player(new Coordinates(100, 50)));
                playerListTeam2.add(new Player(new Coordinates(100, 75)));
                teams.add(new Team(playerListTeam2));

                //initiera puck
                puck = new Puck(new Coordinates(75, 75));
            }
        });
    }
}
