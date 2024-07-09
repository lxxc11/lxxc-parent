package com.lvxc.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

/**
 * 工作日工具类
 * @author 詹杨锋
 * @since 2023/3/14
 */
@Slf4j
public class WorkdayUtil {
    //节假日日期列表
    private static final List<String> HOLIDAYS = new ArrayList<>();
    //调休的工作日期列表
    private static final List<String> SPECIAL_WORKDAYS = new ArrayList<>();

    //静态代码块内调用第三方接口拿到数据存进List中
    static {
        Map<String, Object> param = new HashMap<String, Object>() {{
            //这里key值是注册天行API账号是给的
            //天行网站申请的api-key https://www.tianapi.com/
            put("key", "fc09ff9dc8c6a221fb59646a12f453f4");
            put("type", 1);
        }};
        Set<String> years = new HashSet<>();

        File holidaysFile = new File("./file/holidays");
        HOLIDAYS.addAll(FileUtil.readLines(holidaysFile, StandardCharsets.UTF_8));
        for (String holiday : HOLIDAYS) {
            years.add(holiday.split("-")[0]);
        }

        File specialWorkdaysFile = new File("./file/special_workdays");
        SPECIAL_WORKDAYS.addAll(FileUtil.readLines(specialWorkdaysFile, StandardCharsets.UTF_8));

        //获取当前年份，以及前一年、后一年的节假日数据，后一年的可能因为还没公布而获取不到
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = -1; i < 2; i++) {
            Integer year = currentYear - i;

            if (!years.contains(year.toString())) {
                param.put("date", year);
                String url = "https://apis.tianapi.com/jiejiari/index";
                String response = HttpUtil.get(url, param);
                JSONObject resObj = JSON.parseObject(response);
                int code = resObj.getIntValue("code");
                if (code == 200) {
                    JSONObject result = resObj.getJSONObject("result");
                    if (result.getBooleanValue("update")) {
                        JSONArray list = result.getJSONArray("list");
                        for (int j = 0; j < list.size(); j++) {
                            JSONObject listObj = list.getJSONObject(j);
                            String vacation = (String) listObj.get("vacation");
                            List<String> holidayList = Arrays.asList(vacation.split("\\|"));

                            HOLIDAYS.addAll(holidayList);
                            FileUtil.appendLines(holidayList, holidaysFile, StandardCharsets.UTF_8);

                            String remark = (String) listObj.get("remark");
                            if (StringUtils.isNotEmpty(remark)) {
                                List<String> specialWorkdayList = Arrays.asList(remark.split("\\|"));
                                SPECIAL_WORKDAYS.addAll(specialWorkdayList);
                                FileUtil.appendLines(specialWorkdayList, specialWorkdaysFile, StandardCharsets.UTF_8);
                            }
                        }
                    }
                }
            }
        }
    }

    //计算工作日数的方法
    public static int getWorkingDays(Date start, Date end) {
        int workDays = 0;

        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        //如果开始时间比结束时间晚，交换结束时间为开始时间
        if (start.getTime() > end.getTime()) {
            cal.setTime(end);
            end = start;
        }

        while (cal.getTimeInMillis() <= end.getTime()) {
            String current = DateUtil.format(cal.getTime(), "yyyy-MM-dd");
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            //TODO: 日期判断
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                //工作日并且不是法定节假日
                if (!HOLIDAYS.contains(current)) {
                    workDays++;
                }
            } else {
                //周末并且是调休工作日
                if (SPECIAL_WORKDAYS.contains(current)) {
                    workDays++;
                }
            }
            cal.add(Calendar.DATE, 1);
        }

        return workDays;
    }

    /**
     * 获取n个工作日后的日期
     *
     * @param date   日期
     * @param offset 计算n个工作日后，前一个工作日则传-1
     * @return
     * @throws ParseException
     */
    public static Date offsetDay(Date date, int offset) {
        int target = 0;
        int step = 1;

        if (offset < 0) {
            step = -1;
            offset = -offset;
        }

        while (target < offset) {
            Date newDate = DateUtil.offsetDay(date, step);
            String newDateStr = DateUtil.format(newDate, "yyyy-MM-dd");
            //如果新日期是工作日，则计数+1
            if (isWorkingDay(newDateStr)) {
                target++;
            }
            date = newDate;
        }

        return date;
    }

    /**
     * 判断是否是周末
     *
     * @param dateStr
     * @return
     */
    public static boolean isWeekend(String dateStr) {
        Date date = DateUtil.parse(dateStr, "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }

    /**
     * 判断是否是节假日
     *
     * @param dateStr
     * @return
     */
    public static boolean isHoliday(String dateStr) {
        if (HOLIDAYS.size() > 0)
            return HOLIDAYS.contains(dateStr);
        return false;
    }

    /**
     * 判断是否是调休的工作日
     *
     * @param dateStr
     * @return
     */
    public static boolean isExtraWorkdays(String dateStr) {
        if (SPECIAL_WORKDAYS.size() > 0)
            return SPECIAL_WORKDAYS.contains(dateStr);
        return false;
    }

    /**
     * 判断是否是工作日
     *
     * @param dateStr
     * @return
     */
    public static Boolean isWorkingDay(String dateStr) {
        //是否加班日
        if (isExtraWorkdays(dateStr)) {
            return true;
        }
        //是否节假日
        if (isHoliday(dateStr)) {
            return false;
        }
        //是否是周末
        if (isWeekend(dateStr)) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Date now = new Date();
        System.out.println(getWorkingDays(now, DateUtil.offsetDay(now, 15)));
        System.out.println(DateUtil.format(offsetDay(now, -1), "yyyy-MM-dd"));
    }
}
