
package com.gm.alarmnavi.main;

import com.gm.alarmnavi.network.Server;

public class Main {
	public static void main(String[] args) {
		Server server = new Server();
		server.connectToClient();
	}
}
