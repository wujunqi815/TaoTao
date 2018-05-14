package com.taotao.rest.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class JedisTest {

	@Test
	public void testJedis(){
		//create jedis object
		Jedis jedis = new Jedis("10.16.2.251", 6379);
		jedis.set("test", "hello world");
		String string = jedis.get("test");
		
		System.out.println(string);
		jedis.close();
	}
	
	@Test
	public void testJedisPool(){
		JedisPool pool = new JedisPool("10.16.2.251", 6379);
		Jedis jedis = pool.getResource();
		
		jedis.set("test1", "hello world2");
		String string = jedis.get("test1");
		
		System.out.println(string);
		jedis.close();
		pool.close();
	}
	
	@Test
	public void testSpringJedis(){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml"); 
		
		JedisPool pool = (JedisPool) applicationContext.getBean("redisClient");
		Jedis jedis = pool.getResource();
		
		jedis.set("test3", "hello world3");
		String string = jedis.get("test3");
		
		System.out.println(string);
		jedis.close();
		pool.close();
		
	}
}
