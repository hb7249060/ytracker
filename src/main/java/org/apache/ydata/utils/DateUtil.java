package org.apache.ydata.utils;


import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 */
public class DateUtil {

    private static ThreadLocal<DateFormat> dateFormatHolder = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    private static final Map<DateEnum, DateFormat> formats = new HashMap<DateEnum, DateFormat>();
    // 年月日时分秒毫秒(无下划线)
    public static final String dtLongMill = "yyyyMMddHHmmssS";

    static {
        formats.put(DateEnum.DATE, new SimpleDateFormat(DateEnum.DATE.getValue()));
        formats.put(DateEnum.DATE_MONTH, new SimpleDateFormat(DateEnum.DATE_MONTH.getValue()));
        formats.put(DateEnum.UNSIGNED_DATE, new SimpleDateFormat(DateEnum.UNSIGNED_DATE.getValue()));
        formats.put(DateEnum.TIME, new SimpleDateFormat(DateEnum.TIME.getValue()));
        formats.put(DateEnum.DATETIME, new SimpleDateFormat(DateEnum.DATETIME.getValue()));
        formats.put(DateEnum.DIR_DATE, new SimpleDateFormat(DateEnum.DIR_DATE.getValue()));
        formats.put(DateEnum.NON_SEP_TIME, new SimpleDateFormat(DateEnum.NON_SEP_TIME.getValue()));
        formats.put(DateEnum.UNSIGNED_DATETIME, new SimpleDateFormat(DateEnum.UNSIGNED_DATETIME.getValue()));
        formats.put(DateEnum.DATETIMEUTC, new SimpleDateFormat(DateEnum.DATETIMEUTC.getValue()));
        formats.put(DateEnum.FULL_DATE_PATTERN, new SimpleDateFormat(DateEnum.FULL_DATE_PATTERN.getValue()));
        formats.put(DateEnum.PORTAL_DATE_PATTERN, new SimpleDateFormat(DateEnum.PORTAL_DATE_PATTERN.getValue()));
        formats.put(DateEnum.SIGNED_DATETIME, new SimpleDateFormat(DateEnum.SIGNED_DATETIME.getValue()));
        formats.put(DateEnum.MONTH_AND_DAY, new SimpleDateFormat(DateEnum.MONTH_AND_DAY.getValue()));
        formats.put(DateEnum.DATE_NO_SECEND, new SimpleDateFormat(DateEnum.DATE_NO_SECEND.getValue()));
        formats.put(DateEnum.DOT_DATE, new SimpleDateFormat(DateEnum.DOT_DATE.getValue()));
        formats.put(DateEnum.DATE_NO_YEAR, new SimpleDateFormat(DateEnum.DATE_NO_YEAR.getValue()));
        formats.put(DateEnum.MONTH_SLASH_DAY, new SimpleDateFormat(DateEnum.MONTH_SLASH_DAY.getValue()));
        formats.put(DateEnum.CHINES_YEAR_DATE, new SimpleDateFormat(DateEnum.CHINES_YEAR_DATE.getValue()));
        formats.put(DateEnum.CHINES_MONTH_DATE, new SimpleDateFormat(DateEnum.CHINES_MONTH_DATE.getValue()));
        formats.put(DateEnum.TIME_NO_SECEND, new SimpleDateFormat(DateEnum.TIME_NO_SECEND.getValue()));
        formats.put(DateEnum.DAY_AND_MONTH, new SimpleDateFormat(DateEnum.DAY_AND_MONTH.getValue()));
        formats.put(DateEnum.YEAR_MONTH_DATE, new SimpleDateFormat(DateEnum.YEAR_MONTH_DATE.getValue()));
        formats.put(DateEnum.MONTH_DOT_DAY, new SimpleDateFormat(DateEnum.MONTH_DOT_DAY.getValue()));
    }

    /**
     * 获取当前时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getNow() {
        return formats.get(DateEnum.DATETIME).format(new Date());
    }

    /**
     * 获取当前时间
     */
    public static String getNow(DateEnum format) {
        return formats.get(format).format(new Date());
    }

    /**
     * 转换字符串日期
     */
    public static Date parseDate(String date, DateEnum format) {
        try {
            return formats.get(format).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 转换字符串日期
     */
    public static String parseDateToLoacle(String date, DateEnum format) {
        try {
            DateFormat utcFormater = formats.get(format);
            utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
            DateFormat localFormater = formats.get(DateEnum.DATETIME);
            Date gpsUTCDate = null;
            String gpsUTCDateStr = null;
            try {
                gpsUTCDate = utcFormater.parse(date);
                gpsUTCDateStr = localFormater.format(gpsUTCDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return gpsUTCDateStr;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期转换成字符串
     */
    public static String parseDate(Date date, DateEnum format) {
        return formats.get(format).format(date);
    }

    /**
     * 如果是当年的日期返回 HH-MM
     * 如果是往年 返回YYYY-HH-MM
     */
    public static String parseDateYear(Date date, DateEnum format) {
        String formatDate = formats.get(format).format(date);
        int year = getYear(new Date());
        if (formatDate.contains(year + "")) {
            DateEnum newEnum = DateEnum.MONTH_AND_DAY;//默认MM-DD
            if (format.equals(DateEnum.CHINES_YEAR_DATE)) {//年月日
                newEnum = DateEnum.CHINES_MONTH_DATE;//月日
            }
            return formats.get(newEnum).format(date);
        }
        return formatDate;
    }

    /**
     * 日期转换成字符串
     */
    public static String parseDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 日期转换成字符串, 零时区
     */
    public static String parseUTCDate(Date date, DateEnum format) {
        SimpleDateFormat df = new SimpleDateFormat(format.getValue());
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(date);
    }

    /**
     * Date日期格式化
     */
    public static Date parseToDate(Date date, DateEnum format) {
        return parseDate(parseDate(date, format), format);
    }

    /**
     * 获取前/后日期
     */
    public static Date getBeforeDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    /**
     * 获取前/后日期
     */
    public static String getBeforeDay(DateEnum format, int day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        return formats.get(format).format(cal.getTime());
    }

    /**
     * 获取前/后日期
     *
     * @param date
     * @param format
     * @param day
     * @return
     */
    public static String getBeforeDay(Date date, DateEnum format, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);
        return formats.get(format).format(cal.getTime());
    }

    public static String getBeforeDay(Date date, DateEnum format, int field, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, day);
        return formats.get(format).format(cal.getTime());
    }

    /**
     * 获取前/后日期 时间
     *
     * @param format 格式
     * @param day    前/后日期
     * @param hour   时间
     * @return
     */
    public static String getBeforeDay(DateEnum format, int day, int hour, int mimute, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, mimute);
        cal.set(Calendar.SECOND, sec);
        return formats.get(format).format(cal.getTime());
    }

    /**
     * 得到不要下划线的时间格式
     *
     * @return
     */
    public static String getFormattedLongMillTimestamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date);
    }

    /**
     * 两个时间相减，算出秒数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long minusMillTimes(Date date1, Date date2) {
        return (date1.getTime() - date2.getTime()) / 1000;
    }

    /**
     * 两个时间相减，算出剩余的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long minusDaysTimes(Date date1, Date date2) {
        return (date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24);
    }

    /**
     * 两个时间相减，算出除了天数后小时数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long minusHoursTimes(Date date1, Date date2) {
        return ((date1.getTime() - date2.getTime()) % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
    }

    /**
     * 两个时间相减，算出处于天数小时数后分钟数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long minusMinutesTimes(Date date1, Date date2) {
        return ((date1.getTime() - date2.getTime()) % (1000 * 60 * 60)) / (1000 * 60);
    }

    /**
     * 两个时间相减，算出处于天数小时数分钟数后的秒数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long minusSecondsTimes(Date date1, Date date2) {
        return ((date1.getTime() - date2.getTime()) % (1000 * 60)) / 1000;
    }

    /**
     * 比较时间大小
     *
     * @param date
     * @param date2 return 1 0 -1
     */
    public static synchronized int compare(Date date, Date date2) {
        if (date2 == null)
            return -1;
        long time1 = date.getTime();
        long time2 = date2.getTime();
        if (time1 > time2)
            return 1;
        if (time1 == time2)
            return 0;
        return -1;
    }

    /**
     * 不足10，补0
     *
     * @param time
     * @return
     */
    public static String parse(int time) {
        if (time < 10) {
            return "0" + time;
        } else {
            return time + "";
        }
    }


    /**
     * 根据年月计算
     *
     * @param date
     * @return
     */
    public static int getDays(String date) {
        if (StringUtils.isNotBlank(date)) {
            Calendar c = Calendar.getInstance();
            Date d = parseDate(date, DateEnum.DATE);
            c.setTime(d);
            return c.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        return 0;
    }

    /**
     * 根据年月计算
     *
     * @param date
     * @return
     */
    public static int getDays(Date date) {
        if (date != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        return 0;
    }

    /**
     * 增加日期的天数。失败返回null。
     *
     * @param date      日期
     * @param dayAmount 增加数量。可为负数
     * @return 增加天数后的日期
     * @author cory
     */
    public static Date addDay(Date date, int dayAmount) {
        return addInteger(date, Calendar.DATE, dayAmount);
    }

    /**
     * 增加日期中某类型的某数值。如增加日期
     *
     * @param date     日期
     * @param dateType 类型
     * @param amount   数值
     * @return 计算后日期
     */
    private static Date addInteger(Date date, int dateType, int amount) {
        Date myDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(dateType, amount);
            myDate = calendar.getTime();
        }
        return myDate;
    }


    /**
     * 返回x天x时x分x秒
     *
     * @param d1
     * @param d2
     */
    public static String getDateDiffStr(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return null;
        }
        if (d1.before(d2)) {
            return null;
        }
        long s = (d1.getTime() - d2.getTime()) / 1000;
        int d = (int) s / (24 * 60 * 60);
        int h = (int) s / (60 * 60) - d * 24;
        int m = (int) s / 60 - d * 24 * 60 - h * 60;
        return (d > 0 ? d : 0) + "天" + (h > 0 ? h : 0) + "小时" + (m > 0 ? m : 0) + "分";
    }


    /**
     * 返回x天x时
     *
     * @param d1
     * @param d2
     */
    public static String getDateDiffToHour(Date d1, Date d2) {
        StringBuilder builder = new StringBuilder();
        if (d1 == null || d2 == null) {
            return null;
        }
        if (d1.before(d2)) {
            return null;
        }
        long s = (d1.getTime() - d2.getTime()) / 1000;
        int d = (int) s / (24 * 60 * 60);
        int h = (int) s / (60 * 60) - d * 24;
        if (d > 0) {
            builder.append(d).append("天");
        }
        builder.append(h).append("小时");
        return builder.toString();
    }


    public static Date toDate(String s) throws ServiceException {
        Assert.hasText(s, "参数不能为空");
        try {
            return dateFormatHolder.get().parse(s);
        } catch (Exception e) {
            throw new ServiceException("日期转换错误！");
        }
    }

    public static Date toDate(String s, String formatter) throws ServiceException {
        Assert.hasText(s, "参数不能为空");
        try {
            return new SimpleDateFormat(formatter).parse(s);
        } catch (Exception e) {
            throw new ServiceException("日期转换错误！");
        }
    }

    public static Long toLong(String s) throws ServiceException {
        Date date = toDate(s);
        if (date == null) {
            return 0L;
        }
        return date.getTime();
    }

    public static String toString(Date date) {
        if (date == null) {
            return null;
        }
        return dateFormatHolder.get().format(date);
    }

    public static String toString(Date date, String formatter) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(formatter).format(date);
    }

    public static Date addDays(Date src, Integer days) {
        Date target = null;
        if (src != null && days != null) {
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(src);
            rightNow.add(Calendar.DAY_OF_YEAR, days);
            target = rightNow.getTime();
        }
        return target;
    }

    public static Date addMonth(Date src, Integer month) {
        Date target = null;
        if (src != null && month != null) {
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(src);
            rightNow.add(Calendar.MONTH, month);
            target = rightNow.getTime();
        }
        return target;
    }

    public static Date addHour(Date src, Integer hours) {
        Date target = null;
        if (src != null && hours != null) {
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(src);
            rightNow.add(Calendar.HOUR, hours);
            target = rightNow.getTime();
        }
        return target;
    }

    public static Date addMinutes(Date src, Integer minutes) {
        Date target = null;
        if (src != null && minutes != null) {
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(src);
            rightNow.add(Calendar.MINUTE, minutes);
            target = rightNow.getTime();
        }
        return target;
    }

    public static Date addTime(Date src, Date time) throws ServiceException {
        Date target = null;
        if (src != null && time != null) {
            time = DateUtil.toDate(DateUtil.toString(time, "HH:mm:ss"), "HH:mm:ss");
            target = new Date(src.getTime() + time.getTime() - DateUtil.toDate("0:0:0", "HH:mm:ss").getTime());
        }
        return target;
    }

    public static Integer getWeekDay(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        int dayForWeek;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    public static Integer getMonthDay(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static Date addSecond(Date src, Integer value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(src);
        calendar.add(Calendar.SECOND, value);
        src = calendar.getTime();
        return src;
    }

    public static int subTimeMinute(Date src1, Date src2) {
        return (int) ((src1.getTime() - src2.getTime()) / (1000 * 60));
    }

    public static int subTimeDay(Date src1, Date src2) {
        return (int) ((src1.getTime() - src2.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static boolean isWeekend(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * yyyyMMddHHmmssS
     *
     * @param date
     * @return
     */
    public static final String dtLongMillFormat(Date date) {
        if (date == null) {
            return "";
        }

        return getFormat(dtLongMill).format(date);
    }

    private static final DateFormat getFormat(String format) {
        return new SimpleDateFormat(format);
    }

    public static String getEndDayOfWeekNo(int year, int weekNo) {
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        if (cal.compareTo(Calendar.getInstance()) > 0) {
            cal = Calendar.getInstance();
        }
        return (cal.get(Calendar.MONTH) + 1) + "-" +
                cal.get(Calendar.DAY_OF_MONTH);
    }

    private static Calendar getCalendarFormYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.YEAR, year);
        return cal;
    }

    public static Date addDateTime(Date day, int hour, int minute, int second) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(day);
        rightNow.add(Calendar.HOUR, 23);
        rightNow.add(Calendar.MINUTE, 59);
        rightNow.add(Calendar.SECOND, 59);
        return rightNow.getTime();
    }

    public static Date getTodayEveryTime(Date day, int hour, int minute, int second) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(day);
        rightNow.set(Calendar.HOUR_OF_DAY, hour);
        rightNow.set(Calendar.MINUTE, minute);
        rightNow.set(Calendar.SECOND, second);
        rightNow.set(Calendar.MILLISECOND, 0);
        return rightNow.getTime();
    }


    public static Date getTodayEveryTime(int hour) {
        return getTodayEveryTime(new Date(), hour, 0, 0);
    }

    public static Date zeroTime(Date day) {
        return getTodayEveryTime(day, 0, 0, 0);
    }


    public static int getYear(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.MONTH) + 1;
    }

    public static int getDay(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.DATE);
    }


    public static int getHour(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.HOUR_OF_DAY);
    }

    public static Date getLastSunday() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(DateUtil.getTodayEveryTime(new Date(), 0, 0, 0));
        instance.set(Calendar.DAY_OF_WEEK, 1);
        return instance.getTime();
    }

    /**
     * 获取指定日期所在月份开始的时间 00:00:00
     * month格式 2019-07
     * 返回格式： yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getMonthBegin(String month) {
        Date data = null;
        try {
            data = new SimpleDateFormat(DateEnum.DATE_MONTH.getValue()).parse(month);
        } catch (ParseException e) {
            throw new ServiceException("日期转换错误！");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        c.set(Calendar.MINUTE, 0);
        //将秒至0
        c.set(Calendar.SECOND, 0);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        // 本月第一天的时间戳转换为字符串
        SimpleDateFormat sdf = new SimpleDateFormat(DateEnum.DATETIME.getValue());
        return sdf.format(c.getTime());
    }


    /**
     * 获取指定日期所在月份结束的时间 23:59:59
     * month格式 2019-07
     * 返回格式： yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getMonthEnd(String month) {
        Date data = null;
        try {
            data = new SimpleDateFormat(DateEnum.DATE_MONTH.getValue()).parse(month);
        } catch (ParseException e) {
            throw new ServiceException("日期转换错误！");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        //设置为当月最后一天
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        //将小时至23
        c.set(Calendar.HOUR_OF_DAY, 23);
        //将分钟至59
        c.set(Calendar.MINUTE, 59);
        //将秒至59
        c.set(Calendar.SECOND, 59);
        //将毫秒至999
        c.set(Calendar.MILLISECOND, 999);
        // 本月最后一天的时间戳转换为字符串
        SimpleDateFormat sdf = new SimpleDateFormat(DateEnum.DATETIME.getValue());
        return sdf.format(c.getTime());
    }


    public static String getChineseWeek(Date day) {
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        System.out.println(cal.get(Calendar.DAY_OF_WEEK));
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

    // 获取今年是哪一年
    public static Integer getNowYear(Date date) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }

    // 获取本月是哪一月
    public static int getNowMonth(Date date) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    // 获取本周的开始时间
    public static Date getBeginDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return zeroTime(cal.getTime());
    }

    // 获取本周的结束时间
    public static Date getEndDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek(date));
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getTodayEveryTime(weekEndSta, 23, 59, 59);
    }

    // 获取上周的开始时间
    public static Date getBeginDayOfLastWeek() {
        Date date = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek - 7);
        return zeroTime(cal.getTime());
    }

    // 获取上周的结束时间
    public static Date getEndDayOfLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfLastWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getTodayEveryTime(weekEndSta, 23, 59, 59);
    }

    // 获取本月的开始时间
    public static Date getBeginDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(date), getNowMonth(date) - 1, 1);
        return zeroTime(calendar.getTime());
    }

    // 获取本月的结束时间
    public static Date getEndDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(date), getNowMonth(date) - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(date), getNowMonth(date) - 1, day);
        return getTodayEveryTime(calendar.getTime(), 23, 59, 59);
    }

    //时间解析成多少秒 格式HH:mm:ss  mm:ss ss
    public static Integer parseToSecond(String time) {

        Integer times = 0;  //视频时长 秒级
        if (StringUtils.isNotBlank(time)) {
            //处理mp3格式的文件
            if (time.contains("秒") || time.contains("分")) {
                if (time.contains("分")) {
                    String[] minute = time.split("分");
                    times += (Integer.parseInt(minute[0]) * 60);
                    if (minute.length > 1) {
                        times += Integer.parseInt(minute[1].split("秒")[0]);
                    }
                } else {
                    times += Integer.parseInt(time.split("秒")[0]);
                }
            } else {
                //处理video格式的文件
                String[] split = time.split(":");
                int length = split.length;
                if (length == 1) {
                    return Integer.valueOf(split[0].trim().split("\\.")[0]);
                }
                if (length == 2) {
                    return Integer.parseInt(split[0].trim().split("\\.")[0]) * 60 + Integer.parseInt(split[1].trim().split("\\.")[0]);
                }
                if (length == 3) {
                    return Integer.parseInt(split[0].trim().split("\\.")[0]) * 60 * 60 + Integer.parseInt(split[1].trim().split("\\.")[0]) * 60 + Integer.parseInt(split[2].trim().split("\\.")[0]);
                }
            }
        }
        return times;
    }

    public static void main(String args[]) {
        Date date = DateUtil.parseDate("2019-02-09 00:00:00", DateEnum.DATETIME);
        System.out.println(DateUtil.parseDate(getBeginDayOfWeek(date), DateEnum.DATETIME));
        System.out.println(DateUtil.parseDate(getEndDayOfWeek(date), DateEnum.DATETIME));
        System.out.println(DateUtil.parseDate(getBeginDayOfMonth(date), DateEnum.DATETIME));
        System.out.println(DateUtil.parseDate(getEndDayOfMonth(date), DateEnum.DATETIME));
    }

    /**
     * @Description: 秒转小时，保留一个小数
     * @methodName:
     * @param: sec
     * @return:
     * @author: 李艳
     * @Date: 5/26/21 2:18 PM
     */
    public static String toHourOfTwoDecimal(Long sec) {
        if (sec == null) {
            return "0";
        }
        // 秒转小时
        float hourTemp = (float) sec / 3600;
        // 小时保留1位小数
        hourTemp = (float) (Math.round((hourTemp) * 100)) / 100;
        // 判断最后一位小数 是否是0
        String[] val = String.valueOf(hourTemp).split("\\.");
        if (val != null && val.length == 2) {
            Integer oneDecimalVal = Integer.parseInt(val[1]);
            if (oneDecimalVal == 0) {
                return String.valueOf(val[0]);
            }
        }
        return String.valueOf(hourTemp);
    }


}
