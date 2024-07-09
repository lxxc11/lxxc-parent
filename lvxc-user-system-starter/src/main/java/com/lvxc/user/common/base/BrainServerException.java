package com.lvxc.user.common.base;

import lombok.Data;

@Data
public class BrainServerException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;

	public BrainServerException(IResultCode resultEnum){
		this.code = resultEnum.getCode();
		this.msg = resultEnum.getMsg();
	}
	public BrainServerException(int code, String msg){
		this.code = code;
		this.msg = msg;
	}
	public BrainServerException(String msg){
		this.code = ResultEnum.CODE_EXCEPTION.getCode();
		this.msg = msg;
	}

}
