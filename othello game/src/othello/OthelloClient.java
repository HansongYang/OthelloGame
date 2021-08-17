package othello;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class OthelloClient {
	private String name = "";
	private String serverHostname = "";
	private int port;
	private Socket echoSocket = null;
	private OthelloNetworkController network;

	public OthelloClient(String host, int port, String name) {
			if(host.compareToIgnoreCase("localhost") == 0){
				  serverHostname = new String("127.0.0.1");
			} else {
					serverHostname = new String(host);
			}
			this.name = name;
			this.port = port;
	}

	//This method initializes the socket with input address and port.
	public boolean connectToServer(){
			try {
					echoSocket = new Socket(serverHostname, port);
			} catch (UnknownHostException e) {
					return false;
			} catch (IOException e) {
					return false;
			}
			try{
				network = new OthelloNetworkController(echoSocket);
				network.start();
				network.sendName(name);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
	}

	//Tell the newtork controller threads to disconnect from the server
	public void disconnect(){
		try{
			network.disconnect(name);
			network.interrupt();
			echoSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Tell the network controller threads to request a who message from the server
	public void who(){
			network.who(name);
	}

	//Tell the network controller threads to send a rename request to the server
	public void rename(String newname){
			String temp = name;
			name = newname;
			network.rename(temp, newname);
	}

	//Tell the network controller threads to request a broadcast message from the server
	public void message(String m){
			network.message(name, m);
	}

	//This method sends some messages to the UI
	public static void displayMessage(String result){
			OthelloViewController.displayMessage(result);
	}
}
