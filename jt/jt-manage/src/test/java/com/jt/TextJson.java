package com.jt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;

public class TextJson {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	
	@Test
	public void toJson() throws Exception {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(100L)
				.setItemDesc("商品详情")
				.setCreated(new Date())
				.setUpdated(itemDesc.getCreated());
		String json = mapper.writeValueAsString(itemDesc);
		System.out.println(json);
		
		//将json转化维对象
		ItemDesc desc = mapper.readValue(json, ItemDesc.class);
		System.out.println(desc.toString());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testList() throws Exception {
		ItemDesc itemDesc1 = new ItemDesc();
		itemDesc1.setItemId(100L)
				.setItemDesc("商品详情")
				.setCreated(new Date())
				.setUpdated(itemDesc1.getCreated());
		
		ItemDesc itemDesc2 = new ItemDesc();
		itemDesc2.setItemId(100L)
				.setItemDesc("商品详情")
				.setCreated(new Date())
				.setUpdated(itemDesc2.getCreated());
		List list = new ArrayList();
		list.add(itemDesc1);
		list.add(itemDesc2);
		String json = mapper.writeValueAsString(list);
		System.out.println(json);
		
		List<ItemDesc> list2 = mapper.readValue(json,list.getClass());
		System.out.println(list2);
	}
	
}
