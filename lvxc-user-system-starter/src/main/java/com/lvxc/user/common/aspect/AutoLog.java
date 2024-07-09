package com.lvxc.user.common.aspect;

import com.lvxc.user.common.constants.CommonConstant;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLog {

    /**
     * 日志内容
     *
     * @return
     */
    String value() default "";

    /**
     * 日志类型
     *
     * @return 1登录日志，2操作日志
     */
    int logType() default CommonConstant.LOG_TYPE_2;

    /**
     * 操作日志类型
     *
     * @return （1查询，2添加，3修改，4删除，5导入，6导出，7登录，8退出，9审核）
     */
    int operateType() default CommonConstant.OPERATE_TYPE_1;

}
