package com.lvxc.user.common.constants;

public class CommonConstant {

    /**
     * 正常状态
     */
    public static final Integer STATUS_NORMAL = 0;

    /**
     * 禁用状态
     */
    public static final Integer STATUS_DISABLE = -1;

    /**
     * 删除标志
     */
    public static final Integer DEL_FLAG_1 = 1;

    /**
     * 未删除
     */
    public static final Integer DEL_FLAG_0 = 0;

    /**
     * 删除标志
     */
    public static final boolean DEL_FLAG_BOOL_1 = true;

    /**
     * 未删除
     */
    public static final boolean DEL_FLAG_BOOL_0 = false;


    /**
     * 系统日志类型： 登录
     */
    public static final int LOG_TYPE_1 = 1;

    /**
     * 系统日志类型： 操作
     */
    public static final int LOG_TYPE_2 = 2;

    /**
     * 操作日志类型： 查询
     */
    public static final int OPERATE_TYPE_1 = 1;

    /**
     * 操作日志类型： 添加
     */
    public static final int OPERATE_TYPE_2 = 2;

    /**
     * 操作日志类型： 更新
     */
    public static final int OPERATE_TYPE_3 = 3;

    /**
     * 操作日志类型： 删除
     */
    public static final int OPERATE_TYPE_4 = 4;

    /**
     * 操作日志类型： 导入
     */
    public static final int OPERATE_TYPE_5 = 5;

    /**
     * 操作日志类型： 导出
     */
    public static final int OPERATE_TYPE_6 = 6;

    /**
     * 操作日志类型： 登录
     */
    public static final int OPERATE_TYPE_7 = 7;

    /**
     * 操作日志类型： 退出
     */
    public static final int OPERATE_TYPE_8 = 8;

    /**
     * 操作日志类型： 审核
     */
    public static final int OPERATE_TYPE_9 = 9;

    /**
     * 审批结果： 未审批
     */
    public static final int WSP = 0;

    /**
     * 审批结果： 通过
     */
    public static final int TG = 1;

    /**
     * 审批结果： 不通过
     */
    public static final int BTG = 2;

    /**
     * mapstruct与spring集成
     */
    public static final String MAP_STRUCT_SPRING = "spring";

    public final static String INITUSER = "user.init";

}
