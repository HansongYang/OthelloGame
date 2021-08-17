package othello;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class OthelloNetworkController extends Thread {
  PrintWriter out = null; //output stream
	BufferedReader in = null; //input stream

  public OthelloNetworkController(Socket echoSocket){
      try{
        out = new PrintWriter(echoSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
      } catch (Exception e) {
        e.printStackTrace();
      }
  }

  //This method keeps listening messages from the server
  public void run(){
    try{
      while(true) {
        String ss = in.readLine();
        if(ss == null){
          continue;
        }
        displayMessage(ss);
      }
    } catch(IOException e) {
        System.out.println("Server closed. Please restart the server.");
    }
  }

  //This method sends the name of a client to the server
  public void sendName(String name){
      out.println(name);
  }

  //This method requests to disconnet from the server
  public void disconnect(String name){
      try{
          out.println(name + " disconnect");
          in.close();
          out.close();
      } catch (IOException e) {
    			e.printStackTrace();
  		}
  }

  //This method requests the who message from the server
  public void who(String name){
      out.println(name + " who");
  }

  //This method sends the incoming messages to the client
  public void displayMessage(String result){
      OthelloClient.displayMessage(result);
  }

  //This method sends the rename request to the server
  public void rename(String name, String newname){
      out.println(name + newname+ "name");
  }

  //This method requests the broadcast message from the server
  public void message(String name, String m){
    try{
      out.println(name + m + "message");
    } catch (Exception e) {
        e.printStackTrace();
    }
  }
}
