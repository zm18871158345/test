package org.xmyd.app.backstage.controller.phone;
/**
 * 联通用户验证
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xmyd.app.backstage.entity.XmydCallhistory;
import org.xmyd.app.backstage.ourtools.ResponseMessage;
import org.xmyd.app.backstage.ourtools.ZhangMing;
import org.xmyd.app.backstage.service.loan.XmydLoanService;
import org.xmyd.app.backstage.service.other.XmydCallhistoryService;

@Controller
@RequestMapping("/info")
public class LoginChinaUnicom {
	
	
	
	
	@Autowired
	private HttpSession chsisession;
	@Autowired
	private XmydLoanService loanService;
	
	@Autowired
	private HttpSession loansession;
		
		@Autowired
		private XmydCallhistoryService xmydCallhistoryService;
		
		@RequestMapping("/loginunicom.do")
		@ResponseBody
		public ResponseMessage checkunicom(HttpServletRequest request,String identity_id,String real_name,String phone_number,String phone_pwd) throws ParseException, IOException{
			 ResponseMessage result=new ResponseMessage();

			 String name=phone_number;
			 System.out.println(name);
			 String pwd=phone_pwd;
			 System.out.println(pwd);
			 
			 String loan_ip = request.getHeader("x-forwarded-for"); 
		       if(loan_ip == null || loan_ip.length() == 0 || "unknown".equalsIgnoreCase(loan_ip)) { 
		    	   loan_ip = request.getHeader("Proxy-Client-IP"); 
		       } 
		       if(loan_ip == null || loan_ip.length() == 0 || "unknown".equalsIgnoreCase(loan_ip)) { 
		    	   loan_ip = request.getHeader("WL-Proxy-Client-IP"); 
		       } 
		       if(loan_ip == null || loan_ip.length() == 0 || "unknown".equalsIgnoreCase(loan_ip)) { 
		    	   loan_ip= request.getRemoteAddr(); 
		       }


	      String e3Url = "http://iservice.10010.com/e3/static/query/searchPerInfoUser/?_=";
	   

	      CookieStore cookieStore = new BasicCookieStore();
	      CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
	      
      
	      
	      BasicClientCookie cookie = new BasicClientCookie("mallcity","11|110");
	      cookie.setDomain("10010.com");
	      cookie.setPath("/");
	      cookieStore.addCookie(cookie);
	      
//	      HttpPost post = new HttpPost(e3Url+new Date().getTime());
//	      post.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//	      post.setHeader("Accept-Encoding", "gzip, deflate");
//	      post.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,en-US;q=0.4,zh-TW;q=0.2");
//	      post.setHeader("Cache-Control", "max-age=0");
//	      post.setHeader("Connection", "keep-alive");
////	      post.setHeader("Content-Length", "0");
//	      post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
////	      post.setHeader("Cookie", "mallcity=11|110");
//	      post.setHeader("Host", "iservice.10010.com");
//	      post.setHeader("Origin", "http://iservice.10010.com");
//	      post.setHeader("Referer", "http://iservice.10010.com/e4/skip.html");
//	      post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36");
//	      post.setHeader("X-Requested-With", "XMLHttpRequest");
	      
//	      HttpResponse e3Response = httpclient.execute(post);
	      List<String> cookieList = new ArrayList<String>();
//	      for (Header head : e3Response.getAllHeaders()) {
//	          System.out.println(head+" name:"+head.getName()+" value:"+head.getValue());
//	          if("Set-Cookie".equals(head.getName())){
//	          	String value = head.getValue();
//	          	cookieList.add(value.split(";")[0]);
//	          }
//	      }
	      
	      System.out.println("Post logon cookies:");
	      List<Cookie> cookies = cookieStore.getCookies();
	      if (cookies.isEmpty()) {
	          System.out.println("None");
	      } else {
	          for (int i = 0; i < cookies.size(); i++) {
	              System.out.println("- " + cookies.get(i).toString());
	          }
	      }
	      cookieList.add("mallcity=11|110");//北京
	      //此处可能有变化，官网在改变页面
//	      String piw = "{\"login_name\":\"186****1082\",\"nickName\":\"张明\",\"rme\":{\"ac\":\"\",\"at\":\"\",\"pt\":\"01\",\"u\":\"18672421082\"},\"verifyState\":\"\"}";
//	      cookieList.add("piw="+URLEncoder.encode(piw, "utf-8"));
	      
	      //2.登录，服务器端添加session状态
	      
	      
	      
	    String url = "https://uac.10010.com/portal/Service/MallLogin?callback=jQuery17202691898950318097_1403425938090&redirectURL=http%3A%2F%2Fiservice.10010.com%2Fe4%2Fskip.html%3FmenuCode%3D000100020001%26menuCode%3D000100020001&userName=" + name + "&password=" + pwd + "&pwdType=01&productType=01&redirectType=01&rememberMe=1";
	      
	      
	      
	        HttpGet httpGet = new HttpGet(url);
			HttpResponse loginResponse = httpclient.execute(httpGet);
			int statusCode = loginResponse.getStatusLine().getStatusCode();
				
			boolean ok=false;
			if ( statusCode== 200) {
	          for (Header head : loginResponse.getAllHeaders()) {
	              System.out.println(head);
	              if("Set-Cookie".equals(head.getName())){
	              	String value = head.getValue();
	              	cookieList.add(value.split(";")[0]);
	              }
	          }	          
			}
			
			
			HttpEntity loginEntitys = loginResponse.getEntity();
	        String loginEntityContents = EntityUtils.toString(loginEntitys);
	       
	        //System.out.println("-2 登录状态:" + loginEntityContents);
			
			int s_resultcode=loginEntityContents.indexOf("resultCode")+12;
			int e_resultcode=loginEntityContents.indexOf("\"",s_resultcode+2);
			String resultStr=loginEntityContents.substring(s_resultcode, e_resultcode);
			System.out.println("天王盖地虎:"+resultStr);
			if(!resultStr.equals("0000")){
				result.setCode("1");
				result.setMessage("您输入的服务密码错误！请重新输入！");
				return result;
			}
			
			cookies = cookieStore.getCookies();
	      if (cookies.isEmpty()) {
	          System.out.println("None");
	      } else {
	          for (int i = 0; i < cookies.size(); i++) {
	              System.out.println("- " + cookies.get(i).toString());
	          }
	      }
	      
	      //2.1 test common
	      url = "http://iservice.10010.com/e3/static/common/l?_="+new Date().getTime();
	      HttpPost post21 = new HttpPost(url+new Date().getTime());
	      post21.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//	      post.setHeader("Content-Length", "0");
	      post21.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//	      post.setHeader("Cookie", "mallcity=11|110; route=dbf9f88e9ac80f498ca24d92e525186b; e3=v9pJY2yRGq8R3sLxRH2nyt2vch1CkG73qvY4hQhs8g6CrH6n8wz1!1562354273; piw=%7B%22login_name%22%3A%22185****2012%22%2C%22nickName%22%3A%22%E5%BE%90%E5%8D%93%E6%95%8F%22%2C%22rme%22%3A%7B%22ac%22%3A%22%22%2C%22at%22%3A%22%22%2C%22pt%22%3A%2201%22%2C%22u%22%3A%2218571652012%22%7D%2C%22verifyState%22%3A%22%22%7D; JUT=6KH2RV/gOKXSHLnY4Ouobg6RnSytn0q1yseSAw6MQAtZ7ZdErD5sGZtkczw5CtPj+Su+VleXj539p5jCap/Q/pP9HFvwxeIXN1n3XZ/Piogj4bXyW1kf59uI/5vXPNjLXoM3848pvhMnsx/QzxljPJKKDOmibGORgfE/H0gNdntgKyJnclUSU5LetnEVp22tr9hf2lmbCd6cn8RfTOBrkcolYxE5qFalMbuRjdtDFrPZU7AH4vkiHBYmHHmy4sVrLnxJYO15IpZH0Q/lfCqM5z038aNxiSUbaS9QbQYehEe4cWGwyWFWBtD3lroEtIvYst61hrVSYNLUWfYtQIF2pvdKRxXIC4b4FFgF0v178+MlM0XgZCAudzkdA2bE6Bopp/GNIc2J4/yzbNRpoizurbRu17WYMwRv0Ktgs/HqYtOv5IgUSDBZW39uvLrio2hNNSrTfVUhg9DB9TVnzdZA1Q7+s0xQJueIqnhY1kp8qA91ler/6zYw3ieOj0QsSutbZECyJHPgqteI8hWgJc0bnkw/jD79ehUaMhxjXPypc3g=l30ns7jVaxzyEML6xjh9AA==; _uop_id=addce794431aef182c1d71e9fb38a14c");
//	      post.setHeader("Cookie",StringUtils.join(cookieList.toArray(),";"));
	      post21.setHeader("Origin", "http://iservice.10010.com");
	      post21.setHeader("Referer", "http://iservice.10010.com/e4/");
	      post21.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36");
	      post21.setHeader("X-Requested-With", "XMLHttpRequest");
	      HttpResponse e3Response21 = httpclient.execute(post21);
			 statusCode = e3Response21.getStatusLine().getStatusCode();
			if ( statusCode== 200) {
	          HttpEntity loginEntity = e3Response21.getEntity();
	          String loginEntityContent = EntityUtils.toString(loginEntity);
	          System.out.println("-2.1 common: " + loginEntityContent);
			}
			
	      
	      //2.2 https://uac.10010.com/portal/Service/getHeadImg?callback=jQuery211012441581946720581_1479979764670&_=1479979764671
	      url = "https://uac.10010.com/portal/Service/getHeadImg?callback=jQuery211012441581946720581_1479979764670&_="+new Date().getTime();
	      HttpGet httpGet21 = new HttpGet(url);
	      HttpResponse loginResponse21 = httpclient.execute(httpGet21);
			statusCode = loginResponse21.getStatusLine().getStatusCode();
			
			if ( statusCode== 200) {
	          HttpEntity loginEntity = loginResponse21.getEntity();
	          String loginEntityContent = EntityUtils.toString(loginEntity);
	          System.out.println("-2.2 getHeadImg: " + loginEntityContent);
			}
			
	      //3.1
		  e3Url="http://iservice.10010.com/e3/static/query/searchPerInfoDetail/?_=";
	      HttpPost post1 = new HttpPost(e3Url+new Date().getTime());
	      post1.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
	      post1.setHeader("Accept-Encoding", "gzip, deflate");
	      post1.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,en-US;q=0.4,zh-TW;q=0.2");
	      post1.setHeader("Cache-Control", "max-age=0");
	      post1.setHeader("Connection", "keep-alive");
//	      post.setHeader("Content-Length", "0");
	      post1.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//	      post.setHeader("Cookie", "mallcity=11|110; route=dbf9f88e9ac80f498ca24d92e525186b; e3=v9pJY2yRGq8R3sLxRH2nyt2vch1CkG73qvY4hQhs8g6CrH6n8wz1!1562354273; piw=%7B%22login_name%22%3A%22185****2012%22%2C%22nickName%22%3A%22%E5%BE%90%E5%8D%93%E6%95%8F%22%2C%22rme%22%3A%7B%22ac%22%3A%22%22%2C%22at%22%3A%22%22%2C%22pt%22%3A%2201%22%2C%22u%22%3A%2218571652012%22%7D%2C%22verifyState%22%3A%22%22%7D; JUT=6KH2RV/gOKXSHLnY4Ouobg6RnSytn0q1yseSAw6MQAtZ7ZdErD5sGZtkczw5CtPj+Su+VleXj539p5jCap/Q/pP9HFvwxeIXN1n3XZ/Piogj4bXyW1kf59uI/5vXPNjLXoM3848pvhMnsx/QzxljPJKKDOmibGORgfE/H0gNdntgKyJnclUSU5LetnEVp22tr9hf2lmbCd6cn8RfTOBrkcolYxE5qFalMbuRjdtDFrPZU7AH4vkiHBYmHHmy4sVrLnxJYO15IpZH0Q/lfCqM5z038aNxiSUbaS9QbQYehEe4cWGwyWFWBtD3lroEtIvYst61hrVSYNLUWfYtQIF2pvdKRxXIC4b4FFgF0v178+MlM0XgZCAudzkdA2bE6Bopp/GNIc2J4/yzbNRpoizurbRu17WYMwRv0Ktgs/HqYtOv5IgUSDBZW39uvLrio2hNNSrTfVUhg9DB9TVnzdZA1Q7+s0xQJueIqnhY1kp8qA91ler/6zYw3ieOj0QsSutbZECyJHPgqteI8hWgJc0bnkw/jD79ehUaMhxjXPypc3g=l30ns7jVaxzyEML6xjh9AA==; _uop_id=addce794431aef182c1d71e9fb38a14c");
//	      post.setHeader("Cookie",StringUtils.join(cookieList.toArray(),";"));
	      post1.setHeader("Host", "iservice.10010.com");
	      post1.setHeader("Origin", "http://iservice.10010.com");
	      post1.setHeader("Referer", "http://iservice.10010.com/e4/skip.html");
	      post1.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36");
	      post1.setHeader("X-Requested-With", "XMLHttpRequest");
	      HttpResponse e3Response2 = httpclient.execute(post1);
			statusCode = e3Response2.getStatusLine().getStatusCode();
			
			if ( statusCode== 200) {
	          HttpEntity loginEntity = e3Response2.getEntity();
	          String loginEntityContent = EntityUtils.toString(loginEntity);
	          System.out.println("-3.1 状态:" + loginEntityContent);
			}
			
			//3.2 bind phone number
			if(statusCode == 200){
				url = "http://iservice.10010.com/e3/static/bind";
				HttpPost post2 = new HttpPost(url);
//	      	HttpClient historyClient = HttpClients.createDefault();
	      	post2.setHeader("Accept",
						"application/json, text/javascript, */*; q=0.01");
	      	post2.setHeader("Accept-Encoding", "gzip, deflate");
	      	post2.setHeader("Accept-Language",
						"zh-CN,zh;q=0.8,en;q=0.6,en-US;q=0.4,zh-TW;q=0.2");
				post2.setHeader("Connection", "keep-alive");
//				post2.addHeader("Content-length", "0");
//				post2.setHeader("Cookie",
//						StringUtils.join(cookieList.toArray(), ";"));
				post2.setHeader("Host", "iservice.10010.com");
				post2.setHeader("Origin", "http://iservice.10010.com");
				post2.setHeader("Referer", "http://iservice.10010.com/e4/query/basic/history_list.html?menuId=000100020001");
				post2.setHeader(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36");
				post2.setHeader("X-Requested-With", "XMLHttpRequest");
				System.out.println("line - "+post2.getRequestLine());
				
				HttpResponse response = httpclient.execute(post2);
	          if (response.getStatusLine().getStatusCode() == 200) {
	              statusCode =200;
	              System.out.println("bind: "+EntityUtils.toString(response.getEntity()));
	          }else{
	          	System.out.println("Get history data is failed.");
	          }
	          
	          
			}
			
	      
	      //4.获取历史记录：http://iservice.10010.com/e3/static/query/queryHistoryBill?_=1479960336249&accessURL=http://iservice.10010.com/e4/skip.html?menuCode=000100020001&menuCode=000100020001
	      if(statusCode==200){
	      	url = "http://iservice.10010.com/e3/static/query/queryHistoryBill?_="+new Date().getTime()+"&accessURL=http://iservice.10010.com/e4/skip.html?menuCode=000100020001&menuCode=000100020001";
	      	HttpPost post2 = new HttpPost(url);
	      	HttpClient historyClient = HttpClients.createDefault();
	      	post2.setHeader("Accept","application/json, text/javascript, */*; q=0.01");
	      	post2.setHeader("Accept-Encoding", "gzip, deflate");
	      	post2.setHeader("Accept-Language",
						"zh-CN,zh;q=0.8,en;q=0.6,en-US;q=0.4,zh-TW;q=0.2");
//				post2.setHeader("Cache-Control", "max-age=0");
				post2.setHeader("Connection", "keep-alive");
				post2.setHeader("Content-Type",
						"application/x-www-form-urlencoded;charset=UTF-8");
				post2.setHeader("Cookie",
						StringUtils.join(cookieList.toArray(), ";"));
				post2.setHeader("Host", "iservice.10010.com");
				post2.setHeader("Origin", "http://iservice.10010.com");
				post2.setHeader("Referer", "http://iservice.10010.com/e4/query/basic/history_list.html?menuId=000100020001");
				post2.setHeader(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36");
				post2.setHeader("X-Requested-With", "XMLHttpRequest");
				//请求参数：querytype:0001			querycode:0001			billdate:201608			flag:2
				
				// 创建参数队列    
				String month = "201608";
		        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
		        formparams.add(new BasicNameValuePair("querytype", "0001"));  
		        formparams.add(new BasicNameValuePair("querycode", "0001"));  
		        formparams.add(new BasicNameValuePair("billdate", month));  
		        formparams.add(new BasicNameValuePair("flag", "2"));  
		        UrlEncodedFormEntity uefEntity; 
		        
		        StringEntity stringEntity = new StringEntity("querytype=0001&querycode=0001&billdate=201610&flag=1");
		        stringEntity.setContentEncoding("UTF-8");
		        stringEntity.setContentType("application/json");
		        try {  
		            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
//		            post2.setEntity(uefEntity);
		            post2.setEntity(stringEntity);
		            System.out.println("executing request " + post2.getURI()+" entity:"+stringEntity.getContent());  
//		            HttpResponse response = historyClient.execute(post2);
		            HttpResponse response = httpclient.execute(post2);
		            if (response.getStatusLine().getStatusCode() == 200) {
	                  ZhangMing.saveToLocal(response.getEntity(), "chinaunicom.billzm." + month + ".2.html");
	              }else{
	              	System.out.println("Get history data is failed.");
	              	System.out.println("orginal: "+EntityUtils.toString(response.getEntity()));
	              
	              }
		        } catch (ClientProtocolException e) {  
		            e.printStackTrace();  
		            
		        } catch (UnsupportedEncodingException e1) {  
		            e1.printStackTrace();  
		            
		        } catch (IOException e) {  
		            e.printStackTrace();  
		            
		        } 
		        		        	          
	      }
	      
	      
	      if(statusCode==200){
	    	  url="http://iservice.10010.com/e3/static/query/callDetail?_="+new Date().getTime()+"&accessURL=http://iservice.10010.com/e4/query/bill/call_dan-iframe.html?menuCode=000100030001&menuid=000100030001";
	    	  HttpPost post3 = new HttpPost(url);
	    	  HttpClient callClient = HttpClients.createDefault();
		    	  post3.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		    	  post3.setHeader("Accept-Encoding", "gzip, deflate");
		    	  post3.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
		    	 // post3.setHeader("Cache-Control", "max-age=0");
		    	  post3.setHeader("Connection", "keep-alive");
		    	  post3.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		    	  post3.setHeader("Cookie",StringUtils.join(cookieList.toArray(), ";"));
		    	  post3.setHeader("Host", "iservice.10010.com");
		    	  post3.setHeader("Origin", "http://iservice.10010.com");
		    	  post3.setHeader("Referer", "http://iservice.10010.com/e4/query/bill/call_dan-iframe.html?menuCode=000100030001");
		    	  post3.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
		    	  post3.setHeader("X-Requested-With:", "XMLHttpRequest");
		     boolean pd1=false,pd2=false; 
		     
		    try{  
		    	Calendar calendar = Calendar.getInstance(Locale.CHINA);
		  		calendar.setTime(new Date());
		  		int day = calendar.getMinimum(Calendar.DAY_OF_MONTH);
		  		calendar.set(Calendar.DAY_OF_MONTH, day);
		  		String beginDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		  		System.out.println("*****"+beginDate);// 打印
		  		
		  		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		  		String endDate=sdf.format(new Date());
		  		System.out.println("@@@@@@"+endDate);
		  		
		  	result=getloanding(post3,httpclient,name,real_name,identity_id,beginDate,endDate); 
		      pd1=true;
		  	for(int m=1;m<6;m++){
		  		Calendar   c   =   Calendar.getInstance(Locale.CHINA); 				
				c.add(Calendar.MONTH, -m);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
				c.add(Calendar.MONTH, 0);  
		        c.set(Calendar.DAY_OF_MONTH, 1);  
		        beginDate = format.format(c.getTime());
		        
		        c.add(Calendar.MONTH, 1);  
		        c.set(Calendar.DAY_OF_MONTH, 0);  
		        endDate = format.format(c.getTime());  
		        
				result=getloanding(post3,httpclient,name,real_name,identity_id,beginDate,endDate); 
				pd2=true;
		  	}
		  	
		    }catch(Exception e){
		    	//e.printStackTrace();
		    	pd1=false;
		    	pd2=false;
		    }
		  	
		  	if(pd1&&pd2){

				//String identity_id=(String) loansession.getAttribute("identity_id");
//	   			System.out.println("identity_id:"+identity_id);
//	   			double loan_amount=(double) loansession.getAttribute("loan_amount");
//	   			System.out.println("loan_amount:"+loan_amount);
//	   			int loan_period=(int) loansession.getAttribute("loan_period");
//	   			System.out.println("loan_period:"+loan_period);
//	   			String loan_ip=(String) loansession.getAttribute("loan_ip");
//	   			System.out.println("loan_ip:"+loan_ip);
//	   			
//	   			result=loanService.loanconfirm(identity_id,loan_amount,loan_period,loan_ip);

//	   			double loan_amount=(double) loansession.getAttribute("loan_amount");
//	   			int loan_period=(int) loansession.getAttribute("loan_period");
//	   			//String loan_ip=(String) loansession.getAttribute("loan_ip");
//	   			result=loanService.loanconfirm(identity_id,loan_amount,loan_period,loan_ip);

		  	}
		      
	      }
	      
	      
	      chsisession.invalidate();
	      httpclient.close();
	      
//	      if (loginResponse.getStatusLine().getStatusCode() == 200) {
//	          for (Header head : loginResponse.getAllHeaders()) {
//	              System.out.println(head);
//	          }
//	          HttpEntity loginEntity = loginResponse.getEntity();
//	          String loginEntityContent = EntityUtils.toString(loginEntity);
//	          System.out.println("登录状态:" + loginEntityContent);
	//
//	          //jQuery17208151653951499611_1404661522215({resultCode:"7007",redirectURL:"http://www.10010.com",errDesc:"null",msg:'用户名或密码不正确。<a href="https://uac.10010.com/cust/resetpwd/inputName" target="_blank" style="color: #36c;cursor: pointer;text-decoration:underline;">忘记密码？</a>',needvode:"1"});
//	          //如果登录成功
//	          if (loginEntityContent.contains("resultCode:\"0000\"")) {
	//
//	              //月份
//	              String months[] = new String[]{"201611","201610", "201609", "201608", "201607", "201606"};//"201401", "201402", "201403", "201404", "201405"
	//
//	              for (String month : months) {
//	                  //http://iservice.10010.com/ehallService/static/historyBiil/execute/YH102010002/QUERY_YH102010002.processData/QueryYH102010002_Data/201405/undefined?_=1404661790076&menuid=000100020001
////	                  String billurl = "http://iservice.10010.com/ehallService/static/historyBiil/execute/YH102010002/QUERY_YH102010002.processData/QueryYH102010002_Data/" + month + "/undefined";
//	              	String billurl = "http://iservice.10010.com/e3/static/query/queryHistoryBill?_=1479960336249&accessURL=http://iservice.10010.com/e4/skip.html?menuCode=000100020001&menuCode=000100020001";
	//
//	                  HttpPost httpPost = new HttpPost(billurl);
//	                  HttpResponse billresponse = httpClient.execute(httpPost);
//	                  if (billresponse.getStatusLine().getStatusCode() == 200) {
//	                      Tools.saveToLocal(billresponse.getEntity(), "chinaunicom.bill." + month + ".2.html");
//	                  }
//	              }
	//
//	          }
//	      }
	      
	      return result;
	  }
		
		//
		public  ResponseMessage regixStr(String[] responseStrArray,String name,String real_name,String identity_id){
			ResponseMessage result=new ResponseMessage();
			boolean ok=false;
		try{
			for(int i=0;i<responseStrArray.length;i++){
				String lastStr=responseStrArray[i];
				System.out.println("引入方法："+lastStr);
				
				int s_numberName=lastStr.indexOf("nickName")+11;
                int e_numberName=lastStr.indexOf(",",s_numberName+2)-1;
                String nickName=lastStr.substring(s_numberName, e_numberName);
              		                  
                String numberName=name+"/"+real_name;
               
                int s_calldate=lastStr.indexOf("calldate")+11;
                int e_calldate=lastStr.indexOf(",",s_calldate+7)-1;
                String calldate=lastStr.substring(s_calldate, e_calldate);
                	                  
                int s_calltime=lastStr.indexOf("calltime")+11;
                int e_calltime=lastStr.indexOf(",",s_calltime+7)-1;
                String calltime=lastStr.substring(s_calltime, e_calltime);
                		                  
                String date_time=calldate+" "+calltime;
                		                  
                int s_homeareaName=lastStr.indexOf("homeareaName")+15;
                int e_homeareaName=lastStr.indexOf(",",s_homeareaName+1)-1;
                String homeareaName=lastStr.substring(s_homeareaName, e_homeareaName);
                		                  
                int s_calltypeName=lastStr.indexOf("calltypeName")+15;
                int e_calltypeName=lastStr.indexOf(",",s_calltypeName+1)-1;
                String calltypeName=lastStr.substring(s_calltypeName, e_calltypeName);
                		                  
                int s_othernum=lastStr.indexOf("othernum")+11;
                int e_othernum=lastStr.indexOf(",",s_othernum+1)-1;
                String othernum=lastStr.substring(s_othernum, e_othernum);
                	                  
                int s_calllonghour=lastStr.indexOf("calllonghour")+15;
                int e_calllonghour=lastStr.indexOf(",",s_calllonghour+1)-1;
                String calllonghour=lastStr.substring(s_calllonghour, e_calllonghour);
                	                  
                int s_romatypeName=lastStr.indexOf("romatypeName")+15;
                int e_romatypeName=lastStr.indexOf(",",s_romatypeName+1)-1;
                String romatypeName=lastStr.substring(s_romatypeName, e_romatypeName);
                		                  
                //保存
                XmydCallhistory callhistory=new XmydCallhistory();
                callhistory.setIdentity_id(identity_id);
                callhistory.setCalldate(date_time);
                callhistory.setCalllonghour(calllonghour);
                callhistory.setCalltypeName(calltypeName);
                callhistory.setHomeareaName(homeareaName);
                callhistory.setNumber_nickname(numberName);
                callhistory.setOthernum(othernum);
                callhistory.setRomatypeName(romatypeName);		                  
                result=xmydCallhistoryService.saveUnicom(callhistory);
                				
			}
			
		}catch(Exception e){
			e.printStackTrace();
			
		}
		
	
		return result;
		}
		
		
		//
		public ResponseMessage getloanding(HttpPost post3,CloseableHttpClient httpclient,String name,String real_name,String identity_id,String beginDate,String endDate) throws UnsupportedEncodingException{
			boolean ok=false;
			ResponseMessage result=new ResponseMessage();
//			 String beginDate="2016-11-01";
//	    	  String endDate="2016-11-30";	 

	    	  List<NameValuePair> callparams = new ArrayList<NameValuePair>();
	    	  callparams.add(new BasicNameValuePair("pageNo","1")); 
	    	  callparams.add(new BasicNameValuePair("pageSize","100")); 
	    	  callparams.add(new BasicNameValuePair("beginDate",beginDate)); 
	    	  callparams.add(new BasicNameValuePair("endDate",endDate)); 	    	  
	    	  UrlEncodedFormEntity callEntity; 	    	  
	    	  StringEntity stringEntity = new StringEntity("pageNo=1&pageSize=100&beginDate="+beginDate+"&endDate="+endDate);
	    	  stringEntity.setContentEncoding("UTF-8");
		      stringEntity.setContentType("application/json");		      
		      try{
		    	  callEntity = new UrlEncodedFormEntity(callparams, "UTF-8"); 
		    	   post3.setEntity(stringEntity);		    	  
		    	   HttpResponse callresponse = httpclient.execute(post3);		    	   
		    	   if (callresponse.getStatusLine().getStatusCode() == 200) {
		                 // ZhangMing.saveToLocal(callresponse.getEntity(), "zm" + beginDate+ ".8888.html");		                  
		                  String callresult=EntityUtils.toString(callresponse.getEntity());
		                  StringBuffer buffer=new StringBuffer(callresult);
		                  String lastStr=buffer.toString().replaceAll("\\s*", "");	
		                  
		                  System.out.println("*********:"+lastStr);
		                  
		                  
		                  int s_lastStr=lastStr.indexOf("result")+9;
		                  int e_lastStr=lastStr.indexOf("]",s_lastStr+5);	                 
		                  lastStr=lastStr.substring(s_lastStr, e_lastStr);		                  
		                  String[] responseStrArray=lastStr.split("}");		                  		                  
		                  result=regixStr(responseStrArray, name,real_name,identity_id);		                  
		              }else{
		              	System.out.println("Get history data is failed.");
		              	System.out.println("orginal: "+EntityUtils.toString(callresponse.getEntity()));
		              	ok=true;
		              }		    	   
		      }catch(Exception e){
		    	  e.printStackTrace();
		    	  ok=true;
		      }	
		      if(ok){
		    	result.setCode("0");  
		    	result.setMessage("数据加载异常！");
		    	return result;
		      }
		     
		     return result;
		}
		
}
