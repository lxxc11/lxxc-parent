package com.lvxc.user.common.base;

/**
 * 结果枚举类
 *
 * @author hel
 * 2018年7月4日 下午7:25:23
 */
public enum ResultEnum implements IResultCode{

	CODE_SUCCESS(200, "成功"),
	TOKEN_IS_INVALID_MSG(401, "Token失效，请重新登录!"),
	CODE_UNAUTHORIZED(403, "无权限"),
	CODE_EXCEPTION(500, "失败"),
	ACCOUNT_DISABLE(400, "账号已禁用,请联系管理员"),
	UN_SUPPORT(404, "不支持的操作类型"),
	ACCOUNT_OR_PSW_ERROR(501, "账号密码错误"),
	ACCOUNT_IS_NOTEXIST(502, "账号不存在"),
	LOGIN_ERROR_LOCK(503, "连续登陆失败%s次，账户已锁定，请10分钟后再登陆"),
	LOGIN_ERROR_SMS(-506, "用户名或密码错误达三次,请验证手机号!"),
	DOWN_ERROR(410, "导出异常"),
    EXECUTE_FAIL(999, "执行失败");

	private final Integer code;

	private final String message;

	ResultEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getMsg() {
		return this.message;
	}
}

