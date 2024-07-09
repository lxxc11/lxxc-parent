package com.lvxc.common.utils;

/**
 * <p>
 *密码强度校验
 * </p>
 *
 * @author WangXiangg
 * @since 2023/9/25
 */
public class CheckPassWordUtil {

    public static Boolean checkPassWord(String userName,String password){
        //密码里不能包含用户名
        if(userName==null || userName==""){
            return false;
        }
        if(password.contains(userName)){
            return false;
        }

        //判断密码长度是否大于8
        if (password == null || password.length() <8 ){
            return false;
        }
        //判断密码是否是大小写字母、数字、特殊字符中的至少三种
        //数字
        final String REG_NUMBER = ".*\\d+.*";
        //小写字母
        final String REG_UPPERCASE = ".*[A-Z]+.*";
        //大写字母
        final String REG_LOWERCASE = ".*[a-z]+.*";
        //特殊符号
        final String REG_SYMBOL = ".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*";
        int i = 0;
        if (password.matches(REG_NUMBER)){
            i++;
        }
        if (password.matches(REG_LOWERCASE)){
            i++;
        }
        if (password.matches(REG_UPPERCASE)){
            i++;
        }
        if (password.matches(REG_SYMBOL)){
            i++;
        }
        if (i  < 3 )  {
            return false;
        }

        char[] chars = password.toCharArray();
        for (int a = 0; a < chars.length-2 ; a++) {
            int n1 = chars[a];
            int n2 = chars[a + 1];
            int n3 = chars[a + 2];

            // 判断重复字符(连续3个)
            if (n1 == n2 && n1 == n3) {
                return false;
            }
            // 判断连续字符(3个)： 正序 + 倒序
            if ((n1 + 1 == n2 && n1 + 2 == n3 ) || (n1 - 1 == n2 && n1 - 2 == n3)) {
                return false;
            }
        }
        return true;
    }
}
