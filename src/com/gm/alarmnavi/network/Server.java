package com.gm.alarmnavi.network;

import java.io.*;
import java.net.*;

import com.gm.alarmnavi.callback.*;
import com.gm.alarmnavi.serializable.*;

public class Server implements Callable {

	private final int serverPort = 3337;

	private Socket appSocket;
	private Object data;
	private ServerSocket serverSocket;
	private Sender sender;

	public void connectToClient() {

		sender = new Sender();
		sender.start();
		
		
		try {
			serverSocket = new ServerSocket(serverPort); 
			while (true) {
				System.out.println("S : Connecting ...소방차를 기다리는중...");

				appSocket = serverSocket.accept();

				System.out.println("S : "
						+ appSocket.getInetAddress().getHostAddress());

				new Receiver(appSocket, this).run();
			}
		} catch (IOException e) {
			System.out.println("S : Error " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void callBackMethod(Object obj) {
		FireData fireData = (FireData) obj;
		System.out.println("S : (distance)" + fireData.getDistacne());
		System.out.println("S : (duration)" + fireData.getDuration());
		System.out.println("S : (routePointList)"
				+ fireData.getRoutePointList().size());
	}

	public void sendDataToPushAgent(Object pushData) {
		try {
			sender.broadcast(pushData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e + "push server is broken");
		}
	}
}
