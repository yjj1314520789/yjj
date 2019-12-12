package com.jt.service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

public interface ItemService {

	EasyUITable findItemByPage(Integer page, Integer rows);

	void saveItem(Item item,ItemDesc itemDesc);

	void updateItem(Item item,ItemDesc itemDesc);

	void deleteItems(Long[] ids);

	void updateItemStatus(Integer status, Long[] ids);

	Item findItemById(Long id);

	ItemDesc findItemDescById(Long id);


	
	
}
