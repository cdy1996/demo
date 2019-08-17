package com.cdy.demo.repeatedWheels.myproxy;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 手动生成类文件和进行编译
 */
public class Proxy {
	
	private static String ln = "\r\n";
	private static AtomicInteger integer = new AtomicInteger(0);
	
	public static void main(String[] args) {
		IService service = new MyService();
		IService instance = (IService) Proxy.newProxyInstance(new MyClassLoader(Thread.currentThread().getContextClassLoader()),
				new Class[]{IService.class},
				(proxy, method, args1) -> {
					System.out.println("before");
					return method.invoke(service, args1);
				});
		
		instance.doService("hello");
	}
	
	public static Object newProxyInstance(MyClassLoader classLoader, Class<?>[] interfaces, InvocationHandler h){
		try{
			//1 生成类文件
			int num = integer.incrementAndGet();
			
			String className ="$Proxy" + num ;
			String proxySrc = generateSrc(interfaces[0], className);
			
			//2 加载类文件
			String filePath = Proxy.class.getResource("").getPath();
			File f = new File(filePath + className + ".java");
			f.deleteOnExit();
			File classFile = new File(filePath + className + ".class");
			classFile.deleteOnExit();
			
			FileWriter fw = new FileWriter(f);
			fw.write(proxySrc);
			fw.flush();
			fw.close();
		
			//编译类
			JavaCompiler  compiler = ToolProvider.getSystemJavaCompiler();
			StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
			Iterable iterable = manager.getJavaFileObjects(f);
			
			CompilationTask task = compiler.getTask(null, manager, null, null, null, iterable);
			task.call();
			manager.close();
			//4. 类加载器加载
			Class proxyClass = classLoader.findClass(className);
			Constructor c = proxyClass.getConstructor(InvocationHandler.class);
			f.delete();

			return c.newInstance(h);
			
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * package com.cdy.demo.repeatedWheels.myproxy;
	 * public class $Proxy1 implements com.cdy.demo.repeatedWheels.myproxy.IService{
	 * com.cdy.demo.repeatedWheels.myproxy.InvocationHandler h;
	 * public $Proxy1(com.cdy.demo.repeatedWheels.myproxy.InvocationHandler h) {
	 * 		this.h = h;
	 * }
	 * public java.lang.String doService(java.lang.String arg0){
	 * 		try{
	 * 			java.lang.reflect.Method m = com.cdy.demo.repeatedWheels.myproxy.IService.class.getMethod("doService",new java.lang.Class[]{java.lang.String.class});
	 * 			return (java.lang.String)this.h.invoke(this,m,new java.lang.Object[]{arg0});
	 * 		}catch(java.lang.Throwable e){throw new java.lang.RuntimeException(e);}
	 * }
	 * }
	 * @param interfaces
	 * @param className
	 * @return
	 */
	private static String generateSrc(Class<?> interfaces, String className){
		StringBuilder src = new StringBuilder();
		String name = MyClassLoader.class.getPackage().getName();
		src.append("package ").append(name).append(";").append(ln);
//		src.append("import java.lang.reflect.Method;").append(ln);
		src.append("public class ").append(className).append(" implements ").append(interfaces.getName()).append("{").append(ln);
		
		src.append(InvocationHandler.class.getName()).append(" h;").append(ln);
		
		src.append("public ").append(className).append("(").append(InvocationHandler.class.getName()).append(" h) {").append(ln);
		src.append("this.h = h;").append(ln);
		src.append("}").append(ln);
		
		for (Method m : interfaces.getMethods()) {
			StringBuilder params = new StringBuilder();
			StringBuilder params2 = new StringBuilder();
			StringBuilder classes = new StringBuilder();
			for (Parameter parameter : m.getParameters()) {
				params.append(parameter.getType().getName()).append(" ").append(parameter.getName()).append(",");
				classes.append(parameter.getType().getName()).append(".class").append(",");
				params2.append(parameter.getName()).append(",");
			}
			params.deleteCharAt(params.lastIndexOf(","));
			params2.deleteCharAt(params2.lastIndexOf(","));
			classes.deleteCharAt(classes.lastIndexOf(","));
			String returnType = m.getReturnType().getName();
			src.append("public ").append(returnType).append(" ")
					.append(m.getName()).append("(").append(params.toString()).append("){").append(ln);
			
			src.append("try{").append(ln);
			src.append("java.lang.reflect.Method m = ").append(interfaces.getName()).append(".class.getMethod(\"")
					.append(m.getName()).append("\",new java.lang.Class[]{").append(classes).append("});").append(ln);
			if (!returnType.contains("void")) {
				src.append("return ").append("(").append(returnType).append(")");
			}
			src.append("this.h.invoke(this,m,").append("new java.lang.Object[]{").append(params2).append("}").append(");").append(ln);
			src.append("}catch(java.lang.Throwable e){throw new java.lang.RuntimeException(e);}").append(ln);
			src.append("}").append(ln);
		}
		
		src.append("}");
		
		String s = src.toString();
		System.out.println(s);
		return s;
	}
}
