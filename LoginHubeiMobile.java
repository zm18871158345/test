0package org.xmyd.app.backstage.controller.phone;


import java.io.ByteArrayOutputStream;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
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
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
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
@RequestMapping("/phone")
public class LoginHubeiMobile {
	
	@Autowired
	private XmydLoanService loanService;
	
	@Autowired
	private HttpSession loansession;
		
	
	@Autowired
	private XmydCallhistoryService xmydCallhistoryService;
	
	@Autowired
	private HttpSession mobilesession;
	
	List<String> session=new ArrayList<String>();
	
	 
	
	@RequestMapping("/hbmobile1.do")
	@ResponseBody
	public ResponseMessage HBMobile1(HttpServletRequest request,HttpServletResponse response) throws ParseException, ClientProtocolException, IOException{	
		
		ResponseMessage result=new ResponseMessage();
		
		List<String> cookieList = new ArrayList<String>();
		String time=String.valueOf(new Date().getTime());
		String imgurl="https://hb.ac.10086.cn/SSO/img?codeType=0&rand="+time;
		
		CookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
				
		
		HttpGet imghttpGet=new HttpGet(imgurl+new Date().getTime());
		HttpResponse imgResponseResult=httpclient.execute(imghttpGet);					
		//获取cookie
		
		for (Header head : imgResponseResult.getAllHeaders()) {		
              System.out.println("imgResponseResult:"+head);
              if("Set-Cookie".equals(head.getName())){
              	String value = head.getValue();
              	cookieList.add(value.split(";")[0]);
              	
              }
        }	
		String cookie=StringUtils.join(cookieList.toArray(), ";");
		
		
		mobilesession.setAttribute("cookie1", cookieStore);
		
		
		InputStream imgStream=imgResponseResult.getEntity().getContent();	
				
		OutputStream os = new FileOutputStream("D:\\xmyd\\user\\verifycade.png");
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = imgStream.read(buffer, 0, 8192)) != -1) {
		os.write(buffer, 0, bytesRead);
		}
		os.close();
		imgStream.close();
				
		result.setCode("0");
		result.setMessage("图片验证正常！");
		
		imgStream.close();
		os.close();
		return result;
		
	}

	
	
		@RequestMapping("/hbmobile2.do")
		@ResponseBody
		public ResponseMessage HBMobile2(HttpServletRequest request,HttpServletResponse response) throws ParseException, ClientProtocolException, IOException{	
			ResponseMessage result=new ResponseMessage();
			List<String> cookieList = new ArrayList<String>();
			String time=String.valueOf(new Date().getTime());
		//ip的获取
		String ip = request.getHeader("x-forwarded-for"); 
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	    	   ip = request.getHeader("Proxy-Client-IP"); 
	       } 
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	    	   ip = request.getHeader("WL-Proxy-Client-IP"); 
	       } 
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	    	   ip = request.getRemoteAddr(); 
	       }

	       
	  //  String ip=request.getParameter("ip");
			System.out.println(ip);  
	    String verifycode=request.getParameter("verifycode");
	       System.out.println(verifycode);
		String phone_number=request.getParameter("phone_number");
		  System.out.println(phone_number);
		String phone_pwd=request.getParameter("phone_pwd");
		  System.out.println(phone_pwd);

		
		
		CookieStore cookieStore=new BasicCookieStore();
		cookieStore=(CookieStore) mobilesession.getAttribute("cookie1");
		HttpClient httpClient=HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		
	
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("accountType","0"));
		params.add(new BasicNameValuePair("username",phone_number));
		params.add(new BasicNameValuePair("passwordType","1"));
		params.add(new BasicNameValuePair("password",phone_pwd));
		params.add(new BasicNameValuePair("smsRandomCode",""));
		params.add(new BasicNameValuePair("emailusername","请输入登录帐号"));
		params.add(new BasicNameValuePair("emailpassword",""));
		params.add(new BasicNameValuePair("validateCode",verifycode));  //验证码输入的字符？
		params.add(new BasicNameValuePair("action","/SSO/loginbox"));
		params.add(new BasicNameValuePair("style","mymobile"));
		params.add(new BasicNameValuePair("service","servicenew"));
		params.add(new BasicNameValuePair("continue","http://www.hb.10086.cn/servicenew/index.action"));
		params.add(new BasicNameValuePair("submitMode","login"));
		params.add(new BasicNameValuePair("guestIP",ip));
		boolean ok=false;
					
		String url1="https://hb.ac.10086.cn/SSO/loginbox";
		HttpPost post1=new HttpPost(url1);
		post1.setHeader("ContentType", "application/x-www-form-urlencoded");
		post1.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		post1.setHeader("KeepAlive", "true");   //????????
		post1.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.19 Safari/537.36");
		post1.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
		post1.setHeader("Accept-Encoding", "gzip, deflate, br");
		post1.setHeader("Referer", "https://hb.ac.10086.cn/SSO/loginbox?service=servicenew&style=mymobile&continue=http://www.hb.10086.cn/servicenew/index.action");
		post1.setHeader("Origin", "https://hb.ac.10086.cn ");
		post1.setHeader("Host", "hb.ac.10086.cn ");
	
		
		
		UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params);
		post1.setEntity(entity);
		
		HttpResponse loginResult=httpClient.execute(post1);

		
		String xjbt=EntityUtils.toString(loginResult.getEntity());		
		System.out.println(xjbt);
		

		
		
		int yrncode=xjbt.indexOf("SAMLart");				
		System.out.println(yrncode);
		if(yrncode==-1){
			result.setCode("1");
			result.setMessage("您输入的服务密码或是验证码错误！请重新输入!");
			return result;
		}
		
		int m_SAMLart=xjbt.indexOf("SAMLart")+8;
		
		int s_SAMLart=xjbt.indexOf("\"",m_SAMLart+2)+1;
		int e_SAMLart=xjbt.indexOf("\"",s_SAMLart+2);
		String SAMLart=xjbt.substring(s_SAMLart, e_SAMLart);
		System.out.println(SAMLart);
		
		int m_artifact=xjbt.indexOf("artifact")+9;
		
		int s_artifact=xjbt.indexOf("\"",m_artifact+2)+1;
		int e_artifact=xjbt.indexOf("\"",s_artifact+2);
		String artifact=xjbt.substring(s_artifact, e_artifact);
		System.out.println(artifact);
		
		String posturl2="http://www.hb.10086.cn/servicenew/postLogin.action?timeStamp="+time;
		
		HttpPost post2=new HttpPost(posturl2);
		post2.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post2.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.19 Safari/537.36");
		
		List<NameValuePair> params2=new ArrayList<NameValuePair>();
		
		params2.add(new BasicNameValuePair("RelayState","http://www.hb.10086.cn/servicenew/index.action"));
		params2.add(new BasicNameValuePair("SAMLart",SAMLart));
		params2.add(new BasicNameValuePair("artifact",artifact));
		params2.add(new BasicNameValuePair("accountType","0"));
		params2.add(new BasicNameValuePair("PasswordType","1"));
		params2.add(new BasicNameValuePair("errorMsg",""));
		params2.add(new BasicNameValuePair("errFlag",""));
		params2.add(new BasicNameValuePair("telNum",""));
		
		
		UrlEncodedFormEntity entity2=new UrlEncodedFormEntity(params2);
		post2.setEntity(entity2);
		
		HttpResponse loginResult2=httpClient.execute(post2);
		String loginhtml2=EntityUtils.toString(loginResult2.getEntity());
		System.out.println(loginhtml2);
		int statusCode=loginResult2.getStatusLine().getStatusCode();
		System.out.println(statusCode);
		
		
		if (statusCode==301||statusCode==302){
			//redirect and auto redirect
			System.out.println("enter redirect.");
			Header header =loginResult2.getFirstHeader("Location");
			if(header != null){
				//https://account.chsi.com.cn/account/accountroadmap.action
				String url = header.getValue();
				System.out.println("-----url get1: "+url);
				HttpPost post3 = new HttpPost(url);
				
				post3.addHeader("Content-Type", "application/x-www-form-urlencoded");
				post3.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.19 Safari/537.36");
				
				List<NameValuePair> params3=new ArrayList<NameValuePair>();
				
				params3.add(new BasicNameValuePair("RelayState","http://www.hb.10086.cn/servicenew/index.action"));
				params3.add(new BasicNameValuePair("SAMLart",SAMLart));
				params3.add(new BasicNameValuePair("artifact",artifact));
				params3.add(new BasicNameValuePair("accountType","0"));
				params3.add(new BasicNameValuePair("PasswordType","1"));
				params3.add(new BasicNameValuePair("errorMsg",""));
				params3.add(new BasicNameValuePair("errFlag",""));
				params3.add(new BasicNameValuePair("telNum",""));
				
				HttpResponse responseAcount = httpClient.execute(post3);
				String lacationhtml=EntityUtils.toString(responseAcount.getEntity());
				System.out.println(lacationhtml);

			}
		}
		
		
		String url4="http://www.hb.10086.cn/servicenew/index.action";
		HttpGet get4=new HttpGet(url4);
		get4.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		get4.addHeader("Accept-Encoding", "gzip, deflate");
		get4.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
		get4.addHeader("KeepAlive", "true");
		get4.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
		
		HttpResponse responseget4 = httpClient.execute(get4);
		String get4html=EntityUtils.toString(responseget4.getEntity());
		System.out.println("get4html:"+get4html);
		System.out.println("编码:"+responseget4.getStatusLine().getStatusCode());
		
		
		String url5="http://www.hb.10086.cn/my/balance/qryBalance.action";
		HttpGet get5=new HttpGet(url5);
		get5.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		get5.addHeader("Accept-Encoding", "gzip, deflate");
		get5.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
		get5.addHeader("KeepAlive", "true");
		get5.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
		
		
		HttpResponse loginResult5=httpClient.execute(get5);
		String loginhtml5=EntityUtils.toString(loginResult5.getEntity());
		System.out.println(loginhtml5);
		int statusCode5=loginResult2.getStatusLine().getStatusCode();
		System.out.println(statusCode5);
		
		
		String locationhhtml=null;
		int statusCode6=200;
		HttpResponse responseget6=null;
		
		if (statusCode5==301||statusCode5==302){
			
			Header header =loginResult5.getFirstHeader("Location");
			if(header != null){
				//https://account.chsi.com.cn/account/accountroadmap.action
				String url = header.getValue();
				System.out.println("-----url get1: "+url);
				HttpGet get6 = new HttpGet(url);
				
				get6.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				get6.addHeader("Accept-Encoding", "gzip, deflate");
				get6.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
				get6.addHeader("KeepAlive", "true");
				get6.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
				
				
//				Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
//				Accept-Encoding:gzip, deflate, sdch, br
//				Accept-Language:zh-CN,zh;q=0.8
//				Connection:keep-alive
//				Cookie:_gscu_1502255179=82904906emr1jk18; _ca_tk=pmsiez7e3p8ttyq3x5lsd0jqkzd14c74; my_login_account=18871158345; CmWebtokenid=18871158345,hb; cmccssotoken=7fb16593bfdc4a04816f2cdf6caab6a6@.10086.cn; userinfokey=%7b%22loginType%22%3a%2201%22%2c%22provinceName%22%3a%22270%22%2c%22pwdType%22%3a%2201%22%2c%22userName%22%3a%2218871158345%22%7d; is_login=true; CmLocation=270|270; CmProvid=hb; WT_FPC=id=2b60176392ba7cd19fa1482904906528:lv=1483065735135:ss=1483062434200
//				Host:login.10086.cn
//				Referer:http://www.hb.10086.cn/servicenew/index.action
//				Upgrade-Insecure-Requests:1
//				User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36
				
				responseget6 = httpClient.execute(get6);
				String get6html=EntityUtils.toString(responseget6.getEntity());
				
				System.out.println(get6html);
				 statusCode6=loginResult2.getStatusLine().getStatusCode();
				System.out.println(statusCode6);
				
			}
		}
		
		//重点*************
		if (statusCode6==301||statusCode6==302){
			
			Header header =responseget6.getFirstHeader("Location");
			if(header != null){
				//https://account.chsi.com.cn/account/accountroadmap.action
				String url = header.getValue();
				System.out.println("-----url get1: "+url);
				HttpGet get6 = new HttpGet(url);
			}
		}
		
		
		
		
		
		
//		Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
//		Accept-Encoding:gzip, deflate, sdch
//		Accept-Language:zh-CN,zh;q=0.8
//		Connection:keep-alive
//		Cookie:_gscu_1502255179=82904906emr1jk18; _ca_tk=pmsiez7e3p8ttyq3x5lsd0jqkzd14c74; n62fO8j8LA=MDAwM2IyZDdkZDAwMDAwMDAwMjYwIAExax8xNDgzMDg5OTU0; my_login_account=18871158345; mmobile_roteServer=PRODUCT; JSESSIONIDMY=08VNjl2rW7_k-_d9b4_iBHrcOAPYEL82oLg64kpxd866M72i8Rle!-1649167145; servicepro=35efcb93fae6dd9a591caa661b1459a4; JSESSIONIDSHOPPING=EB1Njl4vfbeHQomTSlOZyZtMEsplBlr9m15ltWqohooRutPlKtqQ!-1048959657; G1IcpoFuFS=MDAwM2IyZDdkZDAwMDAwMDAxNWYwV2JVVykxNDgzMDkxMjA1; JSESSIONIDSHOPPINGBBC=tttNjiO2-72_BwoEYMkAmF-TY9ZM8edBPShCEvYE_11taCfOGqk9!-2030887346; bbc_service=4f1df5ba31b1d84b605445adb0383776|1483064872|1483064872; hhhzdrBwTU=MDAwM2IyZDdkZDAwMDAwMDAwMjUwbzM7WEwxNDgzMDkyMDI0; CmWebtokenid=18871158345,hb; emall_roteServer=PRODUCT; userinfokey=%7b%22loginType%22%3a%2201%22%2c%22provinceName%22%3a%22270%22%2c%22pwdType%22%3a%2201%22%2c%22userName%22%3a%2218871158345%22%7d; is_login=true; Hm_lvt_21ea48c997a7a6224b76082d6e173f48=1483008363,1483010116,1483011052,1483062534; Hm_lpvt_21ea48c997a7a6224b76082d6e173f48=1483065734; CmLocation=270|270; CmProvid=hb; WT_FPC=id=2b60176392ba7cd19fa1482904906528:lv=1483065735135:ss=1483062434200
//		Host:www.hb.10086.cn
//		Referer:http://www.hb.10086.cn/servicenew/index.action
//		Upgrade-Insecure-Requests:1
//		User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36
		
		
//		
//		//登录成功后获取cookie
//		
//		int statusCode =loginResult.getStatusLine().getStatusCode();
//		if(statusCode==200){
//			System.out.println("登录湖北移动$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//			
//			for (Header head : loginResult.getAllHeaders()) {
//	              System.out.println(head);
//	              if("Set-Cookie".equals(head.getName())){
//	              	String value = head.getValue();
//	              	cookieList.add(value.split(";")[0]);
//	              }
//	          }
//		}
//		

		String posturl7="http://www.hb.10086.cn/my/account/smsRandomPass!sendSmsCheckCode.action?menuid=myDetailBill";
		HttpGet httpGet7=new HttpGet(posturl7);
		
		httpGet7.addHeader("Cookie", StringUtils.join(cookieList.toArray(), ";"));
		HttpResponse response7=httpClient.execute(httpGet7);
		String get7html=EntityUtils.toString(response7.getEntity());
		System.out.println("---------$$$$$---------->:"+get7html);
		
		mobilesession.setAttribute("cookie2", cookieStore);
//		String url2="http://www.hb.10086.cn/my/account/smsRandomPass!sendSmsCheckCode.action?menuid=myDetailBill";
//		HttpGet httpGet=new HttpGet(url2);
//		httpGet.addHeader("Cookie", StringUtils.join(cookieList.toArray(), ";"));
//		HttpResponse ResponseGet=httpClient.execute(httpGet);
//		int statusget=ResponseGet.getStatusLine().getStatusCode();
//		//获取cookie	
//		if(statusget==200){
//			System.out.println("我很辛运！！！！！！！！！");
//			for (Header head : ResponseGet.getAllHeaders()) {
//	              System.out.println(head);
//	              if("Set-Cookie".equals(head.getName())){
//	              	String value = head.getValue();
//	              	cookieList.add(value.split(";")[0]);
//	              }
//	          }
//		}
//						
//			//获取参数
//			String loginget=EntityUtils.toString(ResponseGet.getEntity());
//			StringBuffer bufferr=new StringBuffer(loginget);
//            String getStr=bufferr.toString().replaceAll("\\s*", "");           
//            int s_getStr=getStr.indexOf("SAMLart")+15;           
//            int e_getStr=getStr.indexOf(">",s_getStr+5)-2;	          
//            String SAMLart=getStr.substring(s_getStr, e_getStr);	
//            
//            
//		
//            List<NameValuePair> getparamer=new ArrayList<NameValuePair>();
//            getparamer.add(new BasicNameValuePair("RelayState","http://www.hb.10086.cn:80/my/account/smsRandomPass!sendSmsCheckCode.action?menuid=myDetailBill"));
//            getparamer.add(new BasicNameValuePair("SAMLart",SAMLart));
//            getparamer.add(new BasicNameValuePair("PasswordType","1"));
//            getparamer.add(new BasicNameValuePair("errorMsg",""));
//            getparamer.add(new BasicNameValuePair("errFlag",""));
//            getparamer.add(new BasicNameValuePair("telNum",""));
//            
//            
//            String url3="http://www.hb.10086.cn/my/notify.action";
//
//            HttpPost post3=new HttpPost(url3);
////            post3.setHeader("ContentType","application/x-www-form-urlencoded");
////            post3.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
////            post3.setHeader("KeepAlive","true");
////            post3.setHeader("P3P","CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
////            post3.setHeader("UserAgent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.19 Safari/537.36");
////            post3.setHeader("Accept-Language","zh-CN,zh;q=0.8");
////            post3.setHeader("Accept-Encoding","gzip, deflate, sdch");
////            post3.setHeader("Host","www.hb.10086.cn"); 
////            post3.setHeader("Cookie",StringUtils.join(cookieList.toArray(), ";"));
//            
//            
//            UrlEncodedFormEntity post3entity=new UrlEncodedFormEntity(getparamer);
//    		post3.setEntity(post3entity);
//            
//            HttpResponse Responsepost=httpClient.execute(post3);
//            
//            System.out.println("Responsepost:"+Responsepost);
//            
//            if(Responsepost.getStatusLine().getStatusCode()==200){
//            	System.out.println("在此处迷失!");
//            	for (Header head : Responsepost.getAllHeaders()) {
//  	              System.out.println(head);
//  	              if("Set-Cookie".equals(head.getName())){
//  	              	String value = head.getValue();
//  	              	cookieList.add(value.split(";")[0]);
//  	              }
//  	          }
//            }
//           
//            
//          //  mobilesession.setAttribute("cookie2", StringUtils.join(cookieList.toArray(), ";")); 
//            
//            //解析cookie
////            List<String> session=new ArrayList<String>();
//          //  Enumeration<String> cookiename=mobilesession.getAttributeNames();
////			for(Enumeration e=cookiename;e.hasMoreElements();){
////			       String thisName=e.nextElement().toString();
////			       String thisValue=(String) mobilesession.getAttribute(thisName);
////			       System.out.println(thisName+"--------------"+thisValue);
////			       session.add(thisValue.split(";")[0]);
////			}
//		//	String post4cookie=StringUtils.join(session.toArray(), ";");
//            
//           //发短信？？？ 
//            String url4="http://www.hb.10086.cn/my/account/smsRandomPass!sendSmsCheckCode.action?menuid=myDetailBill";
//            HttpPost post4=new HttpPost(url4);
//            post4.setHeader("ContentType","application/x-www-form-urlencoded");
//          post4.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//          post4.setHeader("KeepAlive","true");
//          post4.setHeader("P3P","CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
//          post4.setHeader("UserAgent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.19 Safari/537.36");
//         	post4.setHeader("Accept-Language","zh-CN,zh;q=0.8");
//          post4.setHeader("Accept-Encoding","gzip, deflate, sdch");
//          post4.setHeader("Host","www.hb.10086.cn"); 
//          post4.setHeader("Cookie",StringUtils.join(cookieList.toArray(), ";"));
//          
//          try{
//          UrlEncodedFormEntity post4entity=new UrlEncodedFormEntity(getparamer);
//  			post4.setEntity(post4entity);
//          
//          HttpResponse Responsepost4=httpClient.execute(post4);
//          
//          System.out.println("Responsepost:"+Responsepost4);
//		if(Responsepost4.getStatusLine().getStatusCode()==200){
//			System.out.println("可以发短信验证码了?????????");
//			
//			for (Header head : Responsepost4.getAllHeaders()) {
//	              System.out.println(head);
//	              if("Set-Cookie".equals(head.getName())){
//	              	String value = head.getValue();
//	              	cookieList.add(value.split(";")[0]);
//	              }
//	          }
//			
//		}
//		//mobilesession.setAttribute("cookie2", StringUtils.join(cookieList.toArray(), ";")); 
//		mobilesession.setAttribute("cookie2", cookieStore); 
////			String cookies=StringUtils.join(cookieList.toArray(), ";");
////			mobilesession.setAttribute("cookie", cookies);
////		Enumeration<String> cookienames=mobilesession.getAttributeNames();
////		for(Enumeration e=cookienames;e.hasMoreElements();){
////		       String thisName=e.nextElement().toString();
////		       String thisValue=(String) mobilesession.getAttribute(thisName);
////		       System.out.println(thisName+"------接口2中的session最后保存的值为：--------"+thisValue);
////		       session.add(thisValue.split(";")[0]);
////		}
//		
//          }catch(Exception e){
//        	  e.printStackTrace();
//        	  ok=true;
//          }
//          if(ok){
//        	  result.setCode("1");
//        	  result.setMessage("获取短信验证码异常！");
//        	  return result;
//          }
          result.setCode("0");
          result.setMessage("短信验证码获取正常！");
          return result;
		}   
		//断开3
		
		@RequestMapping("/hbmobile3.do")
		@ResponseBody
		public ResponseMessage HBMobile3(HttpServletRequest request,HttpServletResponse response) throws ParseException, ClientProtocolException, IOException,Exception{
			ResponseMessage result=new ResponseMessage();
			
			
			String loan_ip = request.getHeader("x-forwarded-for"); 
		       if(loan_ip == null || loan_ip.length() == 0 || "unknown".equalsIgnoreCase(loan_ip)) { 
		    	   loan_ip = request.getHeader("Proxy-Client-IP"); 
		       } 
		       if(loan_ip == null || loan_ip.length() == 0 || "unknown".equalsIgnoreCase(loan_ip)) { 
		    	   loan_ip = request.getHeader("WL-Proxy-Client-IP"); 
		       } 
		       if(loan_ip == null || loan_ip.length() == 0 || "unknown".equalsIgnoreCase(loan_ip)) { 
		    	   loan_ip = request.getRemoteAddr(); 
		       }
			
		   
			final String phone_pwd=request.getParameter("phone_pwd");
			System.out.println(phone_pwd);
			final String chkey=request.getParameter("chkey");
			System.out.println(chkey);
			final String identity_id=request.getParameter("identity_id");
			final String phone_number=request.getParameter("phone_number");
			final String real_name=request.getParameter("real_name");
			

			
//			new Thread(new Runnable() { 
//				
//	            public void run() { 
//	            	
//	            	String billcycle;
//	    			for(int m=0;m<6;m++){
//	    		  		Calendar   c   =   Calendar.getInstance(Locale.CHINA); 				
//	    				c.add(Calendar.MONTH, -m);
//	    				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");  
//	    				c.add(Calendar.MONTH, 0);  
//	    		        c.set(Calendar.DAY_OF_MONTH, 1);  
//	    		        String beginDate = format.format(c.getTime());
//	    		        billcycle=beginDate.substring(0, 6);
//	    		        System.out.println( billcycle);		        
//	    		        try{
//							getloanding(billcycle, phone_pwd,chkey,real_name,identity_id,phone_number);
//	    		        }catch(Exception e){
//		    				e.printStackTrace();
//		    			}	        
//	    			}
//	            	
//	            } 
//	        }).start();	

	            
			String billcycle;
			for(int m=0;m<6;m++){
		  		Calendar   c   =   Calendar.getInstance(Locale.CHINA); 				
				c.add(Calendar.MONTH, -m);
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");  
				c.add(Calendar.MONTH, 0);  
		        c.set(Calendar.DAY_OF_MONTH, 1);  
		        String beginDate = format.format(c.getTime());
		        billcycle=beginDate.substring(0, 6);
		       
		     
		        getloanding(billcycle, phone_pwd,chkey,real_name,identity_id,phone_number);
			}

			
//			System.out.println("identity_id:"+identity_id);
//  			double loan_amount=(double) loansession.getAttribute("loan_amount");
//  			System.out.println("loan_amount:"+loan_amount);
//  			int loan_period=(int) loansession.getAttribute("loan_period");
//  			System.out.println("loan_period:"+loan_period);
//  			//String loan_ip=(String) loansession.getAttribute("loan_ip");
//  			System.out.println("loan_ip:"+loan_ip);  			
//  			result=loanService.loanconfirm(identity_id,loan_amount,loan_period,loan_ip);
			
			result.setCode("0");
			result.setMessage("数据检验正常！");
			return result;         
	           
			
			
	}
		
		
		
		
		public ResponseMessage  getloanding(String billcycle,String phone_pwd,String chkey,String real_name,String identity_id,String phone_number) throws UnsupportedEncodingException{
			ResponseMessage result=new ResponseMessage();
			
			CookieStore cookieStore=new BasicCookieStore();
			cookieStore = (CookieStore)mobilesession.getAttribute("cookie2");
			HttpClient httpClient=ZhangMing.createSSLClientDefaultWithCookie(cookieStore);
			
			//String cookie=(String) mobilesession.getAttribute("cookie");
			
		String url5="http://www.hb.10086.cn/my/billdetails/billDetailQry.action?postion=outer";
					 
             

           List<NameValuePair> verifyparams=new ArrayList<NameValuePair>();
           verifyparams.add(new BasicNameValuePair("detailBean.billcycle",billcycle));
           verifyparams.add(new BasicNameValuePair("detailBean.selecttype","0"));
           verifyparams.add(new BasicNameValuePair("detailBean.flag","GSM"));
           verifyparams.add(new BasicNameValuePair("menuid","myDetailBill"));
           verifyparams.add(new BasicNameValuePair("groupId","tabs3"));
           verifyparams.add(new BasicNameValuePair("detailBean.password",phone_pwd));
           verifyparams.add(new BasicNameValuePair("detailBean.chkey",chkey));
         
           HttpPost post5=new HttpPost(url5);
           post5.setHeader("Host", "www.hb.10086.cn");
           post5.setHeader("KeepAlive", "true");
           post5.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
           post5.setHeader("Origin", "http://www.hb.10086.cn");
           post5.setHeader("UserAgent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.19 Safari/537.36");
           post5.setHeader("ContentType", "application/x-www-form-urlencoded");
           post5.setHeader("Referer", "http://www.hb.10086.cn/my/balance/qryBalance.action");
           post5.setHeader("Accept-Encoding", "gzip, deflate");
           post5.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
           // post5.setHeader("cookie",coo);// StringUtils.join(htmlmobile.toArray(), ";")
           
           //-------------------------------------->
           UrlEncodedFormEntity post5entity=new UrlEncodedFormEntity(verifyparams);
   		post5.setEntity(post5entity);
   		boolean ok=false;
   		try{
   		HttpResponse post5Result=httpClient.execute(post5);
   		
   		int gooo=post5Result.getStatusLine().getStatusCode();
   		
   		//	String loginResult=EntityUtils.toString(httpClient.execute(post1).getEntity());  //此为html页面
   		//System.out.println("登录湖北移动返回的结果："+EntityUtils.toString(post5Result.getEntity()));
   		String post5html=EntityUtils.toString(post5Result.getEntity()); 
   				
   		StringBuffer Strbuffer=new StringBuffer(post5html);
        String post5Str=Strbuffer.toString().replaceAll("\\s*", "");	
   		System.out.println("紧促的："+post5Str);
   		
   		
   		int s_post5Str=post5Str.indexOf("通信费")+17;
    int e_post5Str=post5Str.indexOf("合计：",s_post5Str+20)-51;
    String result5html=post5Str.substring(s_post5Str, e_post5Str);
 	  System.out.println("初次压缩后的页面:"+result5html);
	
 	 String[] responseStrArray=result5html.split("<tr>");
 	 
 	 regixhtml(responseStrArray, real_name,identity_id,phone_number);
   		
   		
   		
   		
   		
   		//mobilesession.invalidate();//销毁
		}catch(Exception e){
			e.printStackTrace();
			ok=true;
		}
		if(ok){
			result.setCode("1");
			result.setMessage("移动数据获取异常！");
			return result;
		}
		result.setCode("0");
		result.setMessage("移动数据获取正常！");
		return result;
		}
		
		public void regixhtml(String[] responseStrArray, String real_name,String identity_id,String phone_number){
		ResponseMessage result=new ResponseMessage();
		for(int i=1;i<responseStrArray.length;i++){
			System.out.println(responseStrArray.length);
			String lastStr=responseStrArray[i];
			System.out.println("引入方法："+lastStr);
			String[] dataStr=lastStr.split("<td");
			int s_calldate=dataStr[1].indexOf("&nbsp;")+6;
            int e_calldate=dataStr[1].indexOf("<",s_calldate+2);
            String calldate=dataStr[1].substring(s_calldate, e_calldate);
            int s_homeareaName=dataStr[2].indexOf("&nbsp;")+6;
            int e_homeareaName=dataStr[2].indexOf("<",s_homeareaName+2);
            String homeareaName=dataStr[2].substring(s_homeareaName, e_homeareaName);
            int s_calltypeName=dataStr[3].indexOf("&nbsp;")+6;
            int e_calltypeName=dataStr[3].indexOf("<",s_calltypeName+2);
            String calltypeName=dataStr[3].substring(s_calltypeName, e_calltypeName);
            int s_othernum=dataStr[4].indexOf("&nbsp;")+6;
            int e_othernum=dataStr[4].indexOf("<",s_othernum+2);
            String othernum=dataStr[4].substring(s_othernum, e_othernum);
            int s_calllonghour=dataStr[5].indexOf("&nbsp;")+6;
            int e_calllonghour=dataStr[5].indexOf("<",s_calllonghour+2);
            String calllonghour=dataStr[5].substring(s_calllonghour, e_calllonghour);
            int s_romatypeName=dataStr[6].indexOf("&nbsp;")+6;
            int e_romatypeName=dataStr[6].indexOf("<",s_romatypeName+2);
            String romatypeName=dataStr[6].substring(s_romatypeName, e_romatypeName);
            String number_nickname =phone_number+"/"+real_name;                           
            //保存
            XmydCallhistory callhistory=new XmydCallhistory();
            callhistory.setIdentity_id(identity_id);
            callhistory.setCalldate(calldate);
            callhistory.setCalllonghour(calllonghour);
            callhistory.setCalltypeName(calltypeName);
            callhistory.setHomeareaName(homeareaName);
            callhistory.setNumber_nickname(number_nickname);
            callhistory.setOthernum(othernum);
            callhistory.setRomatypeName(romatypeName);		                  
            result=xmydCallhistoryService.saveUnicom(callhistory);
		}
		
	}
}
