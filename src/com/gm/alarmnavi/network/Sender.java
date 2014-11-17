package com.gm.alarmnavi.network;

import java.io.*;
import java.net.*;
import java.util.*;

public class Sender extends Thread {
	private Socket pushSocket;
	private Object pushData;
	private ServerSocket pushServerSocket;
	private final int serverPort = 3340;
	private HashMap hm;

	public Sender() {
		hm = new HashMap<>();
	}

	@Override
	public void run() {

		int hmkey = 0;
		// TODO Auto-generated method stub
		try {
			pushServerSocket = new ServerSocket(serverPort);

			while (true) {
				System.out
						.println("PS : PushAgent Connecting ... push agent를 기다리는 중..");
				hmkey++;
				pushSocket = pushServerSocket.accept();

				OutputStream out = pushSocket.getOutputStream();
				BufferedOutputStream bufFilterOut = new BufferedOutputStream(
						out);
				ObjectOutputStream objPushData = new ObjectOutputStream(
						bufFilterOut);
				hm.put(hmkey, objPushData);
			}
		} catch (IOException e) {
			System.out.println("S : Error " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void broadcast(Object pushData) throws IOException {
		synchronized (hm) {
			Collection collection = hm.values();
			Iterator iter = collection.iterator();
			while (iter.hasNext()) {
				ObjectOutputStream objectOutputStream = (ObjectOutputStream) iter
						.next();
				objectOutputStream.writeObject(pushData);
				objectOutputStream.flush();
				System.out.println("보낸데이터" + pushData);
			}
		}
	} // broadcast
}
