package com.training.socket.client;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {

	public String ServerHost;
	public Integer ServerPort;
	public String encoding = "UTF-8";
	public Integer contentBytesUnifiedLength = 8;

	public SocketClient(String ServerHost, Integer ServerPort, String encoding, Integer contentBytesUnifiedLength) {
		this.ServerHost = ServerHost;
		this.ServerPort = ServerPort;
		this.encoding = encoding;
		this.contentBytesUnifiedLength = contentBytesUnifiedLength;

	}

	public SocketClient(String ServerHost, Integer ServerPort) {

		// TODO Auto-generated constructor stub
		this.ServerHost = ServerHost;
		this.ServerPort = ServerPort;
	}

	private boolean validServer(String ServerHost, Integer ServerPort) {
		return false;
	}

	public String getServerHost() {
		return ServerHost;
	}

	public void setServerHost(String serverHost) {
		ServerHost = serverHost;
	}

	public Integer getServerPort() {
		return ServerPort;
	}

	public void setServerPort(Integer serverPort) {
		ServerPort = serverPort;
	}

	public String lengthen(String msg) throws Exception  {
		int length = msg.getBytes(encoding).length;

		System.out.println("报文长度为：" + length);
		String format = "%0" + contentBytesUnifiedLength + "d";
		String lengthString = String.format(format, length);
		if (lengthString.length() > contentBytesUnifiedLength) {
			System.out.println("当前报文长度为" + lengthString.length() + ",已超过限定");
			throw new Exception("报文长度超过限制！");
		}
		return msg = lengthString + msg;

	}

	public String sendMsg(String msg) throws Exception {
		String ans = "";
		System.out.println("待写入的请求报文为：" + msg);

		msg = lengthen(msg);
		System.out.println("处理后的完整报文为：" + msg);

		
			// 创建客户端socket
			Socket socket = new Socket(ServerHost, ServerPort); // 客户端socket建立时要写入要连接的服务端地址
			// 获取输出流，向服务器端发送信息
			OutputStream os = socket.getOutputStream();
			BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(os, encoding));
			// 转换为字符打印流,向服务端发送信息
			// bfw.write("用户名、密码：admin admin"+count);
			// bfw.newLine();
			bfw.write(msg);
			bfw.flush();
			// count++;
			socket.shutdownOutput();

			// 通过socket输入流获取服务端响应
			InputStream is = socket.getInputStream();
			BufferedReader bf = new BufferedReader(new InputStreamReader(is, encoding));
		
			String tempString = null;
			while ((tempString = bf.readLine()) != null) {
				ans = ans + tempString+"\r\n";
				System.out.println("接收到的当前行响应信息为：" + tempString);
			}
			System.out.println("ans="+ans);
			socket.shutdownInput();

			// 关闭相关资源

			// pw.close();
			bfw.close();

			os.close();

			bf.close();
			is.close();
			socket.close();

		return ans = "".equals(ans) ? null : ans;

	}

	public static void main(String[] args) {
		int count = 0;
		while (count < 100) {
			try {
				// 创建客户端socket
				Socket socket = new Socket("localhost", 8888); // 客户端socket建立时要写入要连接的服务端地址
				// 获取输出流，向服务器端发送信息
				OutputStream os = socket.getOutputStream();
				BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(os));
				// 转换为字符打印流,向服务端发送信息
				bfw.write("用户名、密码：admin admin" + count);
				bfw.newLine();
				bfw.write("hello" + count);
				bfw.flush();
				count++;
				socket.shutdownOutput();

				// 通过socket输入流获取服务端响应
				InputStream is = socket.getInputStream();
				BufferedReader bf = new BufferedReader(new InputStreamReader(is, "utf-8"));
				String ans = null;
				while ((ans = bf.readLine()) != null) {
					System.out.println("接收到的响应信息为：" + ans);
				}
				socket.shutdownInput();
				System.out.println("ans="+ans);

				// 关闭相关资源

				// pw.close();
				bfw.close();

				os.close();

				bf.close();
				is.close();
				socket.close();

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
