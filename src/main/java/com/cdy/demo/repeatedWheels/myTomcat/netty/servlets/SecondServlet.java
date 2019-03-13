package com.cdy.demo.repeatedWheels.myTomcat.netty.servlets;

import com.alibaba.fastjson.JSON;
import com.cdy.demo.repeatedWheels.myTomcat.netty.http.NettyRequest;
import com.cdy.demo.repeatedWheels.myTomcat.netty.http.NettyResponse;
import com.cdy.demo.repeatedWheels.myTomcat.netty.http.AbstractServlet;

public class SecondServlet extends AbstractServlet {

	@Override
	public void doGet(NettyRequest request, NettyResponse response) {
		doPost(request, response);
	}
	
	@Override
	public void doPost(NettyRequest request, NettyResponse response) {
	    String str = JSON.toJSONString(request.getParameters(),true);  
	    response.write(str,200);
	}
	
}
