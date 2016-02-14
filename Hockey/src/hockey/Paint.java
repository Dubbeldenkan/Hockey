
package hockey;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class Paint extends JPanel {

    final private int windowWidth = 1200;
    final private int windowHeight = 600;
    final private int rinkWidth = 1000;
    final private int rinkHeight = rinkWidth/2;
    final private int rinkWidthStart = 100;
    final private int rinkHeightStart = 50;
    //final private Timer repaintTimer;
    
    //final private int paintFreq = 25;

    public Paint() {

        /*repaintTimer = new Timer(1000/paintFreq, (ActionEvent e) -> {
            repaint();
        });
        //repaintTimer = new Timer(1000/paintFreq, null);
        repaintTimer.setInitialDelay(0);
        repaintTimer.setRepeats(true);
        repaintTimer.setCoalesce(true);*/
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(windowWidth, windowHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintWindow(g);
        paintRink(g);
        paintTeam(g);
        paintPuck(g);
    }
    
    private void paintTeam(Graphics g)
    {
        
    }
    
    private void paintPuck(Graphics g)
    {
        
    }
    
    private void paintRink(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        //måla mittenstrecket
        int numberOfRedLines = 19;
        g2d.setColor(Color.RED);
        int redLinesLength = rinkHeight/(numberOfRedLines*2 + 1);
        for(int i = 0; i < numberOfRedLines + 1; i++)
        {
            g2d.drawLine(rinkWidthStart + rinkWidth/2, 
                    rinkHeightStart + redLinesLength + i*2*redLinesLength, 
                    rinkWidthStart + rinkWidth/2, 
                    rinkHeightStart + 2*redLinesLength + i*2*redLinesLength);
        }
        int circleRadius = rinkWidth/6;
        //måla mållinjer
        int goalLineWidth = 2;
        g2d.fillRect((int) (rinkWidthStart + rinkWidth/15), 
                rinkHeightStart, goalLineWidth, rinkHeight);
        g2d.fillRect((int) (rinkWidthStart + rinkWidth*0.93), 
                rinkHeightStart, goalLineWidth, rinkHeight);
        //måla mittcirkeln
        g2d.setColor(Color.BLUE);
        g2d.drawOval(rinkWidthStart + rinkWidth/2 - circleRadius/2,
                rinkHeightStart + rinkHeight/2 - circleRadius/2, 
                circleRadius, circleRadius);
        //måla mittpricken
        int dotRadius = circleRadius/20;
        g2d.fillOval(rinkWidthStart + rinkWidth/2 - dotRadius/2,
                rinkHeightStart + rinkHeight/2 - dotRadius/2, 
                dotRadius, dotRadius);
        //måla blå linjer
        int blueLineWidth = 3;
        g2d.fillRect((int) (rinkWidthStart + rinkWidth*0.375), 
                rinkHeightStart, blueLineWidth, rinkHeight);
        g2d.fillRect((int) (rinkWidthStart + rinkWidth*0.625), 
                rinkHeightStart, blueLineWidth, rinkHeight);
        
        g2d.dispose();
    }
    
    private void paintWindow(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.BLACK);
        //måla rinken, över- och undersida
        g2d.fillRect(0, 0, windowWidth, windowHeight);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRoundRect(rinkWidthStart, rinkHeightStart, rinkWidth, rinkHeight, 
                (int) (0.14*rinkWidth), (int) (0.14*rinkWidth));
        
        g2d.dispose();
    }
}
