package com.cdy.demo.repeatedWheels.myTomcat;

import java.io.IOException;

/**
 * RegiestServlet:注册处理类 
 */
public class RegiestServlet implements HttpServlet {

	@Override
	public void server(HttpRequest request, HttpResponse response) throws IOException {
		response.writerFile("htmlfile/registerSuccess.html");
		
	}

	
}
