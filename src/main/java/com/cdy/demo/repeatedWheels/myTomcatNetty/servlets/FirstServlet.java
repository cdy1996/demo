package com.cdy.demo.repeatedWheels.myTomcatNetty.servlets;


import com.cdy.demo.repeatedWheels.myTomcatNetty.http.NettyRequest;
import com.cdy.demo.repeatedWheels.myTomcatNetty.http.NettyResponse;
import com.cdy.demo.repeatedWheels.myTomcatNetty.http.AbstractServlet;

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
