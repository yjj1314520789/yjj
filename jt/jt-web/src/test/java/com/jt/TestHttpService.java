package com.jt;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jt.util.HttpClientService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestHttpService {
	 
	@Autowired
	private HttpClientService service;
	
	@Test
	public void testJedis() {
		String url = "www.baidu.com";
		Map<String, String> params = new HashMap<>();
		params.put("id", "10086");
		params.put("name", "大圣");
		service.doGet(url, params, null);
	}
	

}
