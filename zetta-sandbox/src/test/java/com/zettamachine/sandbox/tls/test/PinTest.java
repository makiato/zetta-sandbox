package com.zettamachine.sandbox.tls.test;

import com.zettamachine.sandbox.tls.PinCheck;

public class PinTest {

	public static void main(String[] args) {
		PinCheck pinCheck = new PinCheck();
		//pinCheck.check("https://www.westpac.com.au");
		pinCheck.check("https://www.stgeorge.com.au");
	}

}
