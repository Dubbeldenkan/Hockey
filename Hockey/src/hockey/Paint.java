
package hockey;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TextField;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Paint extends JPanel {

    final private int windowWidth = 1200;
    final private int windowHeight = 600;
    final private int rinkWidth = 1000;
    final private int rinkHeight = rinkWidth/2;
    final private int rinkWidthStart = 100;
    final private int rinkHeightStart = 50;
    private JTextField[] directionTextField;
    private JTextField[] forceTextField;
    private int[] directionValue;
    private int[] forceValue;
    private Team team0;
    private Team team1;
    private GraphicalCircle puck;
    //final private Timer repaintTimer;
    
    //final private int paintFreq = 25;

    public Paint(int teamSize) {  
        directionTextField = new JTextField[teamSize];
        forceTextField = new JTextField[teamSize];
        directionValue = new int[teamSize];
        forceValue = new int[teamSize];
        setupTextFields(teamSize);
        
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
        if(team0 != null)
        {
            paintTeam(g, team0.getPlayers(), 0);
            paintTeam(g, team1.getPlayers(), 1);
            paintPuck(g);
            paintPoints(g, team0.getPoints(), team1.getPoints());
            paintStats(g, team0);
            paintStats(g, team1);
            paintArrow(g, team0);
        }
    }
    
    private void paintTeam(Graphics g, ArrayList<Player> team, 
            int teamNumber)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        for(int i = 0; i < team.size(); i++)
        {
            if(teamNumber == 0)
            {
                g2d.setColor(Color.RED);
            }
            else if(teamNumber == 1)
            {
                g2d.setColor(Color.BLUE);
            }
            double xCoord = convertObject2GraphicsX(team.get(i).getCoord().getX(), rinkWidth)
                    + rinkWidthStart;
            double yCoord = convertObject2GraphicsY(team.get(i).getCoord().getY(), rinkHeight)
                    + rinkHeightStart;
            double diameter = convertObject2GraphicsX(team.get(i).getDiameter(), rinkWidth);
            g2d.fillOval((int) (xCoord - diameter/2),(int) (yCoord - diameter/2), 
                    (int) diameter, (int) diameter);
            if(teamNumber == 0)
            {
                g2d.setColor(Color.BLUE);
            }
            else if(teamNumber == 1)
            {
                g2d.setColor(Color.RED);
            }
            g2d.setFont(new Font("TimesRoman", Font.BOLD, rinkHeight/15));
            g2d.drawString(String.valueOf(team.get(i).getShirtNumber()), 
                    (int) xCoord - rinkHeight/60,
                    (int) yCoord + rinkHeight/40);
        }
    }
    
    private void paintPuck(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.DARK_GRAY);
        double xCoord = convertObject2GraphicsX(puck.getCoord().getX(), rinkWidth)
                + rinkWidthStart;
        double yCoord = convertObject2GraphicsY(puck.getCoord().getY(), rinkHeight)
                + rinkHeightStart;
        double diameter = convertObject2GraphicsX(puck.getRadius(), rinkWidth);
        g2d.fillOval((int) (xCoord - diameter/2),(int) (yCoord - diameter/2), 
                (int) diameter, (int) diameter);
    }
    
    private void paintPoints(Graphics g, int point0, int point1)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.WHITE);
        
        g2d.setFont(new Font("TimesRoman", Font.BOLD, rinkHeight/15));
        g2d.drawString(String.valueOf(point0) + " - " +
            String.valueOf(point1), (int) rinkWidthStart + rinkWidth/2 - rinkHeight/15,
            (int) rinkHeightStart - rinkHeight/40);
    }
    
    private double convertObject2GraphicsX(double percent, double realSize)
    {
        return percent/100*realSize;
    }
    
    private double convertObject2GraphicsY(double percent, double realSize)
    {
        return percent/50*realSize;
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
        int circleDiameter = rinkHeight/6;
        //måla mållinjer
        int goalLineWidth = 2;
        g2d.fillRect((int) (rinkWidthStart + rinkWidth/15), 
                rinkHeightStart, goalLineWidth, rinkHeight);
        g2d.fillRect((int) (rinkWidthStart + rinkWidth*0.93), 
                rinkHeightStart, goalLineWidth, rinkHeight);
        //måla mittcirkeln
        g2d.setColor(Color.BLUE);
        g2d.drawOval(rinkWidthStart + rinkWidth/2 - circleDiameter/2,
                rinkHeightStart + rinkHeight/2 - circleDiameter/2, 
                circleDiameter, circleDiameter);
        //måla mittpricken
        int dotDiameter = circleDiameter/10;
        g2d.fillOval(rinkWidthStart + rinkWidth/2 - dotDiameter/2,
                rinkHeightStart + rinkHeight/2 - dotDiameter/2, 
                dotDiameter, dotDiameter);
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
    
    private void paintStats(Graphics g, Team team)
    {
        int xCoord;
        int yCoord = (int) rinkHeightStart;
        if(team.getTeamNumber() == 0)
        {
            xCoord = (int) rinkWidthStart/5;
        }
        else
        {
            xCoord = (int) rinkWidthStart*6/5 + rinkWidth;
        }
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("TimesRoman", Font.BOLD, rinkHeight/15));
        int yStepSize = rinkHeight/team.getPlayers().size();
        for(int i = 0; i < team.getPlayers().size(); i++)
        {
            g2d.drawString(String.valueOf(team.getPlayer(i).getShirtNumber()), xCoord,
                yCoord + yStepSize/2 + i*yStepSize);
        }
    }
    
    private void setupTextFields(int teamSize)
    {
        //int yStepSize = rinkHeight/team.getPlayers().size();
        int yStepSize = rinkHeight/teamSize;
        setLayout(null);
        for(int yPos = 0; yPos < teamSize; yPos++)
        {
            forceTextField[yPos] = new JTextField(6);
            this.add(forceTextField[yPos]);
            forceTextField[yPos].setBounds(new Rectangle(new Point(100, 100), forceTextField[yPos].getPreferredSize()));
            this.setVisible(true);
            forceTextField[yPos].setLocation(rinkWidthStart/10, yStepSize*(1 + yPos));
            
            forceTextField[yPos].getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    
                    repaint();
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    repaint();
                }
                @Override
                public void insertUpdate(DocumentEvent e) {
                    repaint();
                }
            });
            
            directionTextField[yPos] = new JTextField(6);
            this.add(directionTextField[yPos]);
            directionTextField[yPos].setBounds(new Rectangle(new Point(100, 100), directionTextField[yPos].getPreferredSize()));
            this.setVisible(true);
            directionTextField[yPos].setLocation(rinkWidthStart/10, yStepSize*(1 + yPos) + rinkHeight/20);
            
            directionTextField[yPos].getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    repaint();
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    repaint();
                }
                @Override
                public void insertUpdate(DocumentEvent e) {
                    repaint();
                }
            });
        }
    }
    
    private void paintArrow(Graphics g, Team team)
    {
        double arrowMaxLength = (int) rinkWidth/10;
        double maxForce = 50;
        directionValue = getDirectionValues(team);
        forceValue = getForceValues(team);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(Color.BLACK);
        for(int i = 0; i < team.getPlayers().size(); i++)
        {
            int xKoord = (int) convertObject2GraphicsX(team.getTeamMember(i).getCoord().getX(), rinkWidth)
                    + rinkWidthStart;
            
            int yKoord = (int) convertObject2GraphicsY(team.getTeamMember(i).getCoord().getY(), rinkHeight)
                    + rinkHeightStart;
            double forceRatio = forceValue[i]/maxForce;
            double length = (int) (forceRatio*arrowMaxLength);
            int angle = directionValue[i];
            int xSize = (int) (Math.cos(angle*Math.PI/180)*length);
            int ySize = (int) (Math.sin(angle*Math.PI/180)*length);
            g2d.drawLine(xKoord, yKoord, xKoord + xSize, yKoord + ySize);
        }
    }
    
    public int[] getDirectionValues(Team team)
    {
        int[] directionValues = new int[team.getPlayers().size()]; 
        for(int i = 0; i < team.getPlayers().size(); i++)
        {
            String str = directionTextField[i].getText().replaceAll("[^\\d-]", "");
            if(!str.equals("") && !str.equals("-")) {
                int direction = Integer.parseInt(str)%360;
                if(direction < 0)
                {
                    direction = 360 + direction;
                }
                directionValues[i] = direction;
            }
        }
        return directionValues;
    }
        
    public int[] getForceValues(Team team)
    {
        int[] forceValues = new int[team.getPlayers().size()]; 
        for(int i = 0; i < team.getPlayers().size(); i++)
        {
            String str = forceTextField[i].getText().replaceAll("[^\\d]", "");
            if(!str.equals("")) {
                forceValues[i] = Math.min(Integer.parseInt(str), 50);
            }
        }
        return forceValues;
    }
    
    public void resetTextField(Team team)
    {
        for(int i = 0; i < team.getPlayers().size(); i++)
        {
            forceTextField[i].setText("");
            directionTextField[i].setText("");
        }
    }

    public void setTeamA(Team teamA) {
        this.team0 = teamA;
    }

    public void setTeamB(Team teamB) {
        this.team1 = teamB;
    }

    public void setPuck(GraphicalCircle puck) {
        this.puck = puck;
    }
}
