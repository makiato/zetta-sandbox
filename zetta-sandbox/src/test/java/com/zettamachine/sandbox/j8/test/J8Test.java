package com.zettamachine.sandbox.j8.test;

public class J8Test {

	public static void main(String[] args) {
		Runnable r = () -> {
			System.out.println("Runnable!");
		};
		r.run();
	}

}
