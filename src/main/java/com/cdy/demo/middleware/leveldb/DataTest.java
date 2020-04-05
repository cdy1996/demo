package com.cdy.demo.middleware.leveldb;

import com.cdy.demo.framework.spring.User;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.WriteOptions;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DataTest {
 
	private static final String PATH = "/data/leveldb";
	private static final File FILE = new File(PATH);
	private static final Charset CHARSET = Charset.forName("UTF-8");
	
	@Test
	public void writeObject() {
		Options options = new Options();
		DBFactory factory = Iq80DBFactory.factory;
		DB db = null;
		try {
			db = factory.open(FILE, options);
			User user = new User();
			user.setUsername("admin");
			user.setSex(true);
			WriteOptions writeOptions = new WriteOptions();
			writeOptions.snapshot(true);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(user);
			
			db.put(user.getUsername().getBytes(CHARSET), baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				try {
					db.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	public void readObject() {
		Options options = new Options();
		DBFactory factory = Iq80DBFactory.factory;
		DB db = null;
		try {
			db = factory.open(FILE, options);
			byte[] valueByte = db.get("admin".getBytes(CHARSET));
			ByteArrayInputStream bais = new ByteArrayInputStream(valueByte);
			ObjectInputStream ois = new ObjectInputStream(bais);
			User user = (User) ois.readObject();
			System.out.println(user);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (db != null) {
				try {
					db.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	public void writeObjectList() {
		Options options = new Options();
		DBFactory factory = Iq80DBFactory.factory;
		DB db = null;
		try {
			db = factory.open(FILE, options);
			List<User> userList = new ArrayList<User>();
			User user = new User();
			user.setUsername("admin");
			user.setSex(true);
			User user2 = new User();
			user2.setUsername("root");
			user2.setSex(false);
			userList.add(user);
			userList.add(user2);
			WriteOptions writeOptions = new WriteOptions();
			writeOptions.snapshot(true);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(userList);
			
			db.put("userList".getBytes(CHARSET), baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				try {
					db.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Test
	public void readObjectList() {
		Options options = new Options();
		DBFactory factory = Iq80DBFactory.factory;
		DB db = null;
		try {
			db = factory.open(FILE, options);
			byte[] valueByte = db.get("userList".getBytes(CHARSET));
			ByteArrayInputStream bais = new ByteArrayInputStream(valueByte);
			ObjectInputStream ois = new ObjectInputStream(bais);
			List<User> userList = new ArrayList<User>();
			userList = (List<User>) ois.readObject();
			System.out.println(userList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (db != null) {
				try {
					db.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}