package com.lvxc.common.utils;

import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CronUtil {

    /**
     * 生成指定格式日期字符
     *
     * @param date       日期
     * @param dateFormat : e.g:yyyy-MM-dd HH:mm:ss
     * @return formatTimeStr
     */
    public static String formatDateByPattern(Date date, String dateFormat) {
        dateFormat = dateFormat == null ? "yyyy-MM-dd HH:mm:ss" : dateFormat;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return date != null ? sdf.format(date) : null;
    }

    /**
     * 生成cron表达式 ss mm HH dd MM ? yyyy
     * convert Date to cron ,eg.  "0 06 10 15 1 ? 2014"
     *
     * @param date : 时间点
     */
    public static String getCron(Date date) {
        String dateFormat = "ss mm HH dd MM ? yyyy";
        return formatDateByPattern(date, dateFormat);
    }

    /**
     * 生成cron表达式 ss mm HH dd MM ?
     * convert Date to cron ,eg.  "0 06 10 15 1 ?"
     *
     * @param date : 时间点
     * @param type : 类型 日/周/月
     */
    public static String getLoopCron(Date date, String type, Integer week, Integer day) {
        String dateFormat = "ss mm HH";
        //  dd MM ?
        String cron = formatDateByPattern(date, dateFormat);
        switch (type) {
            case "Day":
                return cron + " * * ?";
            case "Week":
                return cron + " ? * " + getCurrentWeek(week);
            case "Month":
                return cron + " " + day + " * ?";
            default:
                return "false";
        }
    }


    /**
     * 获取当前星期的字符 MON TUE WED THU FRI SAT SUN
     *
     * @param week : 周 1 2 3 4 5 6 7
     * @return 星期字符
     */
    public static String getCurrentWeek(Integer week) {
        String[] weeks = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        return weeks[week - 1];
    }
    public static void main(String[] args) {
        Date date = DateUtil.parse("2024-03-28 14:55:01","yyyy-MM-dd HH:mm:ss");

        String dateFormat = "ss mm HH dd MM ? yyyy";
        String cron = formatDateByPattern(date, dateFormat);
        System.out.println("原始:" + cron);
        //
        String day = formatDateByPattern(date, "ss mm HH");
        System.out.println("日报:" + day + " * * ?");
        //
        //0 15 10 ? * MON 每周一上午10点15分
        //动参为 周
        String week = formatDateByPattern(date, "ss mm HH");
        System.out.println("周报:" + week + " ? * MON");
        //
        //0 15 9 10 * ? 每月10号9点15分
        //动参为 号
        String month = formatDateByPattern(date, "ss mm HH");
        System.out.println("月报:" + month + " 10 * ?");
    }

}
