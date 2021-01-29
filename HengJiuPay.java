package com.chaos.gaia.pay.sign;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chaos.gaia.pay.util.ALianQiuPayHttpAPIService;
import com.chaos.gaia.pay.util.HttpRequestUtil;
import com.chaos.gaia.pay.util.MD5Utils;
import com.chaos.gaia.pay.util.SendMethod;

public class HengJiuPay extends BasePayPlatform {

	public final static String CHARSET = "UTF-8";

	public HengJiuPay(String keytype, PayInfo vo) {
		super(keytype, vo);
	}

	/**
	 * 回调验签
	 * 
	 * @param vo
	 */
	public HengJiuPay(PayInfo vo) {
		super(vo);
	}

	/**
	 * 回调时使用
	 * 
	 * @param param
	 */
	public HengJiuPay(Map<String, String> paramMap) {
		super(paramMap);
		this.orderNo = paramMap.get("order_no");
	}

	/**
	 * 下单时使用
	 * 
	 * @param param
	 */
	public HengJiuPay(String keytype, Map<String, String> paramMap) {
		super(keytype, paramMap);
		this.orderNo = paramMap.get("order_no");
		if (paramMap.get("amount") != null) {
			this.orderAmount = new BigDecimal(paramMap.get("amount"))
					.toString();
		}
	}

	/**
	 * 微信扫码使用
	 * 
	 * @param param
	 */
	public HengJiuPay(String json) {
		super(json);
		JSONObject jsonobj = JSON.parseObject(json);
		this.orderNo = jsonobj.getString("order_no");
	}

	/**
	 * 封装第三方参数
	 */
	@Override
	public Map<String, String> getPayResult() {

		Map<String, String> metaMap = new LinkedHashMap<String, String>();
		try {
			// 商户ID
			metaMap.put("app_id", vo.getMerchantProMap().get("app_id"));
			// 订单金额
			metaMap.put("amount", orderMoneyToPercent(vo.getOrderAmount()));
			// 商户订单号
			metaMap.put("order_no", vo.getOrderNo());
			// 支付通道编号
			metaMap.put("device", "wechat_wp");// vo.getBankkey()
			// 商户密钥
			String key = vo.getMerchantProMap().get("key");
			metaMap.put("app_secret", key);
			// 异步通知地址
			metaMap.put("notify_url", vo.getNotifyUrl());
			StringBuilder sb = new StringBuilder();
			MD5Utils.makePrePayParams(sb, metaMap, false);
			System.out.println("sign加密前的拼接" + sb);
			// MD5加密
			String sign = MD5Utils.encrypt32(sb.toString());
			// 同步跳转地址
			metaMap.put("return_url", vo.getReturnUrl());
			// 签名加密存入map
			metaMap.put("sign", sign);
			// 1.直接渲染支付视图，2.返回JSON，默认值1，推荐传1或者不传
			metaMap.put("api_type","2");
			metaMap.put("isMobile",vo.getIsMobile());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return metaMap;
	}

	/**
	 * 请求第三方api银行
	 */
	// @Override
	public Map<String, String> getBankHtml(Map<String, String> param) {
		return getQrcode(param);
	}

	/**
	 * 请求第三方api二维码
	 */
	@Override
	public Map<String, String> getQrcode(Map<String, String> param) {
		System.out.println("HengJiuPay--getQrcode--param:"+param.toString());
		Map<String, String> result = new HashMap<String, String>();
		// 移除第三方请求不需要的参数。
		String gateway = param.remove("gateway");
		param.remove("isMobile");
		param.remove("rebackurl");
		param.remove("reqMethod");
		System.out.println("封装请求第三方支付页面所需要的参数");
		param.forEach((key, value) -> {
			System.out.println("HengJiuPay--getQrcode metaMap:" + key + "==" + value);
		});
		
		//通过Form表单提交 的方式		
//		result.put("status", "0000");
//		result.put("reqType", "html");
//		
//		if("Y".equals(isMobile.toUpperCase())) {
//			
//			String url = gateway+"?order_no="+param.get("order_no")+"&amount="+param.get("amount")+
//					"&sign="+param.get("sign")+"&return_url="+param.get("return_url")+"&notify_url="+param.get("notify_url")+"&app_id="+param.get("app_id")+
//					"&device=wechat_wp&app_secret="+param.get("app_secret");
//			System.out.println("HengJiuPay--httpClientUrl="+url);
//			
//			try {
//				
//				RequestConfig config = RequestConfig.custom()
//						.setConnectTimeout(50000)
//						.setConnectionRequestTimeout(10000)
//						.setSocketTimeout(50000)
//		                .setRedirectsEnabled(false).build();
//				
//				CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).build();			                                 
//				List<NameValuePair> form = new ArrayList<>();
//				for(String key: param.keySet()) {
//					form.add(new BasicNameValuePair(key, param.get(key)));
//				}
//				
//				URIBuilder uriBuilder = new URIBuilder(url);
//				uriBuilder.setParameters(form); 
//				HttpGet httpGet = new HttpGet(uriBuilder.build());
//				httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
//		        // 传输的类型
//		        httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
//		 
//		        // 执行请求
//		        int responseCode = 0;
//		        HttpResponse httpResponse = httpClient.execute(httpGet);
//		        if(httpResponse != null) {
//		        	HttpEntity httpEntity = httpResponse.getEntity();
//		        	responseCode = httpResponse.getStatusLine().getStatusCode();
//					System.out.println("HengJiuPay--againRequest--responseCode:"+responseCode);
//					if(responseCode == 302) {
//						
//						Header locationHeader = httpResponse.getFirstHeader("Location");
//						String location = locationHeader.getValue();
//						System.out.println("HengJiuPay--againRequest--location----------->"+location);
//						if(location!=null){
//							
//							URL locationUrl = new URL(location);
//				            String params = locationUrl.getQuery();
//				            String[] psList = params.split("&");
//				            Map<String,String> maps = new HashMap<String,String>();
//				            for(int x =0;x<psList.length;x++){
//				                String[] kv = psList[x].split("=");
//				                maps.put(kv[0],kv[1]);
//				            }
//	
//				            String getQrcodeUrl = "https://p-sdk.yylroa.com/pay/to_wx_repeat.api";
//				            
//				            //===================再进行post的请求===================
//				            HttpPost post = new HttpPost(getQrcodeUrl);
//				            
//				            //创建一个entity模拟一个表单
//				            List<NameValuePair> list = new ArrayList<>();
//				            list.add(new BasicNameValuePair("id",maps.get("id")));
//				            list.add(new BasicNameValuePair("ctime",maps.get("ctime")));
//				            list.add(new BasicNameValuePair("referrer","http://pay.ybtest8888.com/pay/wap/HengJiuPay"));
//				            
//				            //设置消息头
//				            post.addHeader("accept-language", "zh,en-US;q=0.9,en;q=0.8,zh-TW;q=0.7");
//				            post.addHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
//				            post.addHeader("origin", "https://p-sdk.yylroa.com");
//				            post.addHeader("referer", location);
//				            post.addHeader("sec-fetch-mode", "cors");
//				            post.addHeader("sec-fetch-site", "same-origin");
//				            post.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
//				            post.addHeader("x-requested-with", "XMLHttpRequest");
//				            
//				            CloseableHttpClient getQrcodeClient = HttpClients.custom().build();;
//				            //包装成一个entity对象
//				            StringEntity entity = new UrlEncodedFormEntity(list,"utf-8");
//				            //设置请求内容
//				            post.setEntity(entity);
//				            
//				            //执行请求内容
//				            CloseableHttpResponse postResponse = getQrcodeClient.execute(post);
//				            HttpEntity postEntity = postResponse.getEntity();
//				            String postString = EntityUtils.toString(postEntity, "UTF-8");
//				            if(postString!=null) {
//				            	
//				            	System.out.println("HengJiuPay --againPost -- postString"+ postString);
//				            	JSONObject pjson = JSONObject.parseObject(postString);
//				            	String code = pjson.getString("code");
//				            	if("1".equals(code)) {
//				            		JSONObject data = pjson.getJSONObject("data");
//				            		String payUrl = data.getString("pay_info");
//				            		System.out.println("HengJiuPay --againPost -- payUrl"+ payUrl);
//				            		String qrcodeHtml="<p><body><meta http-equiv=\"refresh\" content=\"0; URL='"+payUrl+"'\" /></body></p>";
//					       			result.put("html", qrcodeHtml);
//					       			return result;
//				            	}
//				            	
//				            }    
//						}
//						
//					}else if(responseCode == 200){
//						System.out.println("HengJiuPay--againRequest(200):"+EntityUtils.toString(httpEntity, "UTF-8"));
//					}			
//		        }	
//	
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//					
//		}else {
//			String html = SendMethod.getHtmlFrom(gateway, param);
//			System.out.println("HengJiuPay--getQrcode的请求返回值："+html);
//			html+="<script type=\"text/javascript\" language=\"javascript\"> $(function(){ "
//			  		+ " $('#actform').submit(); }) </script></body></p>";
//		    result.put("html", html);
//		}	
//		return result;
		ALianQiuPayHttpAPIService service = new ALianQiuPayHttpAPIService();
		HashMap<String, Object> map = new HashMap<String, Object>();
		 Iterator<Entry<String,String>> it=param.entrySet().iterator();  
		    while(it.hasNext()) {  
		        Map.Entry<String,String> entry=(Map.Entry<String,String>)it.next();  
		        Object key=entry.getKey();  
		        if(key!=null && param.get(key)!=null) {  
		        	map.put(key.toString(), param.get(key));  
		        }  
		    }  
		try {
			String doGet = service.doGet(gateway, map);
			if (StringUtils.isNotEmpty(doGet)) {
			JSONObject jsonObject = JSON.parseObject(doGet);
			String qrcode_url = jsonObject.getString("qrcode_url");
			
			if (StringUtils.isNotEmpty(qrcode_url)) {
				System.out.println("请求二维码的url---->"+qrcode_url);
				result.put("status", "0000");
				qrcode_url ="<p><body><meta http-equiv=\"refresh\" content=\"0; URL='"+ qrcode_url +"'\" /><span class=\"jumpLink\"></span></body></p>";
				result.put("reqType", "html");
				result.put("html", qrcode_url);
			}	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected boolean callback(Map<String, String> params) {
		System.out.println("HengJiupay--回调参数：" + params.toString());

		// ---------------------------调用查询方法-------------------------
		Map<String, String> dict = new LinkedHashMap<String, String>();
		String key = vo.getMerchantProMap().get("key");
		dict.put("app_id", vo.getMerchantProMap().get("app_id"));
		dict.put("order_no", params.get("order_no"));
		dict.put("app_secret", key);
		StringBuilder sb = new StringBuilder();
		MD5Utils.makePrePayParams(sb, dict, false);
		System.out.println("sign加密前的拼接" + sb);
		// MD5加密
		String sign = MD5Utils.encrypt32(sb.toString());
		dict.put("sign", sign);
		System.out.println("HengJiuPay-queryParam==" + dict.toString());

		String gateway = vo.getGatewayUrl();
		// 发送get请求
		String ret = SendMethod.sendGet(gateway, dict);
		System.out.println("HengJiuPay-queryResult==" + ret.toString());
		if (StringUtils.isNotEmpty(ret)) {
			JSONObject jsonObject = JSON.parseObject(ret);
			String msg = jsonObject.getString("msg");
			String code = jsonObject.getString("code");
			if ("1".equals(code) && "支付已完成".equals(msg)) {
				super.status = 1;
				return true;
			} else {
				super.status = 2;
				return false;
			}
		}
		super.status = 2;
		return false;
	}

	/**
	 * 订单自动处理封装参数 订单查询的参数封装
	 */
	@Override
	public Map<String, String> queryOrderMap() {
		// 顺序拼接
		Map<String, String> dict = new LinkedHashMap<String, String>();

		String key = vo.getMerchantProMap().get("key");
		try {
			dict.put("app_id", vo.getMerchantProMap().get("app_id"));
			dict.put("order_no", vo.getOrderNo());
			dict.put("app_secret", key);
			StringBuilder sb = new StringBuilder();
			MD5Utils.makePrePayParams(sb, dict, false);
			System.out.println("sign加密前的拼接" + sb);
			// MD5加密
			String sign = MD5Utils.encrypt32(sb.toString());
			dict.put("sign", sign);
			Iterator<Entry<String, String>> it = dict.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> pair = it.next();
				System.out.println("HengJiuPay - queryOrderMap:" + pair.getKey()
						+ " = " + pair.getValue());
			}
		} catch (Exception e) {
			throw new RuntimeException("支付验签失败");
		}
		return dict;
	}

	/**
	 * 订单自动处理
	 */
	@Override
	public Map<String, String> queryOrderStatus(Map<String, String> param) {
		Map<String, String> metaMap = new LinkedHashMap<String, String>();
		metaMap.put("app_id", param.get("app_id"));
		metaMap.put("order_no", param.get("order_no"));
		metaMap.put("sign", param.get("sign"));
		Map<String, String> result = new HashMap<String, String>();
		// status 0 未处理，1 成功 2 失败
		result.put("status", "0");
		String gateway = param.remove("gateway");
		try {
			Iterator<Entry<String, String>> it = param.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> pair = it.next();
				System.out.println("HengJiuPay - queryOrderStatus:"
						+ pair.getKey() + " = " + pair.getValue());
			}
			String ret = SendMethod.sendGet(gateway, metaMap);
			System.out.println("HengJiuPay-查询返回参数：" + ret);
			if (ret != null && !"".equals(ret)) {
				JSONObject json = JSONObject.parseObject(ret);
				String msg = json.getString("msg");
				if (json.getString("code").equals("1") && msg.equals("支付已完成")) {
					this.status = 1;
					// 本地测试回调，假设成功
					// if (json.getString("code").equals("0") &&
					// msg.equals("订单支付未完成")) {
					// this.status = 1;
					result.put("order_no", param.get("order_no"));
					result.put("status", "1");
				} else {
					this.status = 2;
					result.put("status", "2");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean validQrcode(Map<String, String> merchantProMap) {

		return true;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public String callNotify(String url, Map<String, String> params) {

		url += String.format(NOTIFY_URL, this.getClass().getSimpleName());

		Iterator it = params.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pair = (Map.Entry) it.next();
			System.out.println("HengJiuPay - callnotify:" + pair.getKey()
					+ " = " + pair.getValue());
		}
		System.out.println("HengJiuPay - callnotify url:" + url);
		return HttpRequestUtil.reqApi(url, params);
	}

	@Override
	public String getSuccessResult() {
		return "success";
	}

	@Override
	public String getFailedResult() {
		return "failed";
	}

	public String makePrePayParams(StringBuilder sb,
			Map<String, String> payParams) {
		List<String> keys = new ArrayList<String>(payParams.keySet());
		for (String key : keys) {
			String str = payParams.get(key);
			if (str == null || str.length() == 0) {
				continue;
			}
			sb.append(key).append("=");
			sb.append(str);
			sb.append("&");
		}
		sb.setLength(sb.length() - 1);

		return sb.toString();
	}

}
