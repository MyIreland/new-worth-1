package cn.worth.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateParser;
import org.apache.commons.lang3.time.FastDateFormat;
//import org.joda.time.DateTime;
//import org.joda.time.Days;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: chenxiaoqing9
 * @Date: 2018/8/6 10:37
 * @Description:
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final DateTimeFormatter SHORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");
    public static final DateTimeFormatter SHORT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * HHmmss
     */
    public static String getCurrentTimeStr() {
        return LocalTime.now().format(TIME_FORMATTER);
    }

    /**
     * yyMMdd
     */
    public static String getCurrentShortDateStr() {
        return LocalDate.now().format(SHORT_DATE_FORMATTER);
    }

    /**
     * 返回指定时间的前几天Date
     *
     * @param thisDate
     * @param minusDay
     * @return
     */
//    public static Date getThisDateMillsDay(Date thisDate, int minusDay) {
//        thisDate = null == thisDate ? new Date() : thisDate;
//        return new DateTime(thisDate).minusDays(minusDay).toDate();
//    }

    /**
     * 给定时间的周内第一天
     *
     * @param thisDate
     * @return
     */
//    public static Date getWeekStartDateTime(Date thisDate) {
//        thisDate = null == thisDate ? new Date() : thisDate;
//        Date date = new DateTime(thisDate).dayOfWeek().withMinimumValue().millisOfDay().withMinimumValue().toDate();
//        return date;
//    }

    /**
     * 格式化日期
     */
    public static Date parseDate(String date) {
        return parse(date, "yyyy-MM-dd");
    }

    /**
     * 格式化日期
     */
    public static Date parseTimeMinutes(String date) {
        return parse(date, "yyyy-MM-dd HH:mm");
    }

    /**
     * 格式化日期
     */
    public static Date parseTime(String date) {
        return parse(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化日期
     */
    public static Date parse(String date, String pattern) {
        try {
            return DateUtils.parseDate(date, pattern);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取指定日期的周内最后一天
     *
     * @param thisDate
     * @return
     */
//    public static Date getWeekEndDateTime(Date thisDate) {
//        thisDate = null == thisDate ? new Date() : thisDate;
//        Date date = new DateTime(thisDate).dayOfWeek().withMaximumValue().millisOfDay().withMaximumValue().toDate();
//        return date;
//    }

    /**
     * 返回日期凌晨0:0:0
     *
     * @param thisDate
     * @return
     */
//    public static Date getThisDateStartTime(Date thisDate) {
//        Date date = new DateTime(thisDate).withTimeAtStartOfDay().toDate();
//        return date;
//    }

    /**
     * 返回日期晚上23:59:59
     *
     * @param thisDate
     * @return
     */
//    public static Date getThisDateEndTime(Date thisDate) {
//        Date date = new DateTime(thisDate).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
//        return date;
//    }

    /**
     * 返回日期指定格式
     *
     * @param thisDate
     * @return
     */
    public static Date getThisDateByPattern(Date thisDate, String pattren) throws ParseException {
        DateParser parser = FastDateFormat.getInstance();
        return parser.parse(DateFormatUtils.format(thisDate, pattren));
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek(Date thisDate) {
        return formatDate(thisDate, "E");
    }

    /**
     * 日期型字符串转化为日期 格式
     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
     * "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return DateUtils.parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数
     *
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     *
     * @param date
     * @return
     */
    public static long pastHour(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     *
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     *
     * @param timeMillis
     * @return
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    public static Integer formatDateTime2Minute(long timeMillis) {
        BigDecimal a = new BigDecimal(timeMillis);
        BigDecimal b = new BigDecimal(60 * 1000);
        BigDecimal minute = a.divide(b, 0, BigDecimal.ROUND_HALF_UP);
        int minutes = minute.intValue();
        return minutes == 0 ? 1 : minutes;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
//    public static double diffDays(Date before, Date after) {
//        DateTime dt1 = new DateTime(before).withTimeAtStartOfDay();
//        DateTime dt2 = new DateTime(after).withTimeAtStartOfDay();
//        int days = Days.daysBetween(dt1, dt2).getDays();
//        return days;
//    }

    /**
     * 获取两个日期之间的时间差
     *
     * @param before
     * @param after
     * @return
     */
    public static String diffTime(Date before, Date after) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = after.getTime() - before.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }


    /**
     * 日期比较
     * 返回值>0,前大于后
     *
     * @param dateTime1
     * @param dateTime2
     * @return
     */
    public static int dayCompare(Date dateTime1, Date dateTime2) {
        return dateTime1.compareTo(dateTime2);
    }

//    public static long diffMilliSeconds(Date before, Date after) {
//        long dt1 = new DateTime(before).getMillis();
//        long dt2 = new DateTime(after).getMillis();
//        return dt2 - dt1;
//    }

    /**
     * 获取两个时间的区间事件集合
     *
//     * @param startDate
//     * @param endDate
     * @return
     */
//    public static List<Date> getBlockDateList(Date startDate, Date endDate) {
//        List<Date> dateList = new LinkedList<>();
//        DateTime dateTime = new DateTime(startDate);
//        int i = 1;
//        while (true) {
//            if (startDate.after(endDate)) {
//                break;
//            }
//            dateList.add(startDate);
//            startDate = dateTime.plusDays(i).toDate();
//            i++;
//        }
//        return dateList;
//    }

    public static String getWorkingMonthStrat() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_MONTH) < 26) {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 26);
        return formatDate(calendar.getTime(), "yyyy-MM-dd");
    }

    public static String getWorkingMonthEnd() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_MONTH) > 25) {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 25);
        return formatDate(calendar.getTime(), "yyyy-MM-dd");
    }

    /**
     * yyMMddHHmmss
     */
    public static String getCurrentShortDateTimeStr() {
        return LocalDateTime.now().format(SHORT_DATETIME_FORMATTER);
    }
}