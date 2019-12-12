package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.jt.dubbo.service.DubboOrderService;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;

@Service
public class DubboOrderServiceImpl implements DubboOrderService{
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	
	@Autowired
	private OrderItemMapper orderItemMapper;

	@Override
	@Transactional
	public String saveOrder(Order order) {
		String orderId = "" + order.getUserId() +System.currentTimeMillis();
		Date date = new Date();
		order.setOrderId(orderId)
			 .setStatus(1)
			 .setCreated(date)
			 .setUpdated(date);
		orderMapper.insert(order);
		System.out.println("订单入库成功!!!!");
		OrderShipping orderShipping = new OrderShipping();
		orderShipping.setOrderId(orderId)
					 .setCreated(date)
					 .setUpdated(date);
		orderShippingMapper.insert(orderShipping);
		System.out.println("订单物流入库成功!!!!");
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId)
					 .setCreated(date)
					 .setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		System.out.println("订单商品入库成功!!!!!");
		return orderId;
	}

	/**
	 * 1.利用mybatis-plus方式查询
	 * 2.利用sql语句关联查询3张表
	 */
	@Override
	public Order findOrderById(String id) {
		/*
		 * Order order = orderMapper.selectById(id); OrderShipping orderShipping =
		 * orderShippingMapper.selectById(id); QueryWrapper<OrderItem> queryWrapper =
		 * new QueryWrapper<OrderItem>(); queryWrapper.eq("order_id", id);
		 * List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);
		 * order.setOrderShipping(orderShipping) .setOrderItems(orderItems);
		 */
		return orderMapper.findOrderById(id);
	}
}
