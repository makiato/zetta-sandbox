package com.zettamachine.sandbox.socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketProcessor {
	Socket socket = null;
	
	public SocketProcessor(String host, int port) throws UnknownHostException, IOException {
		socket = new Socket(host, port);
	}
	
	public void close() throws IOException {
		socket.close();
	}
}
