package hockey;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread
{
    private final ServerSocket serverSocket;
    private Socket server;
    
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
   
    public Server(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(30000);
        setupServer();
    }
    
    public void toogleServerReady() {
        serverReady = !serverReady;
        System.out.println("server ready status: " + serverReady);
        sendReadyStatus();
    }
    
    public boolean isServerReady()
    {
        return serverReady;
    }

    public void setServerForceAndDirectionVector(double[] serverForceVector, int[] serverDirectionVector) {
        this.serverForceVector = serverForceVector;
        this.serverDirectionVector = serverDirectionVector;
        dataIsSet = true;
    }

    public boolean isDataReady() {
        return dataReady;
    }

    public double[] getClientForceVector() {
        return clientForceVector;
    }

    public int[] getClientDirectionVector() {
        return clientDirectionVector;
    }
    
    public boolean isClientReady()
    {
        return clientReady;
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
            System.out.println("Received readyStatus: " + clientReady);
            while(!serverReady && clientReady)
            {}
            if(!clientReady)
            {
                break;
            }
            while(!dataIsSet)
            {
                System.out.println("Data in not ready");
            }
            sendPlayerValues();
            while(!hasReceivedData)
            {
                System.out.println("Trying to receive data");
                receivePlayerValues();
            }
            System.out.println("Has recevied data: " + clientForceVector[0] + " " + 
                    clientForceVector[1] + " " + clientForceVector[2]);
            serverReady = false;
            clientReady = false;
            hasReceivedData = false;
            dataIsSet = false;
            dataReady = true;
        }
    }
    
    private void setupServer()
    {
        while(true)
        {
            try
            {
                System.out.println("Waiting for client on port " +
                serverSocket.getLocalPort() + "...");
                server = serverSocket.accept();
                System.out.println("Just connected to "
                      + server.getRemoteSocketAddress());
                DataInputStream in =
                      new DataInputStream(server.getInputStream());
                System.out.println(in.readUTF());
                OutputStream outToClient = server.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToClient);
                out.writeUTF("Hello from "
                        + server.getLocalSocketAddress());
                break;
            }
            catch(SocketTimeoutException s)
            {
                System.out.println("Socket timed out!");
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
                new DataOutputStream(server.getOutputStream());
            out.writeBoolean(serverReady);
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
            DataOutputStream out =
                    new DataOutputStream(server.getOutputStream());
            for(int index = 0; index < serverForceVector.length; index++)
            {
                out.writeDouble(serverForceVector[index]);
                out.writeInt(serverDirectionVector[index]);
            }
            System.out.println("Sending data: " + serverForceVector[0] + " " + 
                    serverForceVector[1] + " " + serverForceVector[2]);
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
            InputStream inFromClient = server.getInputStream();
            DataInputStream in =
                    new DataInputStream(inFromClient);
            clientReady = in.readBoolean();
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
            InputStream inFromClient = server.getInputStream();
            for(int index = 0; index < teamSize; index++)
            {
                DataInputStream in =
                        new DataInputStream(inFromClient);
                clientForceVector[index] = in.readDouble();
                clientDirectionVector[index] = in.readInt();
            }
            hasReceivedData = true;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void closeServer()
    {
        try
        {
            server.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
