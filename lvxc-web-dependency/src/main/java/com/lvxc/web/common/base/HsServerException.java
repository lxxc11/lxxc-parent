package com.lvxc.web.common.base;

import lombok.Data;

@Data
public class HsServerException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;

	public HsServerException(ResultEnum resultEnum){
		this.code = resultEnum.getCode();
		this.msg = resultEnum.getMsg();
	}
	public HsServerException(int code, String msg){
		this.code = code;
		this.msg = msg;
	}

}
