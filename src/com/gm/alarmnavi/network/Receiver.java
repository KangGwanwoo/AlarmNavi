package com.gm.alarmnavi.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.*;

import com.gm.alarmnavi.callback.Callable;
import com.gm.alarmnavi.process.*;
import com.gm.alarmnavi.serializable.*;

public class Receiver implements Runnable {
	private Socket socket;
	private Callable callBackEvent;
	private FireData fireData;
	DbProcess dbProcess;

	public Receiver(Socket socket, Callable callBackEvent) {
		this.socket = socket;
		this.callBackEvent = callBackEvent;
		dbProcess = new DbProcess();
	}

	@Override
	public void run() {
		try {

			InputStream in = socket.getInputStream();
			BufferedInputStream bufFilterIn = new BufferedInputStream(in);
			ObjectInputStream objFilterIn = new ObjectInputStream(bufFilterIn);
			fireData = (FireData) objFilterIn.readObject(); // 소방
			
			
			dbProcess.connectDB(); //dpprocess class 에게 dbserver 접속 명령
			
			callBackEvent.callBackMethod(fireData); // 서버 로그에 fireData에 대한 정보를 남김.
			PushData orderPushData = new PushData(true);
			List<String> list = dbProcess.getAllAppid();
			orderPushData.setGroupAppID(list);
			System.out.println("소방차데이터 : "+fireData.getFirePoint());
			orderPushData.setLocation(fireData.getFirePoint());// 푸시 에이전트로 데이터를 보내기위한
															// 객체를 생성. 1이란 의미는
															// 프로토콜로써 푸시에이전트는
															// 1이라는 시그널을 받으면, 다른
															// 클라이언트들에게 위치정보
															// 요청을보낸다.
			callBackEvent.sendDataToPushAgent(orderPushData); // Server클래스에 구현된
															// sendDtataTopushAgent는
															// 접속된 pushAgent모두에게
															// 직렬화된 객체를 보낸다.
			Thread.sleep(5000);
			dbProcess.connectDB();
			List<String> routeInAppList = dbProcess.traceLocation(fireData); // fireData
																				// 안에
																				// route
																				// list를
																				// 활용하여
																				// dbprocess클래스에서
																				// 처리후
																				// 경로에
																				// 있는
																				// 클라이언트의
																				// appid만
																				// 뽑아내서
																				// list로
																				// 반환
			PushData selectedPushData = new PushData(false);
			selectedPushData.setGroupAppID(routeInAppList);
			selectedPushData.setLocation(fireData.getFirePoint());
			callBackEvent.sendDataToPushAgent(selectedPushData);

		} catch (IOException ioe) {
			System.out.println("S : Receive IO Error " + ioe.getMessage());
			ioe.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			System.out.println("S : Receive Class Error " + cnfe.getMessage());
			cnfe.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// private void socketClose(ObjectInputStream objFilterIn) {
	// try {
	// objFilterIn.close();
	// } catch (IOException ioe) {
	// System.out.println("socket closing io error " + ioe);
	// }
	// }
}
