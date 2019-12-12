package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;

@RestController
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@RequestMapping("/query")
	public EasyUITable findItemByPage(
			Integer page,Integer rows) {
		return itemService.findItemByPage(page,rows);
	}
	/**
	 * 业务要求:
	 * 	url:http://lockhost:8091/item/save
	 * 	参数:id=1&title=112312
	 * 	返回值:sysResult 200 201
	 */
	@RequestMapping("/save")
	public SysResult saveItem(Item item,ItemDesc itemDesc) {

		itemService.saveItem(item,itemDesc);
		return SysResult.success();
	}
	/**
	 * 商品更新
	 * @param item
	 * @param itemDesc
	 * @return
	 */
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {
		itemService.updateItem(item,itemDesc);
		return SysResult.success();
	}
	/**
	 * 商品删除
	 */
	@RequestMapping("/delete")
	public SysResult deleteItems(Long[] ids) {
		itemService.deleteItems(ids);
		return SysResult.success();
	}
	/**
	 * 下架
	 */
	@RequestMapping("/instock")
	public SysResult instock(Long[] ids) {
		int status=2;
		itemService.updateStatus(status,ids);
		return SysResult.success();
	}
	/**
	 * 上架
	 */
	@RequestMapping("/reshelf")
	public SysResult reshelf(Long[] ids) {
		int status=1;
		itemService.updateStatus(status,ids);
		return SysResult.success();
	}
	/**
	 * 商品详情返回
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescById(@PathVariable Long itemId) {
		ItemDesc itemDesc=itemService.findItemDescById(itemId);
		//返回
		return SysResult.success(itemDesc);
	}
}
