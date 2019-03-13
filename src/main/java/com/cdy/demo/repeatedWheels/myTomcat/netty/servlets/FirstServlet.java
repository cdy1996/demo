package com.cdy.demo.repeatedWheels.myTomcat.netty.servlets;


import com.cdy.demo.repeatedWheels.myTomcat.netty.http.NettyRequest;
import com.cdy.demo.repeatedWheels.myTomcat.netty.http.NettyResponse;
import com.cdy.demo.repeatedWheels.myTomcat.netty.http.AbstractServlet;

public class FirstServlet extends AbstractServlet {

	
	@Override
	public void doGet(NettyRequest request, NettyResponse response) {
		doPost(request, response);
	}

	
	@Override
	public void doPost(NettyRequest request, NettyResponse response) {
		String param = "name";  
	    String str = request.getParameter(param);  
	    response.write(param + ":" + str,200);
	}
	
}
