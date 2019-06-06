package com.training.socketclient.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MockSocketServer {

	public static void main(String[] args) throws IOException {
		// 建立服务端socket
		ServerSocket serverSocket = new ServerSocket(7701);
		// 服务监听
		System.err.println("服务器即将启动。。。。");
		// 使用jdk的线程池
		ExecutorService threadPool = Executors.newFixedThreadPool(10);
		long count = 0;
		while (true) {
			Socket socket = serverSocket.accept();

			count++;
			System.out.println("已收到第" + count + "个请求");
			ServerThread serverThread = new MockSocketServer().new ServerThread(socket, count);
			threadPool.submit(serverThread);

		}

	}

	class ServerThread extends Thread {

		private Socket socket = null;
		private long count = 0;

		public ServerThread(Socket socket, Long count) {
			this.socket = socket;
			this.count = count;

		}

		@Override
		public void run() {
			System.out.println("[" + this.getName() + "]" + "已开始处理第" + count + "个请求！");
			System.out.println("线程组名称为：" + this.getThreadGroup().getName());
			System.out.println("线程组中当前活动线程有" + this.activeCount() + "个");
			// 获取输入流
			InputStream iStream = null;
			// 转换成字符流
			BufferedReader bf = null;
			// 响应客户端的请求
			OutputStream os = null;
			PrintWriter pw = null;
			try {
				iStream = socket.getInputStream();
				bf = new BufferedReader(new InputStreamReader(iStream, "utf-8"));
				// 读取信息
				String data = null;
				while ((data = bf.readLine()) != null) {
					System.out.println("接收到的信息为：" + data);

				}
				socket.shutdownInput();// 注意，这个不是重复关闭，它和isstream.close是两回事，请看API文档

				os = socket.getOutputStream();

				pw = new PrintWriter(new OutputStreamWriter(os, "utf-8"));

				pw.write("认证成功！\r\n 欢迎你！");
				pw.flush();
				socket.shutdownOutput();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					// 响应完毕后关闭相关资源
					if (pw != null)
						pw.close();
					if (os != null)
						os.close();
					if (bf != null)
						bf.close();
					if (iStream != null)
						iStream.close();
					socket.close();
					System.out.println("第" + count + "个请求已处理完毕");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

}
