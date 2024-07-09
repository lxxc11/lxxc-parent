package com.lvxc.user.common;

import com.lvxc.user.common.enums.DateEnum;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DateUtil {

    public final static String yyyyMMddHHMMSS = "yyyy-MM-dd HH:mm:ss";

    public final static String yyyyMMddHHMM = "yyyy-MM-dd HH:mm";

    public final static String yyyyMMdd = "yyyy-MM-dd";

    public final static String yyyyMM = "yyyy-MM";

    public final static String yyyy = "yyyy";
    public static final String GMT8 = "GMT+8";
    public static final String MM = "MM";

    /**
     * @param years
     * @return
     * @Description: 获取最近几年年份
     * @author lvxc
     */
    public static List<Integer> getLastYears(int years) {
        if (years < 0) {
            return null;
        }
        List<Integer> lastYears = new ArrayList<Integer>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < years; i++) {
            Integer year = (calendar.get(Calendar.YEAR) - i);
            lastYears.add(year);
        }
        Collections.reverse(lastYears);
        return lastYears;
    }

    /**
     * @param year
     * @return
     * @Description: 获取某一年的开始年月日
     * @author Administrator
     */
    public static String getStartDayInYear(String year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        Date firstDay = calendar.getTime();
        return new SimpleDateFormat(yyyyMMdd).format(firstDay);
    }

    /**
     * @param year
     * @return
     * @Description: 获取某一年的结束年月日
     * @author lvxc
     */
    public static String getEndDayInYear(String year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date firstDay = calendar.getTime();
        return new SimpleDateFormat(yyyyMMdd).format(firstDay);
    }

    public static String getDateFormat(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static Date getStrToDateFormat(String date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            log.warn("发生异常：", e);
        }
        return new Date();
    }

    public static Date getStrToDateFormatList(String date, String... formatList) {
        for (String format : formatList) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            try {
                return simpleDateFormat.parse(date);
            } catch (ParseException e) {
                continue;
            }
        }
        return new Date();
    }

    /**
     * @param calenderType    维度类型(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_YEAR)
     * @param preAxisLength   当前时间前区间长度
     * @param afterAxisLength 当前时间后区间长度
     * @param dateFormat      格式化
     * @return
     * @Description: 获取前几年||前几月||前几日日期
     * @author hel
     */
    public static List<String> generationTimeAxis(Integer calenderType, Integer preAxisLength,
                                                  Integer afterAxisLength, String dateFormat) {
        List<String> timeAxis = new ArrayList<>();
        if (calenderType == null || preAxisLength == null || dateFormat == null) {
            return timeAxis;
        }
        try {
            Calendar calendar = Calendar.getInstance();
            if (afterAxisLength != null && afterAxisLength > 0) {
                calendar.set(calenderType, calendar.get(calenderType) + afterAxisLength);
                preAxisLength += afterAxisLength;
            }
            int target = calendar.get(calenderType);
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            for (int index = 0; index < preAxisLength; index++) {
                Calendar calendar1 = (Calendar) calendar.clone();
                calendar1.set(calenderType, target--);
                timeAxis.add(format.format(calendar1.getTime()));
            }
        } catch (Exception e) {
            throw new RuntimeException("生成时间轴异常");
        }
        return timeAxis.stream().sorted().collect(Collectors.toList());
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getCurrentDate() {
        return getCurrentDate(yyyyMMdd);
    }

    public static Date getCurrentDate(String pattern) {
        Calendar calendar = Calendar.getInstance();
        return getStrToDateFormat(getDateFormat(calendar.getTime(), pattern), pattern);
    }

    /**
     * 获取下一天时间
     *
     * @return
     */
    public static Date getNextDay() {
        return getNextDay(yyyyMMdd);
    }

    public static Date getNextDay(String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        return getStrToDateFormat(getDateFormat(calendar.getTime(), pattern), pattern);
    }

    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();

    }


    public static String getNextMonths(Integer num) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, num); //月份增减
        return new SimpleDateFormat(yyyyMMdd).format(c.getTime());
    }

    /**
     * 获取当年的开始日期
     */
    public static Date getStartYearDateInCurrent() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 0);
        return getStrToDateFormat(getDateFormat(cal.getTime(), yyyyMMdd), yyyyMMdd);
    }

    /**
     * 获取当前月开始的日期
     *
     * @return
     */
    public static Date getStartMonthDateInCurrent() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getStrToDateFormat(getDateFormat(cal.getTime(), yyyyMMdd), yyyyMMdd);
    }

    /**
     * 获取当前月结束的日期
     *
     * @return
     */
    public static Date getMaxMonthDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return getStrToDateFormat(getDateFormat(calendar.getTime(), yyyyMMdd), yyyyMMdd);
    }

    /**
     * 获取当前周开始日期
     *
     * @return
     */
    public static Date getStartWeekDateInCurrent() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return getStrToDateFormat(getDateFormat(cal.getTime(), yyyyMMdd), yyyyMMdd);
    }

    /**
     * 获取当前周结束日期
     *
     * @return
     */
    public static Date getEndWeekDateInCurrent() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getStartWeekDateInCurrent());
        cal.add(Calendar.DATE, 6);
        return cal.getTime();
    }


    /**
     * 将日期字符串转换为Date类型
     * String类型的日期格式和要转化的format格式必须一样
     *
     * @param dateStr    日期字符串
     * @param dateFormat 日期格式  yyyy-MM-dd
     * @return date
     */
    public static Date transformStringToDate(String dateStr, String dateFormat) {
        Date date = null;
        DateFormat format = new SimpleDateFormat(dateFormat);
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }

        return date;
    }


    public static Date getMonthBefore(Date date, int before, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, before);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
        } catch (ParseException e) {
            log.info("parse time error");
        }
        return new Date();
    }

    public static String getMonthBeforeStr(Date date, int before, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, before);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.format(calendar.getTime());
        } catch (Exception e) {
            log.info("format time error");
        }
        return simpleDateFormat.format(new Date());
    }

    public static String getDayBefore(Date date, int before, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, before);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.format(calendar.getTime());
        } catch (Exception e) {
            log.info("parse time error");
        }
        return simpleDateFormat.format(new Date());
    }

    public static String getTime(Date date, int calendarType, int offset, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendarType, offset);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.format(calendar.getTime());
        } catch (Exception e) {
            log.info("parse time error");
        }
        return simpleDateFormat.format(new Date());
    }

    public static Date addDateMinute(Date date, int x) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, x);// 24小时制
        return cal.getTime();

    }

    public static long secondsBetweenDates(Date beginTime, Date endTime) {
        long s = (endTime.getTime() - beginTime.getTime()) / 1000;
        return s;
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date trimTimeZoneDate() {
        return toDate(LocalDate.now());
    }

    public static Date trimTime(Date meetTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(yyyyMMdd);
        try {
            return dateFormat.parse(dateFormat.format(meetTime));
        } catch (ParseException e) {
            return toDate(LocalDate.now());
        }
    }

    public static String trimTimeToString(Date meetTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(yyyyMMdd);
        return dateFormat.format(meetTime);
    }

    public static Date trimDayTime(Date currentDate) {
        SimpleDateFormat format = new SimpleDateFormat(yyyyMM);
        try {
            return format.parse(format.format(currentDate));
        } catch (ParseException e) {
            log.warn("发生异常：", e);
        }
        return null;
    }

    /**
     * Date转LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate;
    }


    /**
     * 获取一年的第一天
     *
     * @param date
     * @return
     */
    public static Date getYearStart(Date date) {
        LocalDate localDate = dateToLocalDate(date);
        Date yearStartDay = Date.from(localDate.with(TemporalAdjusters.firstDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return yearStartDay;
    }

    /**
     * 获取一年的最后一天
     *
     * @param date
     * @return
     */
    public static Date getYearEnd(Date date) {
        LocalDate localDate = dateToLocalDate(date);
        Date yearEndDay = Date.from(localDate.with(TemporalAdjusters.lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return yearEndDay;
    }

    /**
     * 获取一月的第一天
     *
     * @param date
     * @return
     */
    public static Date getMonthStart(Date date) {
        LocalDate localDate = dateToLocalDate(date);
        Date firstDayByMonth = Date.from(localDate.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return firstDayByMonth;
    }

    /**
     * 获取一月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getMonthEnd(Date date) {
        LocalDate localDate = dateToLocalDate(date);
        Date endDayByMonth = Date.from(localDate.with(TemporalAdjusters.lastDayOfMonth()).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return endDayByMonth;
    }

    /**
     * 根据时间 和时间格式 校验是否正确
     *
     * @param length 校验的长度
     * @param sDate  校验的日期
     * @param format 校验的格式
     * @return
     */
    public static boolean isLegalDate(int length, String sDate, String format) {
        int legalLen = length;
        if ((sDate == null) || (sDate.length() != legalLen)) {
            return false;
        }
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取指定周期的之前的日期
     *
     * @param date
     * @param num
     * @param type
     * @return
     */
    public static Date getBeforeDate(Date date, long num, Integer type) {
        LocalDate localDate = dateToLocalDate(date);
        if (type == DateEnum.DAY.getCode()) {
            localDate = localDate.minusDays(num);
        }
        if (type == DateEnum.WEEK.getCode()) {
            localDate = localDate.minusWeeks(num);
        }
        if (type == DateEnum.MONTH.getCode()) {
            localDate = localDate.minusMonths(num);
        }
        if (type == DateEnum.YEAR.getCode()) {
            localDate = localDate.minusYears(num);
        }
        return localDateToDate(localDate);
    }

    /**
     * 获取指定周期的之后的日期
     *
     * @param date
     * @param num
     * @param type
     * @return
     */
    public static Date getAfterDate(Date date, long num, Integer type) {
        LocalDate localDate = dateToLocalDate(date);
        if (type == DateEnum.DAY.getCode()) {
            localDate = localDate.plusDays(num);
        }
        if (type == DateEnum.WEEK.getCode()) {
            localDate = localDate.plusWeeks(num);
        }
        if (type == DateEnum.MONTH.getCode()) {
            localDate = localDate.plusMonths(num);
        }
        if (type == DateEnum.YEAR.getCode()) {
            localDate = localDate.plusYears(num);
        }
        return localDateToDate(localDate);
    }

    /**
     * LocalDate转Date
     *
     * @param localDate
     * @return
     */
    public static Date localDateToDate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * 计算两个时间之间的秒数
     *
     * @param dateOne
     * @param dateTwo
     * @return
     */
    public static long betweenTheTwoDate(Date dateOne, Date dateTwo) {
        if (dateOne.getTime() > dateTwo.getTime()) {
            return (dateOne.getTime() - dateTwo.getTime())/1000;
        }
        if (dateTwo.getTime() > dateOne.getTime()) {
            return (dateTwo.getTime() - dateOne.getTime())/1000;
        }
        return 0;
    }

    /**
     * 获取年
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        LocalDate localDate = dateToLocalDate(date);
        return localDate.getYear();
    }

    /**
     * 获取月
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        LocalDate localDate = dateToLocalDate(date);
        return localDate.getMonthValue();
    }

    /**
     * 获取日
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        LocalDate localDate = dateToLocalDate(date);
        return localDate.getDayOfMonth();
    }

    /**
     * x月前的日期
     *
     * @param numb
     * @return
     */
    public static String getLastNumbMonth(int numb){
        Date dNow = new Date();   //当前时间
        Date dBefore;
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.MONTH, -numb);  //设置为前3月
        dBefore = calendar.getTime();   //得到前3月的时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日"); //设置时间格式
        String defaultStartDate = sdf.format(dBefore);
        return defaultStartDate;//格式化前3月的时间

    }
}
