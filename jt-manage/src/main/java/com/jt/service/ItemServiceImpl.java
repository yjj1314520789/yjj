package com.jt.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;
	@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		//1.获取商品的记录总数
		int total=itemMapper.selectCount(null);
		/**2.获取分页后的数据
		 * sql:select * from tb_item limit 起始页下标,查询的页数
		 * 	第一页:
		 * 	select * from tb_item limit 0,20
		 * 	第二页:
		 *  select * from tb_item limit 20,20
		 * 	第n页
		 * 	select * from tb_item limit (n-1)*20,20
		 */
		int start=(page-1)*rows;
		List<Item> itemList=itemMapper.findItemByPage(start,rows);
		
//		EasyUITable table = new EasyUITable();
//		table.setTotal(total);
//		table.setRows(rows);
		return new EasyUITable(total, itemList);
	}
	@Transactional//控制事务
	@Override
	public void saveItem(Item item,ItemDesc itemDesc) {
		item.setStatus(1).setCreated(new Date()).setUpdated(item.getCreated());
		//mybatis特性:数据库操作中,主键自增后都会自动的回填主键信息
		itemMapper.insert(item);
//		int a=1/0;
		//新增商品详情信息
		itemDesc.setItemId(item.getId())
				.setCreated(item.getCreated())
				.setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
	}
	@Transactional
	@Override
	public void updateItem(Item item,ItemDesc itemDesc) {
		item.setUpdated(new Date());
		itemMapper.updateById(item);
		
		itemDesc.setItemId(item.getId())
				.setUpdated(new Date());
		itemDescMapper.updateById(itemDesc);
	}
	@Transactional
	@Override
	public void deleteItems(Long[] ids) {
		//将数组转化成list集合
		List<Long> idList = Arrays.asList(ids);
		itemMapper.deleteBatchIds(idList);
		
		itemDescMapper.deleteBatchIds(idList);
	}
	//sql:delete from tb_item where id in 
	@Override
	public void updateStatus(int status, Long[] ids) {
		Item item = new Item();
		for (Long id : ids) {
			item.setId(id)
				.setStatus(status)
				.setUpdated(new Date());
			itemMapper.updateById(item);
		}
	}	
	//sql:update tb_item set status=#{status},updated=#{date} where id in 
	@Override
	public ItemDesc findItemDescById(Long itemId) {
		
		return itemDescMapper.selectById(itemId);
	}
	@Override
	public Item findItemById(Long id) {
		return itemMapper.selectById(id);
	}
}
