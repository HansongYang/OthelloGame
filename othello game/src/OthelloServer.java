package server;

import java.io.*;
import java.util.*;
import java.net.*;

public class OthelloServer{
    public static Vector<OthelloServerThread> users = new Vector<>();
    static boolean active = false;
    static int numConnection = 1;
    static OthelloServerThread mtch;

    //The main method starts the server and wait for connection
	  public static void main(String[] args) throws IOException, InterruptedException{
        int port = -1;
        if(args.length > 0){
            try
            {
                port = Integer.parseInt(args[0]);
            }
                catch(NumberFormatException nfe)
            {
                port = -1;
            }
            if(port < 0 || port > 65535){
                port = -1;
            }
            if(port < 0){
                System.out.println("ERROR: Invalid port number: " + args[0]);
                port = 62000;
                System.out.println("Using default port: 62000");
            } else {
                System.out.println("Using input port: " + port);
            }
        } else {
            port = 62000;
            System.out.println("Using default port: 62000");
        }

		    ServerSocket server = new ServerSocket(port); //create the socket
        Socket socket = null;

        while (true){
        	try {
        		  active = true;
	        	  socket = server.accept();
              System.out.println("Inbound connection #" + numConnection);
              numConnection++;
	            mtch = new OthelloServerThread(socket);
	            users.add(mtch);
	            mtch.start();
        	} catch (IOException e) {
        	    if (server != null && !server.isClosed()) {
        	        try {
        	            server.close();
        	            break;
        	        } catch (IOException e1)
        	        {
        	            e1.printStackTrace(System.err);
        	        }
        	    }
        	}
        }
    }

    //This method tells the othello server threads to broadcast the messages to all the clients
    public static void broadcast(){
        mtch.broadcast();
    }

    //This method tells the othello server threads to send the who message to all the clients
    public static void who(){
        mtch.who();
    }

    //This method removes a specific client from the client list
    public static void remove(OthelloServerThread t){
        t.interrupt();
        users.remove(t);
    }
}
