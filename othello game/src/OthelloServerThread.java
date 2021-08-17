package server;

import java.io.*;
import java.util.*;
import java.net.*;

public class OthelloServerThread extends Thread{
    Scanner scn = new Scanner(System.in);
    String name;
    InputStream dis;
    public OutputStream dos;
    Socket s;

    public OthelloServerThread(Socket s) throws IOException {
        if(s != null) {
	        this.s = s;
	        this.dos = s.getOutputStream();
	        this.dis = s.getInputStream();
        }
    }

    @Override
    public void run() {
        String request  = "";
        boolean firstMessage = false;

        try{
        	 BufferedReader br = new BufferedReader(new InputStreamReader(dis));
        	 while ((request = br.readLine()) != null){
              if(!firstMessage){
                 name = request;
        		     System.out.println(request + " has connected.");
                 firstMessage = true;
                 if(OthelloServer.users.size() > 1){
                     OthelloServer.broadcast();
                     OthelloServer.who();
                 }
              }
              for (OthelloServerThread thread : OthelloServer.users){
                  if(request.endsWith("disconnect") && request.startsWith(thread.name)){
                    System.out.println(thread.name + " has disconnected.");
                    OthelloServer.remove(thread);
                    return;
                  }
                  if(request.endsWith("who") && request.startsWith(thread.name)){
                      who(thread);
                  }
                  if(request.endsWith("name") && request.startsWith(thread.name)){
                      rename(thread, request.substring(thread.name.length(), request.length()-4));
                  }
                  if(request.endsWith("message") && request.startsWith(thread.name)){
                      broadcast(thread, request.substring(thread.name.length(), request.length()-7));
                  }
              }
        	}
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
	        try{
	            this.dis.close();
	            this.dos.close();
	            s.close();
	        }catch(IOException e){
	            e.printStackTrace();
	        }
        }
    }

    //This method broadcasts the messages to all the clients
    public void broadcast(){
      try{
        for (OthelloServerThread thread : OthelloServer.users){
            if(!thread.name.equals(name)){
                thread.dos.write(("broadcastSERVER: " + name + " has joined the server.\n").getBytes());
            }
        }
      } catch (IOException e) {
          if(e instanceof SocketException){
              System.out.println("A client left the game without proper disconnection.");
              for (OthelloServerThread thread : OthelloServer.users){
                  if(!thread.isAlive()){
                      OthelloServer.remove(thread);
                  }
              }
          }
      }
    }

    //This method broadcasts the specific messages to all the clients except the issued client
    public void broadcast(OthelloServerThread t, String s){
      try{
        for (OthelloServerThread thread : OthelloServer.users){
          if(thread.name.equals(t.name)){
            continue;
          }

          thread.dos.write(("broadcast"+t.name+ ": " + s + "\n").getBytes());
        }
      } catch (IOException e) {
        if(e instanceof SocketException){
            System.out.println("A client left the game without proper disconnection.");
            for (OthelloServerThread thread : OthelloServer.users){
                if(!thread.isAlive()){
                    OthelloServer.remove(thread);
                }
            }
        }
      }
    }

    //This method broadcasts the who message to all the clients
    public void who(){
      try{
        for (OthelloServerThread thread : OthelloServer.users){
          if(thread.name.equals(name)){
            continue;
          }

          String names = "";
          for (OthelloServerThread t : OthelloServer.users){
              names += t.name + " ";
          }
          thread.dos.write(("who"+names + "\n").getBytes());
        }
      } catch (IOException e) {
        if(e instanceof SocketException){
            System.out.println("A client left the game without proper disconnection.");
            for (OthelloServerThread thread : OthelloServer.users){
                if(!thread.isAlive()){
                    OthelloServer.remove(thread);
                }
            }
        }
      }
    }

    //This method sends the who message to all the clients
    public void who(OthelloServerThread t){
      try{
        String names = "";
        for (OthelloServerThread ost : OthelloServer.users){
            names += (ost.name + " ");
        }
        t.dos.write(("who"+ names +"\n").getBytes());
      } catch (IOException e) {
        if(e instanceof SocketException){
            System.out.println(t.name + " left the game without proper disconnection.");
            for (OthelloServerThread thread : OthelloServer.users){
                if(!thread.isAlive()){
                    OthelloServer.remove(thread);
                }
            }
        }
      }
    }

    //This method broadcasts the rename message to all the clients except the issued client
    public void rename(OthelloServerThread t, String newName){
      try{
        String oldName = t.name;
        t.dos.write(("nameSERVER: Name changed to " + newName+"\n").getBytes());
        for (OthelloServerThread thread : OthelloServer.users){
          if(thread.name.equals(t.name)){
            thread.name = newName;
            continue;
          }
          thread.dos.write(("nameSERVER: " + oldName + " has changed to " + newName+"\n").getBytes());
        }
        System.out.println(oldName + " renamed to " + newName + ".");
      }catch (IOException e) {
        if(e instanceof SocketException){
            System.out.println("A client left the game without proper disconnection.");
            for (OthelloServerThread thread : OthelloServer.users){
                if(!thread.isAlive()){
                    OthelloServer.remove(thread);
                }
            }
        }
      }
    }
}
