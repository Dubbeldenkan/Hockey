package hockey;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Thread{
    
    private Socket client;
    private int port;
    private String serverName;
    
    private boolean clientReady;
    private double[] clientForceVector;
    private int[] clientDirectionVector;
   
    private boolean serverReady;
    private double[] serverForceVector;
    private int[] serverDirectionVector;
    private boolean connected = false;
   
    private int sleepTime = 1000;

    public Client(String serverName, int port)
    {
        this.serverName = serverName;
        this.port = port;
        setupClient();
    }

    public boolean isServerReady() {
        return serverReady;
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
    
    public void run()
    {
        while(true)
        {
            if(clientReady)
            {
                sendReadyStatus();
                receiveReadyStatus();
                if(serverReady)
                {
                    //sendPlayerValues();
                    //receivePlayerValues();
                }
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
                client.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
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
}

