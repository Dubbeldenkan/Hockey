package hockey;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class HockeyMain {
    private Paint paint;
    private Timer repaintTimer;
    final private int paintFreq = 25;
        public HockeyMain() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                paint = new Paint();
                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(paint);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                
                repaintTimer = new Timer(1000/paintFreq, (ActionEvent e) -> {
                   paint.repaint();
                });
                repaintTimer = new Timer(1000/paintFreq, null);
                repaintTimer.setInitialDelay(0);
                repaintTimer.setRepeats(true);
                repaintTimer.setCoalesce(true);
            }
        });
    }
}
