package org.xmyd.app.backstage.controller.phone;



import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xmyd.app.backstage.entity.XmydCallhistory;
import org.xmyd.app.backstage.ourtools.ResponseMessage;
import org.xmyd.app.backstage.service.loan.XmydLoanService;
import org.xmyd.app.backstage.service.other.XmydCallhistoryService;

/**
 * 湖北电信的数据的获取
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/info")
public class LoginHBTelecom2 {
	
	

	@Autowired
	private XmydLoanService loanService;
	
	@Autowired
	private HttpSession loansession;
	
	@Autowired
	private XmydCallhistoryService xmydCallhistoryService;
	
	@Autowired
	private HttpSession telecomsession;
	
	@RequestMapping("/telecom1.do")
	@ResponseBody
	public ResponseMessage telecom1(HttpServletRequest request,HttpServletResponse response) throws ParseException, IOException, NoSuchMethodException, ScriptException{
		ResponseMessage result=new ResponseMessage();
		String phone=request.getParameter("phone_number");
		String jsUrl=request.getServletContext().getRealPath("scripts/ASE.js");
		ScriptEngineManager manager = new ScriptEngineManager();   
		ScriptEngine scriptEngine = manager.getEngineByName("javascript");
		
		FileReader reader = new FileReader(jsUrl);
		scriptEngine.eval(reader);
		Invocable invoke = (Invocable)scriptEngine;
		String password=(String) invoke.invokeFunction("pdwl", request.getParameter("phone_pwd"));
		//String password="3kqUcdKKep1xBFsulKkzdw==";
		System.out.println("password:"+password);
		
		CookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		
		List<String> cookieList=new ArrayList<String>();
		
		String url="http://login.189.cn/captcha?source=login&width=100&height=37";
		
		HttpGet get1=new HttpGet(url);
		HttpResponse resultget1=httpclient.execute(get1);
		
		for (Header head : resultget1.getAllHeaders()) {		
            System.out.println("resultget1的消息头:"+head);
            if("Set-Cookie".equals(head.getName())){
            	String value = head.getValue();
            	cookieList.add(value.split(";")[0]);
            	
            }
      }	
		String cookie=StringUtils.join(cookieList.toArray(), ";");
		System.out.println(cookie);
		
		
		String url1="http://login.189.cn/login/ajax";
		
		HttpPost post1=new HttpPost(url1);
		
		post1.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
		post1.setHeader("Accept","application/json, text/javascript, */*; q=0.01");
		post1.setHeader("AllowAutoRedirect","false");
		post1.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		post1.setHeader("Accept-Language","zh-CN");
		post1.setHeader("Accept-Encoding","gzip,deflate");
		post1.setHeader("x-requested-with","XMLHttpRequest");
		post1.setHeader("XMLHttpRequest","no-cache");
		
		List<NameValuePair> params1=new ArrayList<NameValuePair>();		
		params1.add(new BasicNameValuePair("m","checkphone"));
		params1.add(new BasicNameValuePair("phone",phone));
		
		UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params1);
		post1.setEntity(entity);
		
		CloseableHttpResponse response1=httpclient.execute(post1);
		
		
		if(response1.getStatusLine().getStatusCode()==200){
			for (Header head : response1.getAllHeaders()) {
	              System.out.println(head);
	              if("Set-Cookie".equals(head.getName())){
	              	String value = head.getValue();
	              	cookieList.add(value.split(";")[0]);
	              }
	          }
		}
		
		
		
		
		
		
		
		
		
		
		
		InputStream resStr=resultget1.getEntity().getContent();	
		String osUrl =request.getServletContext().getRealPath("aaa");
		File file = new File (osUrl);
        if (file.isDirectory ())
        {
        	osUrl=file.getAbsolutePath () + File.separator + file.getName () +".png";
        }
		OutputStream os = new FileOutputStream(osUrl);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = resStr.read(buffer, 0, 8192)) != -1) {
		os.write(buffer, 0, bytesRead);
		}
		os.close();
		resStr.close();
		
		System.out.println("*********1**********");
		String url2="http://login.189.cn/login";
		System.out.println("*********2**********");
		HttpPost post2=new HttpPost(url2);
		System.out.println("*********3**********");
		post2.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		post2.setHeader("Accept-Encoding", "gzip, deflate");
		post2.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
		post2.setHeader("Connection", "keep-alive");
		post2.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
		post2.setHeader("Upgrade-Insecure-Requests", "1");
		post2.setHeader("Content-Type", "application/x-www-form-urlencoded");
		System.out.println("*********4**********");
		List<NameValuePair> params2=new ArrayList<NameValuePair>();	
		System.out.println("*********5**********");
		params2.add(new BasicNameValuePair("Account",phone));
		params2.add(new BasicNameValuePair("UType","201"));
		params2.add(new BasicNameValuePair("ProvinceID","18"));
		params2.add(new BasicNameValuePair("AreaCode",""));
		params2.add(new BasicNameValuePair("CityNo",""));
		params2.add(new BasicNameValuePair("RandomFlag","0"));
		params2.add(new BasicNameValuePair("Password",password));
		params2.add(new BasicNameValuePair("Captcha",""));//verifycode
		System.out.println("*********6**********");
		
		post2.setEntity(new UrlEncodedFormEntity(params2));
		System.out.println("*********7**********");
		
		CloseableHttpResponse response2=httpclient.execute(post2);//CloseableHttpResponse
		System.out.println("*********8**********");
		int aa=response2.getStatusLine().getStatusCode();
		System.out.println("*********9**********");
		String resulthtml=EntityUtils.toString(response2.getEntity());
		System.out.println("*********10**********");
		System.out.println(resulthtml);
		
		System.out.println(aa);
		if(aa==301||aa==302){
			for (Header head : response2.getAllHeaders()) {
	            System.out.println("登录之后的消息头:"+head);
	            if("Set-Cookie".equals(head.getName())){
	            	String value = head.getValue();
	            	cookieList.add(value.split(";")[0]);
	            }
	        }
			
			
			Header header =response2.getFirstHeader("Location");
			if(header != null){
				//https://account.chsi.com.cn/account/accountroadmap.action
				url = header.getValue();
				System.out.println("-----url get1: "+url);
				HttpGet getlloca = new HttpGet(url);
				CloseableHttpResponse login=httpclient.execute(getlloca);
				//??????需要设置消息头
				//System.out.println(EntityUtils.toString(login.getEntity()));
			}	
		}
		
		
		String url3="http://www.189.cn/login/index.do";		
		HttpGet loginget2=new HttpGet(url3);
		loginget2.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		loginget2.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		loginget2.addHeader("KeepAlive", "true");
		loginget2.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		loginget2.addHeader("Accept-Language", "zh-CN");
		loginget2.addHeader("Accept-Encoding", "gzip, deflate");
		loginget2.addHeader("X-Requested-With", "XMLHttpRequest");
		
		CloseableHttpResponse loginresult2=httpclient.execute(loginget2);
		System.out.println(loginresult2);
		System.out.println(EntityUtils.toString(loginresult2.getEntity()));
		int logg=loginresult2.getStatusLine().getStatusCode();
		System.out.println(logg);
		for (Header head : loginresult2.getAllHeaders()) {
            System.out.println("游客之前的消息头:"+head);
            if("Set-Cookie".equals(head.getName())){
            	String value = head.getValue();
            	cookieList.add(value.split(";")[0]);
            }
        }
		
		
		String urls="http://www.189.cn/dqmh/ssoLink.do?method=linkTo&platNo=10018&toStUrl=http://hb.189.cn/SSOtoWSSNew?toWssUrl=/pages/selfservice/feesquery/detailListQuery.jsp&trackPath=SYleftDH";
		
		HttpGet loginget3=new HttpGet(urls);
		loginget2.addHeader("Accept", "text/html, application/xhtml+xml, */*");
		loginget2.addHeader("KeepAlive", "true");
		loginget2.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET4.0C; .NET4.0E; .NET CLR 3.5.30729; .NET CLR 3.0.30729)");
		loginget2.addHeader("Accept-Language", "zh-CN");
		loginget2.addHeader("Accept-Encoding", "gzip, deflate");
		CloseableHttpResponse loginresult3=httpclient.execute(loginget3);
		System.out.println(loginresult3);
		System.out.println(EntityUtils.toString(loginresult3.getEntity()));
		int ll=loginresult2.getStatusLine().getStatusCode();
		System.out.println(ll);
		for (Header head : loginresult3.getAllHeaders()) {
            System.out.println("长段消息头:"+head);
            if("Set-Cookie".equals(head.getName())){
            	String value = head.getValue();
            	cookieList.add(value.split(";")[0]);
            }
        }
				
		String url4="http://hb.189.cn/ajaxServlet/getCityCodeAndIsLogin";
		HttpPost loginpost4=new HttpPost(url4);
		loginpost4.addHeader("Content-Type", "application/x-www-form-urlencoded");
		loginpost4.addHeader("Accept", "text/html,application/xhtml+xml,application/xml,*/*");
		loginpost4.addHeader("AllowAutoRedirect", "false");
		loginpost4.addHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/7.0)");
		loginpost4.addHeader("KeepAlive", "true");
		loginpost4.addHeader("Accept-Language", "zh-CN");
		loginpost4.addHeader("Accept-Encoding", "gzip,deflate");
		loginpost4.addHeader("Cache-Control", "no-cache");
		
		List<NameValuePair> params4=new ArrayList<NameValuePair>();		
		params4.add(new BasicNameValuePair("method","getCityCodeAndIsLogin"));
		
		loginpost4.setEntity(new UrlEncodedFormEntity(params4));
		
		CloseableHttpResponse response4=httpclient.execute(loginpost4);

		String loginhtml1=EntityUtils.toString(response4.getEntity());
		int s_citycode=loginhtml1.indexOf("CITYCODE\":\"")+11;
		int e_citycode=loginhtml1.indexOf("\"",s_citycode+2);
		String cityCode=loginhtml1.substring(s_citycode, e_citycode);
		System.out.println(cityCode);
		
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
		
		
		String url5="http://hb.189.cn/feesquery_PhoneIsDX.action";
		
		HttpPost logpost5=new HttpPost(url5);
		
		logpost5.addHeader("Content-Type", "application/x-www-form-urlencoded");
		logpost5.addHeader("Accept", "text/html,application/xhtml+xml,application/xml,*/*");
		logpost5.addHeader("AllowAutoRedirect", "false");
		logpost5.addHeader("KeepAlive", "true");
		logpost5.addHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/7.0)");
		logpost5.addHeader("Accept-Language", "zh-CN");
		logpost5.addHeader("Accept-Encoding", "gzip,deflate");
		logpost5.addHeader("Cache-Control", "no-cache");
        
		List<NameValuePair> params5=new ArrayList<NameValuePair>();		
		params5.add(new BasicNameValuePair("productNumber",phone));
		params5.add(new BasicNameValuePair("cityCode",cityCode));
		params5.add(new BasicNameValuePair("sentType","C"));
		params5.add(new BasicNameValuePair("ip",ip));
		
		
		logpost5.setEntity(new UrlEncodedFormEntity(params5));		
		CloseableHttpResponse response5=httpclient.execute(logpost5);
		System.out.println(response5);
		
		
		String coderesult=EntityUtils.toString(response5.getEntity());
		int s_repayresult=coderesult.indexOf("请注意查收");
		
		if(s_repayresult==-1){
			result.setCode("1");
			result.setMessage("获取短信失败，请重新提交！");
			return result;
		}
		
		telecomsession.setAttribute("cookie1", cookieStore);
		
		result.setCode("0");
		result.setMessage("获取短信验证成功！");
		return result;
		
	}
	
	
	@RequestMapping("/telecom2.do")
	@ResponseBody
	public ResponseMessage loginunicom2(HttpServletRequest request,HttpServletResponse respone) throws ClientProtocolException, IOException{
		
		ResponseMessage result=new ResponseMessage();
		String phone_number=request.getParameter("phone_number");
		String verifycode=request.getParameter("verifycode");
		String identity_id=request.getParameter("identity_id");
		String real_name=request.getParameter("real_name");
		

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
		
		
		
		
		System.out.println("phone_number:"+phone_number);
		System.out.println("verifycode:"+verifycode);
		System.out.println("identity_id:"+identity_id);
		System.out.println("real_name:"+real_name);
		
		
		List<String> cookieList=new ArrayList<String>();
		CookieStore cookieStore = new BasicCookieStore();
		cookieStore = (CookieStore)telecomsession.getAttribute("cookie1");
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		String url1="http://hb.189.cn/validateWhiteList.action";
		
		HttpPost loginpost1=new HttpPost(url1);
		loginpost1.addHeader("Content-Type", "application/x-www-form-urlencoded");
		loginpost1.addHeader("Accept", "text/html,application/xhtml+xml,application/xml,*/*");
		loginpost1.addHeader("AllowAutoRedirect", "false");
		loginpost1.addHeader("KeepAlive", "true");
		loginpost1.addHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/7.0)");
		loginpost1.addHeader("Accept-Language", "zh-CN");
		loginpost1.addHeader("Accept-Encoding", "gzip,deflate");
		loginpost1.addHeader("Cache-Control", "no-cache");
		
		
		List<NameValuePair> params1=new ArrayList<NameValuePair>();		
		params1.add(new BasicNameValuePair("accnbr",phone_number));
		
		loginpost1.setEntity(new UrlEncodedFormEntity(params1));		
		CloseableHttpResponse response1=httpclient.execute(loginpost1);
		
		String loghtml=EntityUtils.toString(response1.getEntity());
		System.out.println(response1);
		System.out.println(loghtml);
		
		for (Header head : response1.getAllHeaders()) {
            System.out.println("长段消息头:"+head);
            if("Set-Cookie".equals(head.getName())){
            	String value = head.getValue();
            	cookieList.add(value.split(";")[0]);
            }
        }
		
		String url2="http://hb.189.cn/feesquery_checkCDMAFindWeb.action";
//			参数 Parameters.Add(new WebApiParameter("random", collection["ValidateNum"]));//textBox4.Text  验证码
//			                Parameters.Add(new WebApiParameter("sentType", "C"));
		
		HttpPost post2=new HttpPost(url2);
		post2.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post2.addHeader("Accept", "text/html,application/xhtml+xml,application/xml,*/*");
		post2.addHeader("AllowAutoRedirect", "false");
		post2.addHeader("KeepAlive", "true");
		post2.addHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/7.0)");
		post2.addHeader("Accept-Language", "zh-CN");
		post2.addHeader("Accept-Encoding", "gzip,deflate");
		post2.addHeader("Cache-Control", "no-cache");
		
		List<NameValuePair> params2=new ArrayList<NameValuePair>();		
		params2.add(new BasicNameValuePair("random",verifycode));
		params2.add(new BasicNameValuePair("sentType","C"));
		
		post2.setEntity(new UrlEncodedFormEntity(params2));		
		CloseableHttpResponse response2=httpclient.execute(post2);
		String loginhtml2=EntityUtils.toString(response2.getEntity());
		System.out.println("对象2:"+response2);
		System.out.println("对象页面2:"+loginhtml2);
		
		if(loginhtml2.equals("0")){
			System.out.println("****************************");
			result.setCode("1");
			result.setMessage("您输入的验证码错误！请重新输入！");
			return result;
		}
		telecomsession.setAttribute("cookie2", cookieStore);
		
		String billcycle;
		for(int m=0;m<6;m++){
	  		Calendar   c   =   Calendar.getInstance(Locale.CHINA); 				
			c.add(Calendar.MONTH, -m);
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");  
			c.add(Calendar.MONTH, 0);  
	        c.set(Calendar.DAY_OF_MONTH, 1);  
	        String beginDate = format.format(c.getTime());
	        billcycle=beginDate.substring(0, 6);
	        System.out.println( billcycle);
	        String startMonth=billcycle+"0000";	     	        
	        result=regixhtml(identity_id,phone_number,real_name,verifycode,startMonth);	        
		}
		
//			System.out.println("identity_id:"+identity_id);
//			double loan_amount=(double) loansession.getAttribute("loan_amount");
//			System.out.println("loan_amount:"+loan_amount);
//			int loan_period=(int) loansession.getAttribute("loan_period");
//			System.out.println("loan_period:"+loan_period);
//			//String loan_ip=(String) loansession.getAttribute("loan_ip");
//			System.out.println("loan_ip:"+loan_ip);
//			
//			result=loanService.loanconfirm(identity_id,loan_amount,loan_period,loan_ip);
		
		return result;
	}
	
	public ResponseMessage loadinghtml(String[] responseStrArray,String phone_number,String identity_id,String real_name){
		ResponseMessage result=new ResponseMessage();
//		String identity_id="xmyd18007132173";
//		String real_name="鲍丹";
		System.out.println("+++++++++++++++");
		for(int i=1;i<responseStrArray.length;i++){
			System.out.println(responseStrArray.length);
			String lastStr=responseStrArray[i];
			//System.out.println("引入方法："+lastStr);
			
			String[] dataStr=lastStr.split("<td");
			
			int s_calldate=dataStr[1].indexOf(">")+1;
			int e_calldate=dataStr[1].lastIndexOf("<");
			String calldate=dataStr[1].substring(s_calldate, e_calldate);
			
			int s_othernum=dataStr[3].indexOf(">")+1;
			int e_othernum=dataStr[3].lastIndexOf("<");
			String othernum=dataStr[3].substring(s_othernum, e_othernum);
			
			int s_calllonghour=dataStr[4].indexOf(">")+1;
			int e_calllonghour=dataStr[4].lastIndexOf("<");
			String intcalllonghour=dataStr[4].substring(s_calllonghour, e_calllonghour);
			int calllonghours=0;
			String calllonghour=null;
			try{
			 calllonghours=Integer.parseInt(intcalllonghour);
			 calllonghour=cal(calllonghours);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			String number_nickname=phone_number+"/"+real_name;
			
			int s_calltypeName=dataStr[6].indexOf(">")+1;
			int e_calltypeName=dataStr[6].lastIndexOf("<");
			String calltypeName=dataStr[6].substring(s_calltypeName, e_calltypeName);
			
			int s_homeareaName=dataStr[8].indexOf(">")+1;
			int e_homeareaName=dataStr[8].lastIndexOf("<");
			String homeareaName=dataStr[8].substring(s_homeareaName, e_homeareaName);
			
			int s_romatypeName=dataStr[7].indexOf(">")+1;
			int e_romatypeName=dataStr[7].lastIndexOf("<");
			String romatypeName=dataStr[7].substring(s_romatypeName, e_romatypeName);
			
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
		return result;
	}
	
	public static String cal(int second){
		String  html="0秒";  
	        if(second!=0){  
	        	Integer s=second;  
	            String format;  
	            Object[] array;  
	            Integer hours =(int) (s/(60*60));  
	            Integer minutes = (int) (s/60-hours*60);  
	            Integer seconds = (int) (s-minutes*60-hours*60*60);  
	            if(hours>0){  
	                format="%1$,d时%2$,d分%3$,d秒";  
	                array=new Object[]{hours,minutes,seconds};  
	            }else if(minutes>0){  
	                format="%1$,d分%2$,d秒";  
	                array=new Object[]{minutes,seconds};  
	            }else{  
	                format="%1$,d秒";  
	                array=new Object[]{seconds};  
	            }  
	            html= String.format(format, array);  
	        }
	        return html;
		 }
	
	public ResponseMessage regixhtml(String identity_id,String phone_number,String real_name,String verifycode,String startMonth) throws ClientProtocolException, IOException{
		ResponseMessage result=new ResponseMessage();
		List<String> cookieList=new ArrayList<String>();
		CookieStore cookieStore = new BasicCookieStore();
		cookieStore = (CookieStore)telecomsession.getAttribute("cookie2");
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

		String url3="http://hb.189.cn/feesquery_querylist.action";
		HttpPost post3=new HttpPost(url3);
		post3.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post3.addHeader("Accept", "text/html,application/xhtml+xml,application/xml,*/*");
		post3.addHeader("AllowAutoRedirect", "false");
		post3.addHeader("KeepAlive", "true");
		post3.addHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/7.0)");
		post3.addHeader("Accept-Language", "zh-CN");
		post3.addHeader("Accept-Encoding", "gzip,deflate");
		post3.addHeader("Cache-Control", "no-cache");
		
		List<NameValuePair> params3=new ArrayList<NameValuePair>();		
		params3.add(new BasicNameValuePair("startMonth",startMonth));
		params3.add(new BasicNameValuePair("type","1"));
		params3.add(new BasicNameValuePair("random",verifycode));
		params3.add(new BasicNameValuePair("pagecount","1000"));
		
		post3.setEntity(new UrlEncodedFormEntity(params3));		
		CloseableHttpResponse response3=httpclient.execute(post3);
		String loginhtml3=EntityUtils.toString(response3.getEntity());
		//System.out.println("对象2:"+response3);
		//System.out.println("对象页面2:"+loginhtml3);
		
		int s_html=loginhtml3.indexOf("小计")+12;
		int e_html=loginhtml3.indexOf("查询时间",s_html+5);
		String resulthtml1=loginhtml3.substring(s_html, e_html);
		//System.out.println("resulthtml1:"+resulthtml1);
		
		String[] responseStrArray=resulthtml1.split("<tr");
		int a=responseStrArray.length;
		System.out.println(a);
		responseStrArray=Arrays.copyOf(responseStrArray, responseStrArray.length-1);
		System.out.println(responseStrArray.length);
		result=loadinghtml( responseStrArray,phone_number,identity_id,real_name);
		
		return result;
	}

}
