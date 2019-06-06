# socket-client
a training project aimed at testing socket comm.

## 2019.6.6 首次提交
### 一、写在前面的话
   &nbsp;&nbsp;这是一个极度简陋和丑陋的java-web工具，但确实基本功能都在的（后端的功劳）。然而就是这个简陋的版本，只有一个html页面，却让我在前端上耗费了大量的时间和精力，两天的时间才拿出来。我知道，对于成熟的前端工程师来讲，这个工具前端的工作量，包括开发和调试，可能只需要2个小时的时间。这个过程其实暴露出了我自己这段时间的工作状态，代码量太少，离一个熟练的开发工程师还太远，前端更是零基础，前端开发全靠百度+拼凑+耗费时间的瞎蒙。借此机会，我希望自己可以知耻而后勇，鞭策自己，永远不要在重复的工作中停滞不前，要鞭策自己，实现每一个目标，用行动，而不是言语，来证明自己。

---

### 二、知识点
&nbsp;&nbsp;这个简单的小工具，编写过程，其实涉及到的知识点还是蛮多的。主要有以下内容：

- SPRING MVC.使用的是spring boot的工程骨架，WEB的也是基于SPRING MVC。很多代码如@Controller这种，一看就是基于SPRING MVC的框架。
- thymeleaf .这个工具现在用的不多了，我这里不细说了，简单说，Thymeleaf是一个跟Velocity、FreeMarker 类似的模板引擎，它可以完全替代 JSP。
- socket服务端和客户端的后端代码，包括定义输出流或者输入流的编码类型，这在本工具中至关重要。
- 前端和后端的字符编码问题
- 前后端之间的数据交互问题
- 前端开发基础：html,css,js,ajax,jquery,dom,他们的语法，标记,特性，彼此之间的数据交互，他们和后端之间的数据交互。这些东西都是值得学习的。
### 三、坑
&nbsp;&nbsp;说说过程中遇到的坑。
1. 字符编码问题。首先，信息需要从前端页面读取，然后传输到后端，比如一个输入框内，敲进去信息，会转化为字节流传输给后端，这里
```
graph LR
前端页面--字节流-->后端
```

这里需要一次字符编码，这个需要在你的html页面中设定：
```
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
...
<head>
<body>
</body>
<html>
```
如上方代码所示，meta中有设置。当然，我在前端页面中设定了字符编码为UTF-8，后端获取到字节流以后，进行解码，同样也需要使用UTF-8进行解码，这样就可以避免出现乱码了。
&nbsp;&nbsp;按照这个工具的流程，后端获取到前端传过来的socket请求报文后，将会给报文加长度标识字符串，然后把报文发送到socket服务端去，这里又涉及到一次编码：

```
mermaid
graph LR
socket客户端--字节流-->socket服务端
```
这一次能，我需要和socket约定好编码格式，举例：socket服务端和我socket客户端约定好，请求报文和返回报文都使用GBK编码，那么，客户端需要将报文写入socket output流的时候，使用GBK编码。
  很快，客户端就收到了服务端的返回报文了，要读取报文，还是需要一次编码：
  ```
  graph LR
  socket服务端--字节流-->socket客户端
  
  ```
  返回报文约定好使用GBK编码的，所以，客户端解码的时候还是要使用GBK进行解码才能正确读取返回报文的字符串。
  
  最后，客户端需要把返回报文传给前端：
  
  ```
  graph LR
  socket客户端--字节流-->前端页面
  
  ```
  客户端要把字符进行编码，因为前端HTML页面已经写定了使用UTF-8编码，为了让前端正确呈现返回内容，socket客户端必须使用UTF-8进行编码。
  
  当然，在本案例中，
  
```
sequenceDiagram
socket客户端->>前端页面: 字节流
前端页面->>socket客户端: 字节流
```
由于使用SPRING MVC的框架，封装后，我们很难看到，上图中的展现出来的编码和解码的过程。但如果在我的另一个工程中，这个过程就体现的很明显了，在那个工程中，有一个Filter就是用来统一前后端之间数据传输的字符编码问题了。

2.**前后端的数据传输问题**

&nbsp;&nbsp;这个问题一直是困扰我许久的问题。当我在demo-servlet工程中接触了servlet后，我明白在最基础的servlet这个层面上，当然还是没有看到MVC这个理念的，前端到后端的post请求时，都是通过httprequest.getparameter把数据取出来的，而后端到前端传数据，更粗暴，直接通过httpresponse的writer写进去一个html，要更深入的从底层理解这一层，我们恐怕要去好好研究servlet-api的源码了。这里我们不深入探讨了。至少，在我们目前的这个工程中，SPRIGN MVC经过封装的处理方式更多的让人眼花缭乱。
对于直接访问页面的情形，后端只要通过Controller返回一个视图就好；

对于前端需要传输部分数据给后端，并且从后端获取更新数据的情形，前端使用AJAX，后端则需要@RequestMapping配合使用@ResponseBody，这样就避免MVC 的视图解析，关于这个问题的解释，可以参考：
[link](https://www.cnblogs.com/daimajun/p/7152970.html)
贴一下部分代码好了，

**前端**：
```
function submitform1() {
		//alert("即将进入ajax调用");//调试时使用
		
				$.ajax({
					//几个参数需要注意一下
					async : true,//默认就是true代表异步
					//type: "POST",//方法类型
					dataType : "json",//预期服务器返回的数据类型
					url : "/socketclient.submit",//url
					data : $('#form1').serialize(),
					success : function(result) {
						console.log(result);//打印服务端返回的数据(调试用)

						if (result.resultCode == 200) {
							alert("SUCCESS");
							//document.getElementById('returnMsg').value=result.msg;//此命令经测试无效
							//document.getElementById('returnMsg').innerHTML = result.msg;//此命令后解析返回报文中的标签，无法原样输出
							document.getElementById('returnMsg').textContent = result.msg;
						}
						if (result.resultCode == 500) {
							alert("异常"+result.error);

						}

						;
					},
					error : function() {
						alert("异常！");
					}
				});
	};
	
```
这里使用了JQUERY库，因此需要先在头里引入JQUERY
```
<script th:src="@{jquery-3.4.1.js}" type="text/javascript"></script>
```
当然这里是thymeleaf的写法不是标准写法，标准写法是在
```
src=[js资源路径]
```
一般使用相对路径(相对于这个html文件的路径)

**后端部分代码**：
```
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
	
```
后端返回数据采用的是map，前端ajax中的result其实就是这个对象，当然ajax已经把数据转换成json了，这个在使用chrome浏览器进行调试时，可以通过console.log明显的看到具体内容。

3.**前端页面编写中的一些坑**

其实还是挺多的，我一一列举把：

1).js函数如果前面加载失败，后面的不会再继续加载了，所以经常会导致onclick时间中出现reference fail的情况，函数调用不到，先检查函数名是不是不对，然后看看是不是js函数加载有问题，灵活借用chrome来定位错误。
2）ajax的type属性，默认GET，POST不是所有的浏览器都支持，所以不要用POST，后端对应的RequestMapping，也同样需要使用GET来响应。

3）ajax把从后端获取的内容显示到前端，请使用把结果作为普通文本原样：
```
document.getElementById('returnMsg').textContent = [返回结果];
```
而不要使用
```
document.getElementById('returnMsg').innerHTML = result.msg;//此命令后解析返回报文中的标签，无法原样输出
```
4）返回信息可以同样使用<textArea>标签来存放，便于复制和换行， 	设置属性为readonly即可

5）学会使用js中的alert和console.log进行前端调试

6）在类上使用@Controller并标明了路径，在类的方法里写@RequestMapping在标明路径时，并不会使用相对于@Controller中标明的相对路径，因此你必须在@RequestMapping中从根路径接着写，不知道是不是我自己没整名表，或许@RestController不一样？我有点不明白。
今天就先写到这里吧。


  
  
  

