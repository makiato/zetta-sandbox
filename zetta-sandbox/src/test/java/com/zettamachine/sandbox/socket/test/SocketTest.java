package com.zettamachine.sandbox.socket.test;

import org.apache.log4j.Logger;

import com.zettamachine.sandbox.socket.SocketProcessor;

public class SocketTest {

	public static void main(String[] args) {
		Logger log = Logger.getLogger("com.zettamachine.sandbox.socket");
		try {
			log.debug("Trying to open a socket");
			SocketProcessor sp = new SocketProcessor("107.22.32.225", 41160);
			sp.close();
			log.debug("Socket Closed");
		} catch (Exception e) {
			log.error(e.getClass().getName(), e);
		}
	}

}
