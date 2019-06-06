package com.training.socketclient.tests;

import org.junit.Test;

import com.training.socket.client.SocketClient;

//@RunWith(JUnit4ClassRunner.class)
public class TestAddString {

	@Test
	public void testAddZero() {
		System.out.println(String.format("%08d", 999012));
	}

	@Test
	public void testSocketClient() throws Exception {
		SocketClient scClient = new SocketClient("localhost", 7701);
		String msg = "hahahahh;";
		System.out.println("接收到的返回内容为:" + scClient.sendMsg(msg));
	}

}
