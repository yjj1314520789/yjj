package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class SysResult {
	
	private Integer status;		//定义状态码,200正常,201失败
	private String msg;			//服务器返回客户端的消息
	private Object data;		//服务器返回客户端的数据
	
	public static SysResult success() {
		return new SysResult(200,null,null);
	}
//	public static SysResult success(String msg) {
//		return new SysResult(200,msg,null);
//	}
	public static SysResult success(Object data) {
		return new SysResult(200,null,data);
	}
	public static SysResult success(String msg,Object data) {
		return new SysResult(200,msg,data);
	}
	public static SysResult fail() {
		return new SysResult(201,null,null);
	}
	public static SysResult fail(String msg) {
		return new SysResult(201,msg,null);
	}
	
}
