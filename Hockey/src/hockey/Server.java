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
   
    private boolean serverReady;
    private double[] serverForceVector = new double[teamSize];
    private int[] serverDirectionVector = new int [teamSize];
   
    private final int sleepTime = 1000;
    
    private boolean hasReceivedData = false;
    private boolean dataReady = false;
   
    public Server(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(30000);
        setupServer();
    }
    
    public void setServerReady(boolean serverReady) {
        this.serverReady = serverReady;
    }

    public void setServerForceVector(double[] serverForceVector) {
        this.serverForceVector = serverForceVector;
    }

    public void setServerDirectionVector(int[] serverDirectionVector) {
        this.serverDirectionVector = serverDirectionVector;
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
    
    public void resetDataReady()
    {
        dataReady = false;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            if(serverReady)
            {
                sendReadyStatus();
                while(!clientReady)
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
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
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
