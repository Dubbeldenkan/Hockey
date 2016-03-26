package hockey;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Thread{
    
    private Socket client;
    private final int port;
    private final String serverName;
    
    private final int teamSize = 3;
   
    private boolean clientReady;
    private double[] clientForceVector = new double[teamSize];
    private int[] clientDirectionVector = new int[teamSize];
   
    private boolean serverReady;
    private double[] serverForceVector = new double[teamSize];
    private int[] serverDirectionVector = new int [teamSize];
    
    private boolean hasReceivedData = false;
    private boolean dataReady = false;
   
    private final int sleepTime = 1000;

    public Client(String serverName, int port)
    {
        this.serverName = serverName;
        this.port = port;
        setupClient();
    }

    public boolean isDataReady() {
        return dataReady;
    }

    public double[] getServerForceVector() {
        return serverForceVector;
    }

    public int[] getServerDirectionVector() {
        return serverDirectionVector;
    }

    public void setClientReady(boolean clientReady) {
        this.clientReady = clientReady;
    }

    public void setClientForceVector(double[] clientForceVector) {
        this.clientForceVector = clientForceVector;
    }

    public void setClientDirectionVector(int[] clientDirectionVector) {
        this.clientDirectionVector = clientDirectionVector;
    }
    
    public void resetDataReady()
    {
        dataReady = false;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            if(clientReady)
            {
                sendReadyStatus();
                while(!serverReady)
                {
                    receiveReadyStatus();
                }
                sendPlayerValues();
                while(!hasReceivedData)
                {
                    receivePlayerValues();
                }
                serverReady = false;
                clientReady = false;
                hasReceivedData = false;
                dataReady = true;
            }                
            
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void setupClient()
    {
        while(true)
        {
            try
            {
                System.out.println("Connecting to " + serverName +
                        " on port " + port);
                client = new Socket(serverName, port);
                System.out.println("Just connected to " 
                     + client.getRemoteSocketAddress());
                OutputStream outToServer = client.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);
                out.writeUTF("Hello from "
                        + client.getLocalSocketAddress());
                InputStream inFromServer = client.getInputStream();
                DataInputStream in =
                        new DataInputStream(inFromServer);
                System.out.println("Server says " + in.readUTF());
                break;
            }
            catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
    }
   
    public void sendReadyStatus()
    {
        try
        {
            DataOutputStream out =
                new DataOutputStream(client.getOutputStream());
            out.writeBoolean(clientReady);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void sendPlayerValues(double[] forceVector, int[] directionVector)
    {
        try
        {
            DataOutputStream out =
                new DataOutputStream(client.getOutputStream());
            for(int index = 0; index < forceVector.length; index++)
            {
                out.writeDouble(forceVector[index]);
                out.writeInt(directionVector[index]);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void receiveReadyStatus()
    {
        try
        {
            InputStream inFromServer = client.getInputStream();
            DataInputStream in =
                    new DataInputStream(inFromServer);
            serverReady = in.readBoolean();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void sendPlayerValues()
    {
        try
        {
            for(int index = 0; index < teamSize; index++)
            {
                DataOutputStream out =
                    new DataOutputStream(client.getOutputStream());
                out.writeDouble(clientForceVector[index]);
                out.writeInt(clientDirectionVector[index]);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void receivePlayerValues()
    {
        try
        {
            InputStream inFromServer = client.getInputStream();
            for(int index = 0; index < teamSize; index++)
            {
                DataInputStream in =
                        new DataInputStream(inFromServer);
                serverForceVector[index] = in.readDouble();
                serverDirectionVector[index] = in.readInt();
            }
            hasReceivedData = true;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}

