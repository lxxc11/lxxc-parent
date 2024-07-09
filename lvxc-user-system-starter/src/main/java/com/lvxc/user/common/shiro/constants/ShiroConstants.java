package com.lvxc.user.common.shiro.constants;

/**
 * Shiro通用常量
 * 
 * @author cgj
 */
public class ShiroConstants
{
    /**
     * 当前登录的用户
     */
    public static final String CURRENT_USER = "currentUser";

    /**
     * 用户名字段
     */
    public static final String CURRENT_USERNAME = "username";

    /**
     * 锁定屏幕字段
     */
    public static final String LOCK_SCREEN = "lockscreen";

    /**
     * 消息key
     */
    public static final String MESSAGE = "message";

    /**
     * 错误key
     */
    public static final String ERROR = "errorMsg";

    /**
     * 编码格式
     */
    public static final String ENCODING = "UTF-8";

    /**
     * 当前在线会话
     */
    public static final String ONLINE_SESSION = "online_session";

    /**
     * 验证码key
     */
    public static final String CURRENT_CAPTCHA = "captcha";

    /**
     * 验证码开关
     */
    public static final String CURRENT_ENABLED = "captchaEnabled";

    /**
     * 验证码类型
     */
    public static final String CURRENT_TYPE = "captchaType";

    /**
     * 验证码
     */
    public static final String CURRENT_VALIDATECODE = "validateCode";

    /**
     * 验证码错误
     */
    public static final String CAPTCHA_ERROR = "captchaError";

    /**
     * 登录记录缓存
     */
    public static final String LOGINRECORDCACHE = "loginRecordCache";

    /**
     * 系统活跃用户缓存
     */
    public static final String SYS_USERCACHE = "sys-userCache";

    public final static String TOKEN_IS_INVALID_MSG = "Token失效，请重新登录!";

    public final static String X_ACCESS_TOKEN = "x-access-token";

    public final static String TOKEN = "token";

    public final static String LOGIN_TYPE = "logintype";

    public final static String EXPERT = "other_system";

    /** 登录用户Token令牌缓存KEY前缀 */
    public static final String PREFIX_USER_TOKEN  = "prefix_user_token_";

    public static final String PREFIX_USER_INFO  = "prefix_user_info_";

    /**
     * 随机盐的位数
     **/
    public static final int SALT_LENGTH = 8;
    /**
     * hash的散列次数
     **/
    public static final int HASH_ITERATORS = 1024;

}
