package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.anon.Cache_find;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;

@Service
public class ItemCatServiceImpl implements ItemCatService{
	@Autowired
	private ItemCatMapper itemCatMapper;
	//@Autowired
	private Jedis jedis;
	@Override
	public ItemCat findItemCatById(Long itemCatId) {
		
		return itemCatMapper.selectById(itemCatId);
	}
	/**
	 * VO对象~~~转换~~~POJO
	 * 思路:
	 * 	1.根据parentId查询一级分类信息 list<ItemCat>
	 * 	2.遍历list<ItemCat>集合,获取其中的数据库数据
	 * 	  ~~~~~id/name~~~~~EasyUITree
	 * 	3.EasyUITree~~~~~封装到list集合中返回
	 */
	@Override
	@Cache_find
	public List<EasyUITree> findEasyUITreeByParentId(Long parentId) {
		List<ItemCat> itemCatList = findItemCatByParentId(parentId);
		List<EasyUITree> treeList = new ArrayList<EasyUITree>();
		for (ItemCat itemCat : itemCatList) {
			Long id = itemCat.getId();
			String text = itemCat.getName();
			String state = itemCat.getIsParent()?"closed":"open";
			EasyUITree easyUITree=new EasyUITree(id, text, state);
			//如果是父集,state=closed
			treeList.add(easyUITree);
		}
		return treeList;
	}
	
	public List<ItemCat> findItemCatByParentId(Long parentId){
		QueryWrapper<ItemCat> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("parent_id", parentId);
		List<ItemCat> itemCatList = itemCatMapper.selectList(queryWrapper);
		return itemCatList;
	}
	/**
	 *	 缓存操作流程:
	 *		1.先查缓存
	 *		2.结果为null,说明用户第一次查询应该先查询数据库
	 *		 将数据库数据存到缓存中
	 *		3.结果不为null,json串:将json数据转化为对象 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EasyUITree> findItemCatCache(Long parentId) {
		List<EasyUITree> treeList = new ArrayList<EasyUITree>();
		String key = "ITEM_CAT_" + parentId;
		String result = jedis.get(key);
		if(StringUtils.isEmpty(result)) {
			//用户第一次查询
			System.out.println("从数据库读取数据");
			treeList = findEasyUITreeByParentId(parentId);
			//将数据保存到缓存中
			String value = ObjectMapperUtil.toJSON(treeList);
			jedis.set(key, value);
		}else {
			System.out.println("从redis缓存读取数据");
			treeList = ObjectMapperUtil.toObject(result, treeList.getClass());
		}
		return treeList;
	}
}
