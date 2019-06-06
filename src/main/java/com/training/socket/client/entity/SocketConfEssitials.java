package com.training.socket.client.entity;

public class SocketConfEssitials {
	String serverHost;
	Integer serverPort;
	String encoding;
	String origContent;
	Integer lengthConsumeBytesNum;

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getOrigContent() {
		return origContent;
	}

	public void setOrigContent(String origContent) {
		this.origContent = origContent;
	}

	public Integer getLengthConsumeBytesNum() {
		return lengthConsumeBytesNum;
	}

	public void setLengthConsumeBytesNum(Integer lengthConsumeBytesNum) {
		this.lengthConsumeBytesNum = lengthConsumeBytesNum;
	}

	@Override
	public String toString() {
		return "SocketConfEssitials [serverHost=" + serverHost + ", serverPort=" + serverPort + ", encoding=" + encoding
				+ ", origContent=" + origContent + ", lengthConsumeBytesNum=" + lengthConsumeBytesNum + "]";
	}

}