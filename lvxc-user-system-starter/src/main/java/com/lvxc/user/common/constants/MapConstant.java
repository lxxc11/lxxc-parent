package com.lvxc.user.common.constants;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class MapConstant {
    public static Map<Integer,String> logOperateMap = new HashMap<Integer, String>(){{
        put(1, "查询");
        put(2, "添加");
        put(3, "修改");
        put(4, "删除");
        put(5, "导入");
        put(6, "导出");
        put(7, "登录");
        put(8, "退出");
        put(9, "审核");
    }};
}
