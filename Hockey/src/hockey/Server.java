package hockey;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread
{
    private ServerSocket serverSocket;
    private Socket server;
   
    private boolean clientReady;
    private double[] clientForceVector;
    private int[] clientDirectionVector;
   
    private boolean serverReady;
    private double[] serverForceVector;
    private int[] serverDirectionVector;
    private boolean connected = false;
   
    private int sleepTime = 1000;
   
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

    public boolean isClientReady() {
        return clientReady;
    }

    public double[] getClientForceVector() {
        return clientForceVector;
    }

    public int[] getClientDirectionVector() {
        return clientDirectionVector;
    }
    
    public void run()
    {
        while(true)
        {
            if(serverReady)
            {
                sendReadyStatus();
                receiveReadyStatus();
                if(clientReady)
                {
                    //sendPlayerValues();
                    //receivePlayerValues();
                }
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
            for(int index = 0; index < serverForceVector.length; index++)
            {
                DataOutputStream out =
                    new DataOutputStream(server.getOutputStream());
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
