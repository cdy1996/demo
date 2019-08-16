package com.cdy.demo.repeatedWheels.myTomcatNetty.servlets;

import com.alibaba.fastjson.JSON;
import com.cdy.demo.repeatedWheels.myTomcatNetty.http.NettyRequest;
import com.cdy.demo.repeatedWheels.myTomcatNetty.http.NettyResponse;
import com.cdy.demo.repeatedWheels.myTomcatNetty.http.AbstractServlet;

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
