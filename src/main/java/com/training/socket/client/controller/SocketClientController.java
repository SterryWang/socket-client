package com.training.socket.client.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.training.socket.client.SocketClient;
import com.training.socket.client.entity.SocketConfEssitials;

@Controller("/socketclient")
public class SocketClientController {

	@RequestMapping(value = "/socketclient", method = RequestMethod.GET)
	public String index(Model model) {

		return "socketclient";
	}

	@RequestMapping(value = "/socketclient.submit", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> doTransform(SocketConfEssitials socketConf, Model model) throws Exception {

		if (socketConf != null) {
			System.out.println(socketConf.toString());
			/*
			 * model.addAttribute("receivedContent", new
			 * SocketClient(socketConf.getServerHost(), socketConf.getServerPort(),
			 * socketConf.getEncoding(),
			 * socketConf.getLengthConsumeBytesNum()).sendMsg(socketConf.getOrigContent()));
			 */
		}
		Map<String, String> map = new HashMap<>();
		try {
			String msg=new SocketClient(socketConf.getServerHost(), socketConf.getServerPort(),
					socketConf.getEncoding(), socketConf.getLengthConsumeBytesNum()).sendMsg(socketConf.getOrigContent());
			map.put("msg",msg );
			map.put("else", "test");
			map.put("resultCode", "200");
			System.out.println("返回内容给前端！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("出现异常！");
			map.put("resultCode","500");
			map.put("error",e.toString());
		}
		return map;
	}

}
