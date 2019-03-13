package com.cdy.demo.repeatedWheels.myTomcat.netty.server;

import com.cdy.demo.repeatedWheels.myTomcat.netty.CustomConfig;
import com.cdy.demo.repeatedWheels.myTomcat.netty.http.NettyRequest;
import com.cdy.demo.repeatedWheels.myTomcat.netty.http.NettyResponse;
import com.cdy.demo.repeatedWheels.myTomcat.netty.http.AbstractServlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;


public class RequestHandler extends ChannelInboundHandlerAdapter {

	private Logger LOG = Logger.getLogger(RequestHandler.class);
	
    private static final Map<Pattern,Class<?>> servletMapping = new HashMap<Pattern,Class<?>>();
    
    static{
    	
    		CustomConfig.load("web.properties");
    	
    		for (String key : CustomConfig.getKeys()) {
			if(key.startsWith("servlet")){
				String name = key.replaceFirst("servlet.", "");
				if(name.indexOf(".") != -1){
					name = name.substring(0,name.indexOf("."));
				}else{
					continue;
				}
				String pattern = CustomConfig.getString("servlet." + name + ".urlPattern");
				pattern = pattern.replaceAll("\\*", ".*");
				String className = CustomConfig.getString("servlet." + name + ".className");
				if(!servletMapping.containsKey(pattern)){
					try {
						servletMapping.put(Pattern.compile(pattern), Class.forName(className));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
    }
  
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
        if (msg instanceof io.netty.handler.codec.http.HttpRequest) {
        		io.netty.handler.codec.http.HttpRequest r = (io.netty.handler.codec.http.HttpRequest) msg;
        		NettyRequest request = new NettyRequest(ctx,r);
        		NettyResponse response = new NettyResponse(ctx,r);
            String uri = request.getUri();
            String method = request.getMethod();
  
            LOG.info(String.format("Uri:%s method %s", uri, method));  
  
            boolean hasPattern = false;
            for (Entry<Pattern, Class<?>> entry : servletMapping.entrySet()) {
            		if (entry.getKey().matcher(uri).matches()) {  
	            		AbstractServlet servlet = (AbstractServlet)entry.getValue().newInstance();
	            		if("get".equalsIgnoreCase(method)){
	            			servlet.doGet(request, response);
	            		}else{
	            			servlet.doPost(request, response);
	            		}
	            		hasPattern = true;
                }
			}
            
            if(!hasPattern){  
                String out = String.format("404 NotFound URL%s for method %s", uri,method);  
                response.write(out,404);  
                return;  
            }  
        }  
    }  
  
    @Override  
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {  
        ctx.flush();  
    }  
  
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {  
        cause.printStackTrace();
        ctx.close();  
    }  
}