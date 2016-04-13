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
    private boolean dataIsSet;
   
    private boolean serverReady;
    private double[] serverForceVector = new double[teamSize];
    private int[] serverDirectionVector = new int [teamSize];
    
    private boolean hasReceivedData = false;
    private boolean dataReady = false;

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
    
    public boolean isServerReady()
    {
        return serverReady;
    }

    public void toggleClientReady() {
        clientReady = !clientReady;
        sendReadyStatus();
    }
    
    public boolean isClientReady()
    {
        return clientReady;
    }

    public void setClientForceAndDirectionVector(double[] clientForceVector, int[] clientDirectionVector) {
        this.clientForceVector = clientForceVector;
        this.clientDirectionVector = clientDirectionVector;
        dataIsSet = true;
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
            receiveReadyStatus();
            System.out.println("Received readyStatus: " + serverReady);
            while(!clientReady && serverReady)
            {}
            if(!serverReady)
            {
                break;
            }
            while(!dataIsSet)
            {
                System.out.println("Data is not set");
            }
            System.out.println("trying to receive data");
            sendPlayerValues();
            while(!hasReceivedData)
            {
                receivePlayerValues();
            }
            System.out.println("Has recevied data: " + serverForceVector[0] + " " + 
                    serverForceVector[1] + " " + serverForceVector[2]);
            clientReady = false;
            serverReady = false;
            hasReceivedData = false;
            dataIsSet = false;
            dataReady = true;
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
            System.out.println("Sending data: " + clientForceVector[0] + " " + 
                clientForceVector[1] + " " + clientForceVector[2]);
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

