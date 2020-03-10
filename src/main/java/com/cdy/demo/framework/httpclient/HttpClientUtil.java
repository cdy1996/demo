package com.cdy.demo.framework.httpclient;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpClientUtil {
	static PoolingHttpClientConnectionManager connectionManager;
	static HttpClient httpClient;

	static{
		try {
		     // 配置同时支持 HTTP 和 HTPPS
		     SSLContextBuilder builder = new SSLContextBuilder();
		     builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		     SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build());
		     Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					 .register("http", PlainConnectionSocketFactory.getSocketFactory())
					 .register("https", sslConnectionSocketFactory).build();
		     connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		 }catch (Exception e){
		     log.error("初始化http 连接池异常",e);
		     connectionManager = new PoolingHttpClientConnectionManager();
		 }
		connectionManager.setMaxTotal(20);
		connectionManager.setDefaultMaxPerRoute(100);
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(1000)
				.setConnectionRequestTimeout(2000)
				.setSocketTimeout(3000).build();
		HttpRequestRetryHandler retryHandler = new StandardHttpRequestRetryHandler();
		httpClient = HttpClientBuilder.create()
				.setDefaultRequestConfig(requestConfig)
				.setRetryHandler(retryHandler)
				.setConnectionManager(connectionManager).build();
	}

	/**
	 * @Title: doGet
	 * @Description: get方式
	 * @param url 请求路径
	 * @param params 参数
	 * @author Mundo
	 */
	public static String doGet(String url, Map<String, String> params) {
 
		// 返回结果
		String result = "";
		// 创建HttpClient对象
		HttpGet httpGet = null;
		try {
			// 拼接参数,可以用URIBuilder,也可以直接拼接在？传值，拼在url后面，如下--httpGet = new
			// HttpGet(uri+"?id=123");
			URIBuilder uriBuilder = new URIBuilder(url);
			if (null != params && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					uriBuilder.addParameter(entry.getKey(), entry.getValue());
					// 或者用
					// 顺便说一下不同(setParameter会覆盖同名参数的值，addParameter则不会)
					// uriBuilder.setParameter(entry.getKey(), entry.getValue());
				}
			}
			URI uri = uriBuilder.build();
			// 创建get请求
			httpGet = new HttpGet(uri);
			log.info("访问路径：" + uri);
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {// 返回200，请求成功
				// 结果返回
				result = EntityUtils.toString(response.getEntity());
				log.info("请求成功！，返回数据：" + result);
			} else {
				log.info("请求失败！");
			}
		} catch (Exception e) {
			log.info("请求失败!");
			log.error(ExceptionUtils.getStackTrace(e));
		} finally {
			// 释放连接
			if (null != httpGet) {
				httpGet.releaseConnection();
			}
		}
		return result;
	}
 
	/**
	 * @Title: doPost
	 * @Description: post请求
	 * @param url
	 * @param params
	 * @return
	 * @author Mundo
	 */
	public static String doPost(String url, Map<String, String> params) {
		String result = "";
		// 创建httpclient对象
		HttpPost httpPost = new HttpPost(url);
		try { // 参数键值对
			if (null != params && !params.isEmpty()) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				NameValuePair pair = null;
				for (String key : params.keySet()) {
					pair = new BasicNameValuePair(key, params.get(key));
					pairs.add(pair);
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs);
				httpPost.setEntity(entity);
			}
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
				log.info("返回数据：>>>" + result);
			} else {
				log.info("请求失败！，url:" + url);
			}
		} catch (Exception e) {
			log.error("请求失败");
			log.error(ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		} finally {
			if (null != httpPost) {
				// 释放连接
				httpPost.releaseConnection();
			}
		}
		return result;
	}
 
	/**
	 * @Title: sendJsonStr
	 * @Description: post发送json字符串
	 * @param url
	 * @param params
	 * @return 返回数据
	 * @author Mundo
	 */
	public static String sendJsonStr(String url, String params) {
		String result = "";
 
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.addHeader("Content-type", "application/json; charset=utf-8");
			httpPost.setHeader("Accept", "application/json");
			if (StringUtils.isNotBlank(params)) {
				httpPost.setEntity(new StringEntity(params, StandardCharsets.UTF_8));
			}
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity());
				log.info("返回数据：" + result);
			} else {
				log.info("请求失败");
			}
		} catch (IOException e) {
			log.error("请求异常");
			log.error(ExceptionUtils.getStackTrace(e));
		}
		return result;
	}
 
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
//		map.put("id", UUID.randomUUID().toString());
		String get = doGet("http://www.zjsti.gov.cn/InnerRun/InnerRunToYPTHandler.ashx?method=getStrainUser&userid=28221851191218848", map);
		System.out.println("get请求调用成功，返回数据是：" + get);
//		String post = doPost("http://localhost:8080/mundo/test", map);
//		System.out.println("post调用成功，返回数据是：" + post);
//		String json = sendJsonStr("http://localhost:8080/mundo/test", "{\"name\":\"David\"}");
//		System.out.println("json发送成功，返回数据是：" + json);
	}
}
