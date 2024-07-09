package com.lvxc.common.utils;

import java.math.BigDecimal;

/**
 *  同比/环比/增长率计算
 * @author chenp
 */
public class PercentUtil {


    /**、
     * @param preNum 同期数
     * @param sufNum 往期数
     */
    public static double percentBigDecimal(BigDecimal preNum, BigDecimal sufNum){
        double result = countDecimal(preNum,sufNum);
        return result;
    }
    public static double countDecimal(BigDecimal preNum, BigDecimal sufNum){
        boolean preBoolean = verifyNum(preNum);
        boolean sufBoolean = verifyNum(sufNum);
        //同时为true计算
        if(preBoolean && sufBoolean){
            boolean b = verifyEqual(preNum, sufNum);
            if (!b){
                return realCountDecimal(preNum,sufNum);
            }
            if (b){
                return 0;
            }
        }
        if(!preBoolean && !sufBoolean){
            return 0;
        }
        if(!sufBoolean){
            return 100;
        }
        return -100;
    }
    //验证数字是否为零和null
    public static boolean verifyNum(BigDecimal num){
        if(null !=num && num.compareTo(BigDecimal.ZERO)!=0 ){
            return true;
        }
        return false;
    }

    //验证两个数字是否相等
    public static boolean verifyEqual(BigDecimal preNum, BigDecimal sufNum){
        int n = preNum.compareTo(sufNum);
        //比较 -1 小于   0 等于    1 大于
        return n == 0;
    }
    //真正计算
    public static double realCountDecimal(BigDecimal preNum, BigDecimal sufNum){
        //(前面的数字-后面的数字)/后面的数字*100
        BigDecimal bigDecimal = (preNum.subtract(sufNum)).divide(sufNum,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
        if (bigDecimal.compareTo(BigDecimal.ZERO) !=0){
            return  bigDecimal.doubleValue();
        }
        return 0;
    }

    public static void main(String[] args) {
        BigDecimal a = BigDecimal.valueOf(1);
        BigDecimal b = BigDecimal.valueOf(2);
        double result = percentBigDecimal(a, b);
        System.out.println(result);
    }

}
